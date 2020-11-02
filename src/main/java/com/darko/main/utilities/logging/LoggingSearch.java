package com.darko.main.utilities.logging;

import com.darko.main.Main;
import com.darko.main.other.Methods;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.*;

public class LoggingSearch implements CommandExecutor, TabCompleter {

    static Boolean inUse = false;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0 || (!args[0].toLowerCase().equals("normal") && !args[0].toLowerCase().equals("special"))) {
            new Methods().sendConfigMessage(sender, "Messages.IncorrectUsageSearchLogsCommand");
            return true;
        }

        if (args[0].toLowerCase().equals("normal")) {

            if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.SearchNormalLogsCommand")) return true;

            new BukkitRunnable() {
                @Override
                public void run() {
                    try {

                        //Waiting until other searches finish. Only one can be active at the time.
                        while (inUse) {
                            Main.getInstance().getLogger().info("Waiting until the last search finishes to start this one.");
                            Thread.sleep(1000);
                        }

                        inUse = true;
                        sender.sendMessage(ChatColor.YELLOW + "Search started.");
                        Main.getInstance().getLogger().info("Search started.");
                        Long startingTime = System.currentTimeMillis();

                        //Deleting all the temporary files in case some are left.
                        clearTemporaryFiles();

                        if (args.length < 3) {
                            new Methods().sendConfigMessage(sender, "Messages.IncorrectUsageSearchNormalLogsCommand");
                            inUse = false;
                            return;
                        }

                        Integer days;
                        try {
                            days = Integer.parseInt(args[1]);
                        } catch (Throwable throwable) {
                            new Methods().sendConfigMessage(sender, "Messages.IncorrectUsageSearchNormalLogsCommand");
                            inUse = false;
                            return;
                        }

                        String silent = "";
                        for (String arg : args) {
                            if (arg.equals("-silent")) {
                                silent = "-silent";
                                break;
                            }
                        }

                        String searchString = "";
                        for (Integer i = 2; i < args.length; i++) {
                            if (args[i].equals("-silent")) continue;
                            if (!searchString.isEmpty())
                                searchString = searchString.concat(" ");
                            searchString = searchString.concat(args[i]);
                        }
                        searchString = searchString.toLowerCase();

                        List<String> blacklistedStrings = Main.getInstance().getConfig().getStringList("SearchLogs.NormalSearchBlacklistedStrings");

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

                                if (epochDayRightNow - days > epochDayOfFileCreation) {
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
                            if (f.getName().contains(".gz")) {
                                String outputPath = Main.getInstance().getDataFolder() + "/temporary-files/" + f.getName().replace(".gz", "");
                                if (!new Methods().uncompressFileGZIP(f.getAbsolutePath(), outputPath))
                                    Main.getInstance().getLogger().warning("Something failed during extraction of the file " + f.getAbsolutePath());
                            } else {
                                new Methods().copyPasteFile(new File(f.getAbsolutePath()), new File(Main.getInstance().getDataFolder() + "/temporary-files/" + f.getName()));
                            }
                        }

                        //Reloading the list of files that need to be read with the files from /temporary-files/.
                        filesToRead.clear();
                        filesToRead.addAll(Arrays.asList(new File(Main.getInstance().getDataFolder() + "/temporary-files").listFiles()));
                        filesToRead.sort(Comparator.naturalOrder());

                        String serverName = Main.getInstance().getDataFolder().getAbsolutePath().replace("\\", "/");
                        try {
                            serverName = serverName.substring(0, serverName.indexOf("/plugins"));
                            serverName = serverName.substring(serverName.lastIndexOf("/") + 1);
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                            serverName = "unknownServerName";
                        }

                        //The file is first written to /temporary-files/. It'll get moved after the whole search is completed.
                        String outputFilePath = "";
                        outputFilePath = outputFilePath.concat(Main.getInstance().getDataFolder() + "/temporary-files/");
                        outputFilePath = outputFilePath.concat(("/"));
                        outputFilePath = outputFilePath.concat(serverName);
                        outputFilePath = outputFilePath.concat("-");
                        outputFilePath = outputFilePath.concat(("normalLogs"));
                        outputFilePath = outputFilePath.concat(("-"));
                        outputFilePath = outputFilePath.concat((sender.getName()));
                        outputFilePath = outputFilePath.concat(("-"));
                        outputFilePath = outputFilePath.concat(String.valueOf(System.currentTimeMillis()));
                        outputFilePath = outputFilePath.concat(silent);
                        outputFilePath = outputFilePath.concat(".txt");

                        //Searching through the files for the arguments.
                        File outputFile = new File(outputFilePath);
                        FileWriter writer = new FileWriter(outputFile, true);
                        writer.write("");

                        for (File f : filesToRead) {

                            BufferedReader bufferedReader = new BufferedReader(new FileReader(f));
                            String line;

                            lineReader:
                            while ((line = bufferedReader.readLine()) != null) {

                                if (!line.toLowerCase().matches("(.*)" + searchString + "(.*)")) continue lineReader;

                                for (String s : blacklistedStrings) {
                                    if (line.toLowerCase().contains(s)) {
                                        writer.write(f.getName() + ":" + "This line contained a blacklisted string. Skipping it." + "\n");
                                        continue lineReader;
                                    }
                                }

                                //Writes only those lines which contain the provided search string, it skips over the rest.
                                writer.write(f.getName() + ":" + line + "\n");

                            }

                            bufferedReader.close();

                        }

                        writer.close();

                        //The maxFileSizeWithoutCompression is in MB so it's multiplied by 1mil to get the size in bytes.
                        Integer maxFileSizeWithoutCompression = Main.getInstance().getConfig().getInt("SearchLogs.MaxFileSizeWithoutCompression");
                        maxFileSizeWithoutCompression *= 1000000;

                        //If the file is bigger than the defined limit, it gets compressed.
                        if (outputFile.length() > maxFileSizeWithoutCompression) {

                            new Methods().compressFile(outputFile.getAbsolutePath(), outputFile.getAbsolutePath().concat(".gz"));

                            outputFile = new File(outputFile.getAbsolutePath().concat(".gz"));

                        }

                        //The file with all the results is copied to the defined output path and all the other files are removed.
                        String tempOutputFilePathString = outputFile.getAbsolutePath().replace("\\", "/");
                        tempOutputFilePathString = tempOutputFilePathString.substring(tempOutputFilePathString.lastIndexOf("/") + 1);
                        tempOutputFilePathString = Main.getInstance().getConfig().getString("SearchLogs.OutputPath") + "/" + tempOutputFilePathString;

                        if (outputFile.length() != 0)
                            new Methods().copyPasteFile(outputFile, (new File(tempOutputFilePathString)));
                        else {
                            sender.sendMessage(ChatColor.RED + "The results file is empty, not sending it.");
                        }

                        Long endingTime = System.currentTimeMillis();
                        Long totalTime = endingTime - startingTime;
                        Integer seconds = 0;
                        while (totalTime >= 1000) {
                            seconds += 1;
                            totalTime -= 1000;
                        }

                        Double totalFileSize = 0d;
                        for (File file : filesToRead)
                            totalFileSize += file.length();
                        totalFileSize /= 1000000;

                        clearTemporaryFiles();

                        sender.sendMessage(ChatColor.GREEN + "Search completed, took " + seconds + "." + totalTime + "s. Scanned " + filesToRead.size() + " files with a total size of " + Math.round(totalFileSize * 100.0) / 100.0 + "MB.");
                        Main.getInstance().getLogger().info("Search completed, took " + seconds + "." + totalTime + "s. Scanned " + filesToRead.size() + " files with a total size of " + Math.round(totalFileSize * 100.0) / 100.0 + "MB.");

                        inUse = false;

                    } catch (Throwable throwable) {
                        new Methods().sendConfigMessage(sender, "Messages.IncorrectUsageSearchNormalLogsCommand");
                        throwable.printStackTrace();
                        inUse = false;
                    }
                }
            }.runTaskAsynchronously(Main.getInstance());

        } else if (args[0].toLowerCase().equals("special")) {

            if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.SearchSpecialLogsCommand")) return true;

            new BukkitRunnable() {
                @Override
                public void run() {
                    try {

                        //Waiting until other searches finish. Only one can be active at the time.
                        while (inUse) {
                            Main.getInstance().getLogger().info("Waiting until the last search finishes to start this one.");
                            Thread.sleep(1000);
                        }

                        inUse = true;
                        sender.sendMessage(ChatColor.YELLOW + "Search started.");
                        Main.getInstance().getLogger().info("Search started.");
                        Long startingTime = System.currentTimeMillis();

                        //Deleting all the temporary files in case some are left.
                        clearTemporaryFiles();

                        if (args.length < 5) {
                            new Methods().sendConfigMessage(sender, "Messages.IncorrectUsageSearchSpecialLogsCommand");
                            inUse = false;
                            return;
                        }

                        List<String> logNames = Arrays.asList(args[1].split(","));
                        for (String s : logNames) {
                            if (!Logging.logNamesAndConfigPaths.containsKey(s)) {
                                new Methods().sendConfigMessage(sender, "Messages.IncorrectUsageSearchSpecialLogsCommand");
                                inUse = false;
                                return;
                            }
                        }

                        Integer days;
                        try {
                            days = Integer.parseInt(args[2]);
                        } catch (Throwable throwable) {
                            new Methods().sendConfigMessage(sender, "Messages.IncorrectUsageSearchSpecialLogsCommand");
                            inUse = false;
                            return;
                        }

                        String silent = "";
                        for (String arg : args) {
                            if (arg.equals("-silent")) {
                                silent = "-silent";
                                break;
                            }
                        }

                        HashMap<String, String> arguments = new HashMap<>();

                        for (Integer i = 3; i < args.length; i++) {

                            String argumentKey;
                            String argumentValues = "";

                            if (args[i].toLowerCase().equals("-silent")) continue;
                            if (!args[i].contains(":")) continue;

                            argumentKey = args[i].replace(":", "");

                            for (Integer j = i + 1; j < args.length; j++) {

                                if (args[j].toLowerCase().equals("-silent")) break;
                                if (args[j].contains(":")) break;

                                if (!argumentValues.isEmpty())
                                    argumentValues = argumentValues.concat(" ");
                                argumentValues = argumentValues.concat(args[j]);

                            }

                            arguments.put(argumentKey, argumentValues);

                        }

                        //Getting a list of all the files with the correct log name from /logs/ and /compressed-logs/.
                        List<File> filesToRead = new ArrayList<>();
                        for (File file : new File(Main.getInstance().getDataFolder() + "/logs/").listFiles()) {
                            for (String s : logNames) {
                                if (file.getName().contains(s)) {
                                    filesToRead.add(file);
                                    break;
                                }
                            }
                        }
                        for (File file : new File(Main.getInstance().getDataFolder() + "/compressed-logs/").listFiles()) {
                            for (String s : logNames) {
                                if (file.getName().contains(s)) {
                                    filesToRead.add(file);
                                    break;
                                }
                            }
                        }

                        //Removing from that list all the ones that aren't in the defined time limit.
                        List<File> filesToRemove = new ArrayList<>();
                        for (File file : filesToRead) {
                            try {

                                Integer day = new Methods().getDateValuesFromStringDDMMYYYY(file.getName().substring(0, 10))[0];
                                Integer month = new Methods().getDateValuesFromStringDDMMYYYY(file.getName().substring(0, 10))[1];
                                Integer year = new Methods().getDateValuesFromStringDDMMYYYY(file.getName().substring(0, 10))[2];
                                LocalDate fileDateLD = LocalDate.of(year, month, day);

                                Integer epochDayOfFileCreation = Math.toIntExact(fileDateLD.toEpochDay());
                                Integer epochDayRightNow = Math.toIntExact(LocalDate.now().toEpochDay());

                                if (epochDayRightNow - days > epochDayOfFileCreation) {
                                    filesToRemove.add(file);
                                }

                            } catch (Throwable ignored) {
                                filesToRemove.add(file);
                            }
                        }
                        filesToRead.removeAll(filesToRemove);

                        //Copying all the files that need to be read to /temporary-files/. Uncompressing the compressed ones.
                        for (File f : filesToRead) {
                            if (f.getName().contains(".gz")) {
                                String outputPath = Main.getInstance().getDataFolder() + "/temporary-files";
                                if (!new Methods().uncompressFile(f.getAbsolutePath(), outputPath))
                                    Main.getInstance().getLogger().warning("Something failed during extraction of the file " + f.getAbsolutePath());
                            } else {
                                new Methods().copyPasteFile(new File(f.getAbsolutePath()), new File(Main.getInstance().getDataFolder() + "/temporary-files/" + f.getName()));
                            }
                        }

                        //Reloading the list of files that need to be read with the files from /temporary-files/.
                        filesToRead.clear();
                        filesToRead.addAll(Arrays.asList(new File(Main.getInstance().getDataFolder() + "/temporary-files").listFiles()));
                        filesToRead.sort(Comparator.naturalOrder());

                        String serverName = Main.getInstance().getDataFolder().getAbsolutePath().replace("\\", "/");
                        try {
                            serverName = serverName.substring(0, serverName.indexOf("/plugins"));
                            serverName = serverName.substring(serverName.lastIndexOf("/") + 1);
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                            serverName = "unknownServerName";
                        }

                        String logNamesString = "";
                        for (String s : logNames) {
                            if (!logNamesString.isEmpty())
                                logNamesString = logNamesString.concat("-");
                            logNamesString = logNamesString.concat(s);
                        }

                        //The file is first written to /temporary-files/. It'll get moved after the whole search is completed.
                        String outputFilePath = "";
                        outputFilePath = outputFilePath.concat(Main.getInstance().getDataFolder() + "/temporary-files/");
                        outputFilePath = outputFilePath.concat(("/"));
                        outputFilePath = outputFilePath.concat(serverName);
                        outputFilePath = outputFilePath.concat("-");
                        outputFilePath = outputFilePath.concat((logNamesString));
                        outputFilePath = outputFilePath.concat(("-"));
                        outputFilePath = outputFilePath.concat((sender.getName()));
                        outputFilePath = outputFilePath.concat(("-"));
                        outputFilePath = outputFilePath.concat(String.valueOf(System.currentTimeMillis()));
                        outputFilePath = outputFilePath.concat(silent);
                        outputFilePath = outputFilePath.concat(".txt");

                        //Searching through the files for the arguments.
                        File outputFile = new File(outputFilePath);
                        FileWriter writer = new FileWriter(outputFile, true);
                        writer.write("");

                        String errorReadLines = "";

                        for (File f : filesToRead) {

                            BufferedReader bufferedReader = new BufferedReader(new FileReader(f));
                            String line;

                            lineReader:
                            while ((line = bufferedReader.readLine()) != null) {

                                String lineCopy = line;

                                if (!lineCopy.startsWith("|") || !lineCopy.endsWith("|")) {
                                    Main.getInstance().getLogger().warning("Error reading line " + lineCopy);
                                    continue lineReader;
                                }

                                HashMap<String, String> lineArguments = new HashMap<>();

                                while (lineCopy.indexOf("|") + 1 != lineCopy.length() && lineCopy.contains(":")) {
                                    try {

                                        lineCopy = lineCopy.substring(lineCopy.indexOf("|") + 1);
                                        String argumentName = lineCopy.substring(0, lineCopy.indexOf(":"));
                                        lineCopy = lineCopy.substring(lineCopy.indexOf(":") + 1);
                                        String argument = lineCopy.substring(0, lineCopy.indexOf("|"));
                                        lineCopy = lineCopy.substring(lineCopy.indexOf("|"));

                                        lineArguments.put(argumentName, argument);

                                    } catch (Throwable throwable) {
                                        Main.getInstance().getLogger().warning("Error reading line " + line);
                                    }
                                }

                                try {
                                    for (Map.Entry<String, String> inputArguments : arguments.entrySet()) {

                                        if (!lineArguments.containsKey(inputArguments.getKey()) || !lineArguments.get(inputArguments.getKey()).toLowerCase().contains(inputArguments.getValue().toLowerCase())) {
                                            continue lineReader;
                                        }

                                    }
                                } catch (Throwable throwable) {
                                    Main.getInstance().getLogger().warning("Error reading line " + line);
                                    errorReadLines = errorReadLines.concat("Encountered an error while reading the line, probably doesn't contain the searched arguments:" + line + "\n");
                                }

                                //Writes only those lines which contain all the provided arguments, it skips over the rest.
                                writer.write(f.getName() + ":" + line + "\n");

                            }

                            bufferedReader.close();

                        }

                        writer.write(errorReadLines);

                        writer.close();

                        //The maxFileSizeWithoutCompression is in MB so it's multiplied by 1mil to get the size in bytes.
                        Integer maxFileSizeWithoutCompression = Main.getInstance().getConfig().getInt("SearchLogs.MaxFileSizeWithoutCompression");
                        maxFileSizeWithoutCompression *= 1000000;

                        //If the file is bigger than the defined limit, it gets compressed.
                        if (outputFile.length() > maxFileSizeWithoutCompression) {

                            new Methods().compressFile(outputFile.getAbsolutePath(), outputFile.getAbsolutePath().concat(".gz"));

                            outputFile = new File(outputFile.getAbsolutePath().concat(".gz"));

                        }

                        //The file with all the results is copied to the defined output path and all the other files are removed.
                        String tempOutputFilePathString = outputFile.getAbsolutePath().replace("\\", "/");
                        tempOutputFilePathString = tempOutputFilePathString.substring(tempOutputFilePathString.lastIndexOf("/") + 1);
                        tempOutputFilePathString = Main.getInstance().getConfig().getString("SearchLogs.OutputPath") + "/" + tempOutputFilePathString;

                        if (outputFile.length() != 0)
                            new Methods().copyPasteFile(outputFile, (new File(tempOutputFilePathString)));
                        else {
                            sender.sendMessage(ChatColor.RED + "The results file is empty, not sending it.");
                        }

                        Long endingTime = System.currentTimeMillis();
                        Long totalTime = endingTime - startingTime;
                        Integer seconds = 0;
                        while (totalTime >= 1000) {
                            seconds += 1;
                            totalTime -= 1000;
                        }

                        Double totalFileSize = 0d;
                        for (File file : filesToRead)
                            totalFileSize += file.length();
                        totalFileSize /= 1000000;

                        clearTemporaryFiles();

                        sender.sendMessage(ChatColor.GREEN + "Search completed, took " + seconds + "." + totalTime + "s. Scanned " + filesToRead.size() + " files with a total size of " + Math.round(totalFileSize * 100.0) / 100.0 + "MB.");
                        Main.getInstance().getLogger().info("Search completed, took " + seconds + "." + totalTime + "s. Scanned " + filesToRead.size() + " files with a total size of " + Math.round(totalFileSize * 100.0) / 100.0 + "MB.");

                        inUse = false;

                    } catch (Throwable throwable) {
                        new Methods().sendConfigMessage(sender, "Messages.IncorrectUsageSearchSpecialLogsCommand");
                        throwable.printStackTrace();
                        inUse = false;
                    }
                }
            }.runTaskAsynchronously(Main.getInstance());

        }

        return true;

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        try {

            if (args.length == 1) {

                List<String> choices = new ArrayList<>();
                choices.add("normal");
                choices.add("special");

                List<String> completions = new ArrayList<>();
                for (String s : choices) {
                    if (s.toLowerCase().startsWith(args[0].toLowerCase())) {
                        completions.add(s);
                    }
                }

                return completions;

            }

            if (args.length == 2 && args[0].toLowerCase().equals("normal") && args[1].isEmpty()) {

                List<String> completions = new ArrayList<>();

                for (Integer i = 0; i < 10; i++) {
                    completions.add(i.toString());
                }

                return completions;

            }

            if (args.length == 3 && args[0].toLowerCase().equals("special") && args[2].isEmpty()) {

                List<String> completions = new ArrayList<>();

                for (Integer i = 0; i < 10; i++) {
                    completions.add(i.toString());
                }

                return completions;

            }

            if (args[0].toLowerCase().equals("special")) {

                if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.SearchSpecialLogsCommand")) return null;

                if (args.length == 2) {

                    String argument = args[1];

                    if (argument.contains(",")) argument = argument.substring(argument.lastIndexOf(",") + 1);

                    List<String> logNames = new ArrayList<>();
                    for (Map.Entry<String, String> entry : Logging.logNamesAndConfigPaths.entrySet()) {
                        logNames.add(entry.getKey());
                    }

                    List<String> completions = new ArrayList<>();
                    for (String name : logNames) {
                        if (name.startsWith(argument)) {
                            completions.add(args[1].substring(0, args[1].lastIndexOf(argument)) + name);
                        }
                    }

                    return completions;

                } else if (args.length > 3 && args.length % 2 == 0) {

                    String logNameArgument = args[1];
                    if (logNameArgument.contains(","))
                        logNameArgument = logNameArgument.substring(logNameArgument.lastIndexOf(",") + 1);

                    List<String> completions = new ArrayList<>();
                    for (String argument : getArgumentListFromLogName(logNameArgument)) {
                        if (argument.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
                            completions.add(argument);
                        }
                    }

                    return completions;

                }

            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return null;

    }

    static List<String> getArgumentListFromLogName(String logName) {

        List<String> arguments = new ArrayList<>();

        if (logName.equals("claimsCreated") || logName.equals("claimsDeleted") || logName.equals("claimsExpired") || logName.equals("claimsModified")) {

            arguments.add("Time:");
            arguments.add("User:");
            arguments.add("LowestY:");
            arguments.add("Area:");

            return arguments;

        } else if (logName.equals("eggsThrown")) {

            arguments.add("Time:");
            arguments.add("User:");
            arguments.add("Location:");
            arguments.add("ClaimOwner:");

            return arguments;

        } else if (logName.equals("droppedItems")) {

            arguments.add("Time:");
            arguments.add("User:");
            arguments.add("Item:");
            arguments.add("Location:");

            return arguments;

        } else if (logName.equals("itemsPlacedInItemFrames")) {

            arguments.add("Time:");
            arguments.add("User:");
            arguments.add("Item:");
            arguments.add("Location:");

            return arguments;

        } else if (logName.equals("itemsTakenOutOfItemFrames")) {

            arguments.add("Time:");
            arguments.add("User:");
            arguments.add("Item:");
            arguments.add("Location:");

            return arguments;

        } else if (logName.equals("mcmmoRepairUse")) {

            arguments.add("Time:");
            arguments.add("User:");
            arguments.add("Item:");

            return arguments;

        } else if (logName.equals("cratePrizes")) {

            arguments.add("Time:");
            arguments.add("User:");
            arguments.add("Items:");
            arguments.add("Commands:");
            arguments.add("Crate:");

            return arguments;

        } else if (logName.equals("spawnLimitReached")) {

            arguments.add("Time:");
            arguments.add("EntityType:");
            arguments.add("Location:");
            arguments.add("ClaimOwner:");

            return arguments;

        } else if (logName.equals("pickedUpItems")) {

            arguments.add("Time:");
            arguments.add("User:");
            arguments.add("Item:");
            arguments.add("Location:");

            return arguments;

        } else if (logName.equals("uiClicks")) {

            arguments.add("Time:");
            arguments.add("User:");
            arguments.add("InventoryName:");
            arguments.add("ClickedItem:");
            arguments.add("Location:");

            return arguments;

        } else if (logName.equals("itemsBroken")) {

            arguments.add("Time:");
            arguments.add("User:");
            arguments.add("Item:");
            arguments.add("Location:");

            return arguments;

        } else if (logName.equals("numberOfClaimsNotification")) {

            arguments.add("Time:");
            arguments.add("User:");
            arguments.add("NumberOfClaims:");

            return arguments;

        }

        return null;

    }

    static void clearTemporaryFiles() {

        for (File file : new File(Main.getInstance().getDataFolder() + "/temporary-files").listFiles())
            if (!file.delete())
                Main.getInstance().getLogger().warning("Something failed during the deletion of temporary-files file " + file.getAbsolutePath());

    }

}
