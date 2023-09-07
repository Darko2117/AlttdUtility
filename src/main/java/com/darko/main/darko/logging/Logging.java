package com.darko.main.darko.logging;

import com.darko.main.AlttdUtility;
import com.darko.main.common.BukkitTasksCache;
import com.darko.main.common.Methods;
import com.darko.main.darko.logging.listeners.LoggingNoAPI;
import com.darko.main.darko.logging.logs.Log;
import org.bukkit.scheduler.BukkitRunnable;
import org.reflections.Reflections;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Logging {

    private static int cachedDayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

    private static final ConcurrentLinkedQueue<Log> logQueue = new ConcurrentLinkedQueue<>();

    private static Thread logWritingThread = null;

    public static volatile boolean isShuttingDown = false;
    public static volatile boolean logWritingIsDone = false;

    private static boolean isCompressing = false;

    private static final HashMap<String, Log> cachedLogs = new HashMap<>();

    public static void initiate() {

        for (String directory : List.of("logs", "compressed-logs", "search-output", "temporary-files"))
            new File(AlttdUtility.getInstance().getDataFolder() + File.separator + directory).mkdir();

        cacheDefaultLogs();

        createAllBlankLogFiles();

        startDateCheck();

        initializeLogWriting();

        LoggingNoAPI.startPlayerLocationLog();

    }

    private static void createAllBlankLogFiles() {

        for (Map.Entry<String, Log> entry : getCachedLogs().entrySet()) {
            Logging.addToLogWriteQueue(entry.getValue());
        }

    }

    private static void checkAndCompress() {

        String[] logsNames = new File(AlttdUtility.getInstance().getDataFolder() + File.separator + "logs").list();
        if (logsNames == null || logsNames.length == 0) {
            return;
        }

        for (String logName : logsNames) {

            File log = new File(AlttdUtility.getInstance().getDataFolder() + File.separator + "logs" + File.separator + logName);

            if (log.getName().startsWith(Methods.getDateStringYYYYMMDD()))
                continue;

            try {
                if (Methods.compressFile(log.getAbsolutePath(), log.getAbsolutePath().replace(File.separator + "logs" + File.separator, File.separator + "compressed-logs" + File.separator).concat(".gz"))) {
                    if (!log.delete())
                        AlttdUtility.getInstance().getLogger().warning("Something failed during deletion of the file " + log.getAbsolutePath());
                } else {
                    AlttdUtility.getInstance().getLogger().warning("Something failed during file compression of the file " + log.getAbsolutePath());
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        }

    }

    private static void checkAndDeleteOld() {

        String[] logsNames = new File(AlttdUtility.getInstance().getDataFolder() + File.separator + "compressed-logs").list();
        if (logsNames == null || logsNames.length == 0) {
            return;
        }

        for (String logName : logsNames) {

            try {

                File file = new File(AlttdUtility.getInstance().getDataFolder() + File.separator + "compressed-logs" + File.separator + logName);

                String fileNameWithoutDate = file.getName().substring(11, file.getName().indexOf(".txt.gz"));

                int numberOfLogsToKeepFromConfig = Logging.getCachedLogFromName(fileNameWithoutDate).getDaysOfLogsToKeep();

                if (numberOfLogsToKeepFromConfig == -1)
                    numberOfLogsToKeepFromConfig = 999999999;

                if (numberOfLogsToKeepFromConfig == 0)
                    throw new Throwable();

                int day = Methods.getDateValuesFromStringYYYYMMDD(logName.substring(0, 10))[0];
                int month = Methods.getDateValuesFromStringYYYYMMDD(logName.substring(0, 10))[1];
                int year = Methods.getDateValuesFromStringYYYYMMDD(logName.substring(0, 10))[2];
                LocalDate fileDateLD = LocalDate.of(year, month, day);

                int epochDayOfFileCreation = Math.toIntExact(fileDateLD.toEpochDay());
                int epochDayRightNow = Math.toIntExact(LocalDate.now().toEpochDay());

                if (epochDayOfFileCreation + numberOfLogsToKeepFromConfig < epochDayRightNow) {

                    if (file.delete())
                        AlttdUtility.getInstance().getLogger().info(file.getName() + " deleted.");
                    else
                        AlttdUtility.getInstance().getLogger().warning("Something failed during deletion of file " + file.getAbsolutePath());
                }

            } catch (Throwable throwable) {
                AlttdUtility.getInstance().getLogger().warning(logName + " has an invalid name. Please set it to yyyy-mm-dd format if you want the plugin to keep track of it and delete it after the specified time.");
            }

        }

    }

    private static void startDateCheck() {
        BukkitTasksCache.addTask(new BukkitRunnable() {
            @Override
            public void run() {
                try {

                    if (isCompressing)
                        return;

                    int dayNow = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

                    if (cachedDayOfMonth == dayNow)
                        return;

                    isCompressing = true;

                    checkAndCompress();
                    checkAndDeleteOld();
                    createAllBlankLogFiles();

                    cachedDayOfMonth = dayNow;

                    isCompressing = false;

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    isCompressing = false;
                }
            }
        }.runTaskTimerAsynchronously(AlttdUtility.getInstance(), 0, 20));
    }

    private static void initializeLogWriting() {

        if (logWritingThread != null)
            return;

        logWritingThread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (!isShuttingDown) {

                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ignored) {
                    }

                    while (logQueue.peek() != null)
                        writeToFile(logQueue.poll());

                }

                logWritingIsDone = true;

            }
        });
        logWritingThread.start();

    }

    public static void addToLogWriteQueue(Log log) {

        logQueue.add(log);

    }

    public static void writeToFile(Log log) {
        try {

            String logMessage = log.getLogArgumentsString();

            if (!log.getArguments().entrySet().iterator().next().getValue().isEmpty()) {
                logMessage = logMessage.concat("\n");
            } else {
                logMessage = "";
            }

            FileWriter writer = new FileWriter(AlttdUtility.getInstance().getDataFolder() + log.getPath(), true);
            writer.write(logMessage);
            writer.close();

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    static List<String> getArgumentListFromLogName(String logName) {

        LinkedList<String> arguments = new LinkedList<>();
        for (String argument : Logging.getCachedLogFromName(logName).getArguments().keySet().stream().toList()) {
            argument = argument.concat(":");
            arguments.add(argument);
        }

        if (arguments.contains("Location:") || arguments.contains("Area:"))
            arguments.add("-radius:");

        return arguments;

    }

    static List<String> getAdditionalLogNames() {

        List<String> logNames = new ArrayList<>();

        for (String s : AlttdUtility.getInstance().getConfig().getStringList("AdditionalLogs")) {

            s = s.substring(s.indexOf("Name:") + 5);
            s = s.substring(0, s.indexOf(" "));

            logNames.add(s);

        }

        return logNames;

    }

    public static HashMap<String, Log> getCachedLogs() {

        if (cachedLogs.isEmpty())
            cacheDefaultLogs();

        return cachedLogs;
    }

    public static void cacheDefaultLogs() {

        cachedLogs.clear();

        for (Class<? extends Log> log : new Reflections("com.darko.main.darko.logging.logs").getSubTypesOf(Log.class)) {

            Log logTemp = null;
            try {
                logTemp = log.getDeclaredConstructor().newInstance();
            } catch (Exception exception) {
                exception.printStackTrace();
                continue;
            }

            cachedLogs.put(logTemp.getName(), logTemp);

        }

    }

    public static Log getCachedLogFromName(String logName) {
        return cachedLogs.get(logName);
    }

    public static void updateCachedLogsFromConfig() {

        for (Map.Entry<String, Log> entry : getCachedLogs().entrySet()) {
            entry.getValue().setEnabled(AlttdUtility.getInstance().getConfig().getBoolean("Logging." + entry.getValue().getName() + ".Enabled"));
            entry.getValue().setDaysOfLogsToKeep(AlttdUtility.getInstance().getConfig().getInt("Logging." + entry.getValue().getName() + ".DaysOfLogsToKeep"));
        }

    }

}
