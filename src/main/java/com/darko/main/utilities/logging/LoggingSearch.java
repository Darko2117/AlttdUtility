package com.darko.main.utilities.logging;

import com.darko.main.Main;
import com.darko.main.other.Methods;
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

        if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.SearchLogsCommand")) return true;

        new BukkitRunnable() {
            @Override
            public void run() {

                try {
                    while (inUse) {
                        Main.getInstance().getLogger().info("Waiting until the last search finishes to start this one.");
                        Thread.sleep(1000);
                    }
                } catch (Throwable ignored) {
                }

                inUse = true;
                Main.getInstance().getLogger().info("Search started.");
                Long startingTime = System.currentTimeMillis();

                for (String fileName : new File(Main.getInstance().getDataFolder() + "/temporary-files").list())
                    if (!new File(Main.getInstance().getDataFolder() + "/temporary-files/" + fileName).delete())
                        Main.getInstance().getLogger().warning("Something failed during the deletion of temporary-files file " + fileName);

                if (args.length < 4) {
                    Methods.sendConfigMessage(sender, "Messages.IncorrectUsageSearchLogsCommand");
                    return;
                }

                String logName = args[0];
                if (!Logging.logNamesAndConfigPaths.containsKey(logName)) {
                    Methods.sendConfigMessage(sender, "Messages.IncorrectUsageSearchLogsCommand");
                    return;
                }

                Integer days;
                try {
                    days = Integer.parseInt(args[1]);
                } catch (Throwable throwable) {
                    Methods.sendConfigMessage(sender, "Messages.IncorrectUsageSearchLogsCommand");
                    return;
                }

                String silent = "";
                for (String arg : args) {
                    if (arg.equals("-silent")) {
                        silent = "-silent";
                        break;
                    }
                }

                List<String> argumentNames = new ArrayList<>();
                List<String> arguments = new ArrayList<>();
                for (Integer i = 2; i < args.length; i++) {
                    if (args[i].equals("-silent")) {
                        continue;
                    } else if (args[i].contains(":")) {
                        args[i] = args[i].replace(":", "");
                        argumentNames.add(args[i]);
                    } else {
                        arguments.add(args[i]);
                    }
                }

                List<File> filesToRead = new ArrayList<>();
                for (File file : new File(Main.getInstance().getDataFolder() + "/logs/").listFiles()) {
                    if (file.getName().contains(logName)) filesToRead.add(file);
                }
                for (File file : new File(Main.getInstance().getDataFolder() + "/compressed-logs/").listFiles()) {
                    if (file.getName().contains(logName)) filesToRead.add(file);
                }

                List<File> filesToRemove = new ArrayList<>();
                for (File file : filesToRead) {
                    try {

                        Integer day = Methods.getDateValuesFromString(file.getName().substring(0, 10))[0];
                        Integer month = Methods.getDateValuesFromString(file.getName().substring(0, 10))[1];
                        Integer year = Methods.getDateValuesFromString(file.getName().substring(0, 10))[2];
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

                for (File f : filesToRead) {

                    if (f.getName().contains(".gz")) {
                        String outputPath = Main.getInstance().getDataFolder() + "/temporary-files";
                        if (!Methods.uncompressFile(f.getAbsolutePath(), outputPath))
                            Main.getInstance().getLogger().warning("Something failed during extraction of the file " + f.getAbsolutePath());
                    } else {
                        Methods.copyPasteFile(new File(f.getAbsolutePath()), new File(Main.getInstance().getDataFolder() + "/temporary-files/" + f.getName()));
                    }

                }

                filesToRead.clear();
                for (String fileName : new File(Main.getInstance().getDataFolder() + "/temporary-files").list())
                    filesToRead.add(new File(Main.getInstance().getDataFolder() + "/temporary-files/" + fileName));

                try {

                    String outputFilePath = "";
                    outputFilePath = outputFilePath.concat(Main.getInstance().getConfig().getString("SearchLogs.OutputPath"));
                    outputFilePath = outputFilePath.concat(("/"));
                    outputFilePath = outputFilePath.concat((logName));
                    outputFilePath = outputFilePath.concat(("-"));
                    outputFilePath = outputFilePath.concat((sender.getName()));
                    outputFilePath = outputFilePath.concat(("-"));
                    outputFilePath = outputFilePath.concat(String.valueOf(new Random().nextInt(1000000)));
                    outputFilePath = outputFilePath.concat(silent);
                    outputFilePath = outputFilePath.concat(".txt");

                    File outputFile = new File(outputFilePath);
                    FileWriter writer = new FileWriter(outputFile, true);
                    writer.write("");

                    for (File f : filesToRead) {

                        BufferedReader bufferedReader = new BufferedReader(new FileReader(f));

                        String line;
                        while ((line = bufferedReader.readLine()) != null) {

                            List<Boolean> foundArguments = new ArrayList<>();

                            for (Integer i = 0; i < argumentNames.size(); i++) {

                                if (line.contains(argumentNames.get(i))) {

                                    StringBuilder argument = new StringBuilder(line);

                                    argument = argument.delete(0, argument.indexOf(argumentNames.get(i)) + argumentNames.get(i).length() + 1);
                                    argument = argument.delete(argument.indexOf("|"), argument.length());

                                    if (argument.toString().contains(arguments.get(i)))
                                        foundArguments.add(true);
                                    else foundArguments.add(false);

                                } else foundArguments.add(false);

                            }

                            if (!foundArguments.contains(false))
                                writer.write(line + "\n");

                        }

                        bufferedReader.close();

                    }

                    filesToRead.clear();
                    writer.close();

                    Methods.compressFile(outputFile.getAbsolutePath(), outputFile.getAbsolutePath().concat(".gz"));
                    if(!outputFile.delete())
                        Main.getInstance().getLogger().warning("Failed to delete the file " + outputFile.getAbsolutePath());

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

                for (String fileName : new File(Main.getInstance().getDataFolder() + "/temporary-files").list())
                    if (!new File(Main.getInstance().getDataFolder() + "/temporary-files/" + fileName).delete())
                        Main.getInstance().getLogger().warning("Something failed during the deletion of temporary-files file " + fileName);

                inUse = false;

                Long endingTime = System.currentTimeMillis();
                Long totalTime = endingTime - startingTime;
                Integer seconds = 0;
                while (totalTime >= 1000) {
                    seconds += 1;
                    totalTime -= 1000;
                }
                Main.getInstance().getLogger().info("Search completed, took " + seconds + "." + totalTime + "s.");

            }
        }.runTaskAsynchronously(Main.getInstance());

        return true;

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.SearchLogsCommand")) return null;

        if (args.length == 1) {

            List<String> logNames = new ArrayList<>();
            for (Map.Entry<String, String> entry : Logging.logNamesAndConfigPaths.entrySet()) {
                logNames.add(entry.getKey());
            }

            List<String> completions = new ArrayList<>();
            for (String name : logNames) {
                if (name.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(name);
                }
            }

            return completions;

        } else if (args.length == 2) {

            List<String> completions = new LinkedList<>();

            if (args[1].equals("")) {
                for (Integer i = 1; i < 10; i++) completions.add(i.toString());
                return completions;
            } else {
                try {
                    Integer number = Integer.parseInt(args[1]);
                    number *= 10;
                    for (Integer i = number; i < number + 10; i++) {
                        completions.add(i.toString());
                    }
                    return completions;
                } catch (Throwable ignored) {
                }
            }

        } else if (args.length > 2) {

            List<String> completions = new ArrayList<>();
            for (String argument : getArgumentListFromLogName(args[0])) {
                if (argument.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
                    completions.add(argument);
                }
            }

            return completions;

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

            return arguments;

        }

        return null;

    }

}
