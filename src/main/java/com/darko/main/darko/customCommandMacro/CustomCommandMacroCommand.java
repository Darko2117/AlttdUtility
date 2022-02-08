package com.darko.main.darko.customCommandMacro;

import com.darko.main.AlttdUtility;
import com.darko.main.common.database.Database;
import com.darko.main.common.Methods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomCommandMacroCommand implements CommandExecutor, TabCompleter {

    private static final HashMap<Player, List<CustomCommandMacro>> macros = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        new BukkitRunnable() {
            @Override
            public void run() {

                if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.CustomCommandMacroCommand"))
                    return;

                if (!(sender instanceof Player player)) {
                    new Methods().sendConfigMessage(sender, "Messages.PlayerOnlyCommandMessage");
                    return;
                }

                String UUID = player.getUniqueId().toString();
                String username = player.getName();

                if (args.length == 0) {
                    new Methods().sendConfigMessage(sender, "Messages.CustomCommandMacroUsage");
                    return;
                }

                if (args[0].equals("add")) {

                    if (args.length < 3) {
                        new Methods().sendConfigMessage(sender, "Messages.CustomCommandMacroUsage");
                        return;
                    }

                    String macroName = args[1].toLowerCase();
                    String commandString = "";

                    for (int i = 2; i < args.length; i++) {
                        commandString = commandString.concat(args[i]);
                        if (i < args.length - 1) commandString = commandString.concat(" ");
                    }
                    if (!commandString.startsWith("/")) commandString = "/" + commandString;

                    if (getMacroNamesForPlayer(player).contains(macroName)) {
                        new Methods().sendConfigMessage(sender, "Messages.CustomCommandMacroAlreadyExists");
                        return;
                    }

                    String statement = "INSERT INTO custom_command_macro(UUID, Username, MacroName, Command) VALUES("
                            + "'" + UUID + "', "
                            + "'" + username + "', "
                            + "'" + macroName + "', "
                            + "'" + commandString + "');";

                    try {

                        Database.connection.prepareStatement(statement).executeUpdate();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', AlttdUtility.getInstance().getConfig()
                                .getString("Messages.CustomCommandMacroSavedMessage").replace("%macroName%", macroName).replace("%command%", commandString)));

                        loadMacrosForPlayer(player);

                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }

                } else if (args[0].equals("remove")) {

                    if (args.length < 2) {
                        new Methods().sendConfigMessage(sender, "Messages.CustomCommandMacroUsage");
                        return;
                    }

                    String macroName = args[1].toLowerCase();

                    if (!getMacroNamesForPlayer(player).contains(macroName)) {
                        new Methods().sendConfigMessage(sender, "Messages.CustomCommandMacroDoesntExist");
                        return;
                    }

                    String statement = "DELETE FROM custom_command_macro WHERE MacroName = '" + macroName + "' AND UUID = '" + UUID + "';";

                    try {

                        Database.connection.prepareStatement(statement).executeUpdate();
                        new Methods().sendConfigMessage(sender, "Messages.CustomCommandMacroRemovedMessage");

                        loadMacrosForPlayer(player);

                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }

                } else if (args[0].equals("edit")) {

                    if (args.length < 3) {
                        new Methods().sendConfigMessage(sender, "Messages.CustomCommandMacroUsage");
                        return;
                    }

                    String macroName = args[1].toLowerCase();
                    String commandString = "";

                    if (!getMacroNamesForPlayer(player).contains(macroName)) {
                        new Methods().sendConfigMessage(sender, "Messages.CustomCommandMacroDoesntExist");
                        return;
                    }

                    for (Integer i = 2; i < args.length; i++) {
                        commandString = commandString.concat(args[i]);
                        if (i < args.length - 1) commandString = commandString.concat(" ");
                    }
                    if (!commandString.startsWith("/")) commandString = "/" + commandString;

                    String statement = "UPDATE custom_command_macro SET Command = '" + commandString + "' WHERE MacroName = '" + macroName + "' AND UUID = '" + UUID + "';";

                    try {

                        Database.connection.prepareStatement(statement).executeUpdate();
                        new Methods().sendConfigMessage(sender, "Messages.CustomCommandMacroEdited");

                        loadMacrosForPlayer(player);

                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }

                } else if (getMacroNamesForPlayer(player).contains(args[0].toLowerCase())) {

                    String macroName = args[0].toLowerCase();
                    String commandString;

                    String statement = "SELECT * FROM custom_command_macro WHERE MacroName = '" + macroName + "' AND UUID = '" + UUID + "';";

                    try {

                        ResultSet rs = Database.connection.prepareStatement(statement).executeQuery();
                        rs.next();
                        commandString = rs.getString("Command");

                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                        return;
                    }

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.chat(commandString);
                        }
                    }.runTask(AlttdUtility.getInstance());

                } else {

                    new Methods().sendConfigMessage(sender, "Messages.CustomCommandMacroDoesntExist");

                }

            }
        }.runTaskAsynchronously(AlttdUtility.getInstance());

        return true;

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.CustomCommandMacroCommand"))
            return null;

        if (!(sender instanceof Player player)) return null;

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

            return getMacroNamesForPlayer(player);

        } else if (args.length == 2 && args[0].equals("edit")) {

            return getMacroNamesForPlayer(player);

        }

        return null;

    }

    public static void loadMacros() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            loadMacrosForPlayer(player);
        }
    }

    private static void loadMacrosForPlayer(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {

                macros.remove(player);

                String statement = "SELECT * FROM custom_command_macro WHERE UUID = '" + player.getUniqueId() + "'";

                try {

                    ResultSet rs = Database.connection.prepareStatement(statement).executeQuery();

                    List<CustomCommandMacro> macrosForPlayer = new ArrayList<>();

                    while (rs.next())
                        macrosForPlayer.add(new CustomCommandMacro(rs.getString("UUID"), rs.getString("MacroName"), rs.getString("Command")));

                    macros.put(player, macrosForPlayer);

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

            }
        }.runTaskAsynchronously(AlttdUtility.getInstance());
    }

    private List<String> getMacroNamesForPlayer(Player player) {

        List<String> results = new ArrayList<>();

        for (CustomCommandMacro customCommandMacro : macros.get(player)) {
            results.add(customCommandMacro.getMacroName());
        }

        return results;

    }

}