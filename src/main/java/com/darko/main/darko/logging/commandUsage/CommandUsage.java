package com.darko.main.darko.logging.commandUsage;

import com.darko.main.AlttdUtility;
import com.darko.main.common.Methods;
import com.darko.main.darko.logging.LoggingSearch;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.*;

public class CommandUsage implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        new BukkitRunnable() {
            @Override
            public void run() {
                try {

                    if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.CommandUsageCommand"))
                        return;

                    if (strings.length != 2) {
                        new Methods().sendConfigMessage(commandSender, "Messages.InvalidUsageCommandUsageCommand");
                        return;
                    }

                    String username = strings[0];
                    Integer numberOfDays;

                    try {
                        numberOfDays = Integer.parseInt(strings[1]);
                    } catch (Throwable throwable) {
                        new Methods().sendConfigMessage(commandSender, "Messages.InvalidUsageCommandUsageCommand");
                        return;
                    }

                    if (LoggingSearch.inUse) {
                        commandSender.sendMessage(ChatColor.RED + "A search is in progress, this one will start when that one finishes.");
                    }

                    while (LoggingSearch.inUse) {
                        AlttdUtility.getInstance().getLogger().info("Waiting until the last search finishes to start this one.");
                        Thread.sleep(1000);
                    }

                    LoggingSearch.inUse = true;
                    commandSender.sendMessage(ChatColor.YELLOW + "Search started.");
                    AlttdUtility.getInstance().getLogger().info("Search started.");

                    LoggingSearch.clearTemporaryFiles();

                    //Getting a list of all the log files.
                    String logsDirectoryPath = new File(".").getAbsolutePath() + "/logs/";
                    List<File> filesToRead = new ArrayList<>(Arrays.asList(new File(logsDirectoryPath).listFiles()));

                    //Removing from that list all the ones that aren't in the defined time limit.
                    List<File> filesToRemove = new ArrayList<>();
                    for (File file : filesToRead) {
                        try {

                            Integer day = new Methods().getDateValuesFromStringYYYYMMDD(file.getName().substring(0, 10))[0];
                            Integer month = new Methods().getDateValuesFromStringYYYYMMDD(file.getName().substring(0, 10))[1];
                            Integer year = new Methods().getDateValuesFromStringYYYYMMDD(file.getName().substring(0, 10))[2];
                            LocalDate fileDateLD = LocalDate.of(year, month, day);

                            Integer epochDayOfFileCreation = Math.toIntExact(fileDateLD.toEpochDay());
                            Integer epochDayRightNow = Math.toIntExact(LocalDate.now().toEpochDay());

                            if (epochDayRightNow - numberOfDays > epochDayOfFileCreation) {
                                filesToRemove.add(file);
                            }

                        } catch (Throwable ignored) {
                            if (!file.getName().equals("latest.log"))
                                filesToRemove.add(file);
                        }
                    }
                    filesToRead.removeAll(filesToRemove);

                    //Copying all the files that need to be read to /temporary-files/. Uncompressing the compressed ones.
                    for (File f : filesToRead) {
                        try {

                            if (f.getName().contains(".gz")) {
                                String outputPath = AlttdUtility.getInstance().getDataFolder() + "/temporary-files/" + f.getName().replace(".gz", "");
                                if (!new Methods().uncompressFileGZIP(f.getAbsolutePath(), outputPath))
                                    AlttdUtility.getInstance().getLogger().warning("Something failed during extraction of the file " + f.getAbsolutePath());
                            } else {
                                new Methods().copyPasteFile(new File(f.getAbsolutePath()), new File(AlttdUtility.getInstance().getDataFolder() + "/temporary-files/" + f.getName()));
                            }

                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }

                    //Reloading the list of files that need to be read with the files from /temporary-files/.
                    filesToRead.clear();
                    filesToRead.addAll(Arrays.asList(new File(AlttdUtility.getInstance().getDataFolder() + "/temporary-files").listFiles()));
                    filesToRead.sort(Comparator.naturalOrder());

                    String serverName = AlttdUtility.getInstance().getDataFolder().getAbsolutePath().replace("\\", "/");
                    try {
                        serverName = serverName.substring(0, serverName.indexOf("/plugins"));
                        serverName = serverName.substring(serverName.lastIndexOf("/") + 1);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                        serverName = "unknownServerName";
                    }

                    //The file is first written to /temporary-files/. It'll get moved after the whole search is completed.
                    String outputFilePath = "";
                    outputFilePath = outputFilePath.concat(AlttdUtility.getInstance().getDataFolder() + "/temporary-files/");
                    outputFilePath = outputFilePath.concat("/");
                    outputFilePath = outputFilePath.concat(serverName);
                    outputFilePath = outputFilePath.concat("-");
                    outputFilePath = outputFilePath.concat("commandUsage");
                    outputFilePath = outputFilePath.concat("-");
                    outputFilePath = outputFilePath.concat(commandSender.getName());
                    outputFilePath = outputFilePath.concat("-");
                    outputFilePath = outputFilePath.concat(String.valueOf(System.currentTimeMillis()));
                    outputFilePath = outputFilePath.concat("-silent");
                    outputFilePath = outputFilePath.concat(".txt");

                    //Searching through the files for the arguments.
                    File outputFile = new File(outputFilePath);
                    FileWriter writer = new FileWriter(outputFile, true);
                    writer.write("");

                    //HashMap<CommandName, NumberOfUses>
                    HashMap<String, Integer> results = new HashMap<>();

                    for (File f : filesToRead) {
                        try {

                            BufferedReader bufferedReader = new BufferedReader(new FileReader(f));
                            String line;

                            lineReader:
                            while ((line = bufferedReader.readLine()) != null) {

                                if (!line.contains(" issued server command: ")) continue lineReader;

                                String lineUser = line;
                                lineUser = lineUser.substring(0, lineUser.indexOf(" issued server command: "));
                                lineUser = lineUser.substring(lineUser.lastIndexOf(" ") + 1);
                                if (!lineUser.equalsIgnoreCase(username)) continue lineReader;

                                String lineCommand = line;
                                lineCommand = lineCommand.substring(lineCommand.indexOf(" issued server command: ") + 24);

                                if (results.containsKey(lineCommand)) {
                                    results.put(lineCommand, results.get(lineCommand) + 1);
                                } else {
                                    results.put(lineCommand, 1);
                                }

                            }

                            bufferedReader.close();

                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }

                    //Sorting the HashMap by values
                    List<Map.Entry<String, Integer>> list = new LinkedList<>(results.entrySet());
                    list.sort(Map.Entry.comparingByValue());
                    List<Map.Entry<String, Integer>> list1 = new LinkedList<>();
                    for (Integer i = list.size() - 1; i >= 0; i--) {
                        list1.add(list.get(i));
                    }
                    HashMap<String, Integer> temp = new LinkedHashMap<>();
                    for (Map.Entry<String, Integer> aa : list1) {
                        temp.put(aa.getKey(), aa.getValue());
                    }

                    writer.write(username + " command usage results");

                    for (Map.Entry<String, Integer> entry : temp.entrySet()) {
                        writer.write(entry.getKey() + " ----- " + entry.getValue() + "\n");
                    }

                    writer.close();

                    //The file with all the results is copied to the defined output path and all the other files are removed.
                    String tempOutputFilePathString = outputFile.getAbsolutePath().replace("\\", "/");
                    tempOutputFilePathString = tempOutputFilePathString.substring(tempOutputFilePathString.lastIndexOf("/") + 1);
                    tempOutputFilePathString = AlttdUtility.getInstance().getConfig().getString("SearchLogs.OutputPath") + "/" + tempOutputFilePathString;

                    if (outputFile.length() != 0)
                        new Methods().copyPasteFile(outputFile, (new File(tempOutputFilePathString)));
                    else {
                        commandSender.sendMessage(ChatColor.RED + "The results file is empty, not sending it.");
                    }

                    LoggingSearch.clearTemporaryFiles();

                    commandSender.sendMessage(ChatColor.GREEN + "Search completed!");
                    AlttdUtility.getInstance().getLogger().info("Search completed!");

                    LoggingSearch.inUse = false;

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    LoggingSearch.inUse = false;
                }
            }
        }.runTaskAsynchronously(AlttdUtility.getInstance());

        return true;

    }

}
