package com.darko.main.darko.customCommandMacro;

import com.darko.main.AlttdUtility;
import com.darko.main.common.database.Database;
import com.darko.main.common.Methods;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomCommandMacroCommand implements CommandExecutor, TabCompleter, Listener {

    private static final HashMap<Player, List<CustomCommandMacro>> cachedMacros = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.CustomCommandMacroCommand"))
            return true;

        if (!(sender instanceof Player player)) {
            new Methods().sendConfigMessage(sender, "Messages.PlayerOnlyCommandMessage");
            return true;
        }

        String UUID = player.getUniqueId().toString();
        String username = player.getName();

        if (args.length == 0) {
            new Methods().sendConfigMessage(sender, "Messages.CustomCommandMacroUsage");
            return true;
        }

        if (args[0].equals("add")) {

            if (args.length < 3) {
                new Methods().sendConfigMessage(sender, "Messages.CustomCommandMacroUsage");
                return true;
            }

            String macroName = args[1].toLowerCase();
            String commandString = "";

            for (int i = 2; i < args.length; i++) {
                commandString = commandString.concat(args[i]);
                if (i < args.length - 1)
                    commandString = commandString.concat(" ");
            }
            if (!commandString.startsWith("/"))
                commandString = "/" + commandString;

            // Blacklisted commands check
            {
                String commandName = commandString.substring(1);
                while (commandName.contains("/"))
                    commandName = commandName.substring(1);
                if (commandName.contains(" "))
                    commandName = commandName.substring(0, commandName.indexOf(" "));

                List<String> blacklistedCommands = AlttdUtility.getInstance().getConfig().getStringList("CustomCommandMacro.BlacklistedCommands");
                if (blacklistedCommands.contains(commandName)) {
                    new Methods().sendConfigMessage(sender, "Messages.CustomCommandMacroBlacklistedCommand");
                    return true;
                }
            }

            if (getMacroNamesForPlayer(player).contains(macroName)) {
                new Methods().sendConfigMessage(sender, "Messages.CustomCommandMacroAlreadyExists");
                return true;
            }

            String finalCommandString = commandString;
            new BukkitRunnable() {
                @Override
                public void run() {

                    String statement = "INSERT INTO custom_command_macro(UUID, Username, MacroName, Command) VALUES(" + "'" + UUID + "', " + "'" + username + "', " + "'" + macroName + "', " + "'" + finalCommandString + "');";

                    try {

                        Database.connection.prepareStatement(statement).executeUpdate();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', AlttdUtility.getInstance().getConfig().getString("Messages.CustomCommandMacroSavedMessage").replace("%macroName%", macroName).replace("%command%", finalCommandString)));

                        cachePlayer(player);

                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }

                }
            }.runTaskAsynchronously(AlttdUtility.getInstance());

        } else if (args[0].equals("remove")) {

            if (args.length < 2) {
                new Methods().sendConfigMessage(sender, "Messages.CustomCommandMacroUsage");
                return true;
            }

            String macroName = args[1].toLowerCase();

            if (!getMacroNamesForPlayer(player).contains(macroName)) {
                new Methods().sendConfigMessage(sender, "Messages.CustomCommandMacroDoesntExist");
                return true;
            }

            new BukkitRunnable() {
                @Override
                public void run() {

                    String statement = "DELETE FROM custom_command_macro WHERE MacroName = '" + macroName + "' AND UUID = '" + UUID + "';";

                    try {

                        Database.connection.prepareStatement(statement).executeUpdate();
                        new Methods().sendConfigMessage(sender, "Messages.CustomCommandMacroRemovedMessage");

                        cachePlayer(player);

                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }

                }
            }.runTaskAsynchronously(AlttdUtility.getInstance());

        } else if (args[0].equals("edit")) {

            if (args.length < 3) {
                new Methods().sendConfigMessage(sender, "Messages.CustomCommandMacroUsage");
                return true;
            }

            String macroName = args[1].toLowerCase();
            String commandString = "";

            if (!getMacroNamesForPlayer(player).contains(macroName)) {
                new Methods().sendConfigMessage(sender, "Messages.CustomCommandMacroDoesntExist");
                return true;
            }

            for (Integer i = 2; i < args.length; i++) {
                commandString = commandString.concat(args[i]);
                if (i < args.length - 1)
                    commandString = commandString.concat(" ");
            }
            if (!commandString.startsWith("/"))
                commandString = "/" + commandString;

            String finalCommandString = commandString;
            new BukkitRunnable() {
                @Override
                public void run() {

                    String statement = "UPDATE custom_command_macro SET Command = '" + finalCommandString + "' WHERE MacroName = '" + macroName + "' AND UUID = '" + UUID + "';";

                    try {

                        Database.connection.prepareStatement(statement).executeUpdate();
                        new Methods().sendConfigMessage(sender, "Messages.CustomCommandMacroEdited");

                        cachePlayer(player);

                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }

                }
            }.runTaskAsynchronously(AlttdUtility.getInstance());

        } else if (getMacroNamesForPlayer(player).contains(args[0].toLowerCase())) {

            StringBuilder commandStringBuilder = new StringBuilder(getMacroForPlayer(player, args[0].toLowerCase()).getCommand());
            String[] commandArgs = StringUtils.split(commandStringBuilder.toString(), " ");

            int variableIndex = 1;

            for (int i = 1; i < commandArgs.length; i++) {

                if (!(commandArgs[i].startsWith("{") && commandArgs[i].endsWith("}")))
                    continue;

                try {
                    commandArgs[i] = args[variableIndex];
                    variableIndex++;
                } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                    player.sendMessage(ChatColor.RED + "You are missing variables needed for that command:\n" + commandStringBuilder);
                    return true;
                }

            }

            commandStringBuilder = new StringBuilder();
            for (String s : commandArgs) {
                commandStringBuilder.append(s).append(" ");
            }
            commandStringBuilder.deleteCharAt(commandStringBuilder.length() - 1);

            player.chat(commandStringBuilder.toString());

        } else {
            new Methods().sendConfigMessage(sender, "Messages.CustomCommandMacroDoesntExist");
        }

        return true;

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.CustomCommandMacroCommand"))
            return null;

        if (!(sender instanceof Player player))
            return null;

        if (args.length == 1) {

            List<String> commands = new ArrayList<>();
            commands.add("add");
            commands.add("remove");
            commands.add("edit");

            List<String> completions = new ArrayList<>();

            for (String string : commands) {
                if (string.startsWith(args[0])) {
                    completions.add(string);
                }
            }

            for (String string : getMacroNamesForPlayer(player)) {
                if (string.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(string);
                }
            }

            return completions;

        } else if (args.length == 2 && args[0].equals("remove")) {

            List<String> completions = new ArrayList<>();
            for (String macroName : getMacroNamesForPlayer(player)) {
                if (macroName.startsWith(args[1])) {
                    completions.add(macroName);
                }
            }
            return completions;

        } else if (args.length == 2 && args[0].equals("edit")) {

            List<String> completions = new ArrayList<>();
            for (String macroName : getMacroNamesForPlayer(player)) {
                if (macroName.startsWith(args[1])) {
                    completions.add(macroName);
                }
            }
            return completions;

        }

        return null;

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                cachePlayer(event.getPlayer());
            }
        }.runTaskAsynchronously(AlttdUtility.getInstance());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        cachedMacros.remove(event.getPlayer());
    }

    public static void cachePlayer(Player player) {
        try {

            String statement = "SELECT * FROM custom_command_macro WHERE UUID = '" + player.getUniqueId() + "'";
            ResultSet rs = Database.connection.prepareStatement(statement).executeQuery();

            List<CustomCommandMacro> macrosForPlayer = new ArrayList<>();

            while (rs.next())
                macrosForPlayer.add(new CustomCommandMacro(rs.getString("UUID"), rs.getString("MacroName"), rs.getString("Command")));

            cachedMacros.put(player, macrosForPlayer);

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static void cacheAllPlayers() {
        cachedMacros.clear();
        for (Player player : Bukkit.getOnlinePlayers()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    cachePlayer(player);
                }
            }.runTaskAsynchronously(AlttdUtility.getInstance());
        }
    }

    public static List<String> getMacroNamesForPlayer(Player player) {

        List<String> results = new ArrayList<>();

        if (!cachedMacros.containsKey(player))
            return results;

        for (CustomCommandMacro customCommandMacro : cachedMacros.get(player)) {
            results.add(customCommandMacro.getMacroName());
        }

        return results;

    }

    public static CustomCommandMacro getMacroForPlayer(Player player, String macroName) {

        if (!getMacroNamesForPlayer(player).contains(macroName))
            return null;

        for (CustomCommandMacro customCommandMacro : cachedMacros.get(player)) {
            if (customCommandMacro.getMacroName().equals(macroName)) {
                return customCommandMacro;
            }
        }

        return null;

    }

}
