package com.darko.main.utilities.customChatMessage;

import com.darko.main.Main;
import com.darko.main.database.Database;
import com.darko.main.other.Methods;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CustomChatMessage implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.CustomChatMessageCommand")) return true;

        if (!(sender instanceof Player)) {
            new Methods().sendConfigMessage(sender, "Messages.PlayerOnlyCommandMessage");
            return true;
        }

        Player player = (Player) sender;
        String UUID = player.getUniqueId().toString();
        String username = player.getName();

        if (args.length == 0) {
            new Methods().sendConfigMessage(sender, "Messages.CustomChatMessageUsage");
            return true;
        }

        if (args[0].equals("add")) {

            if (args.length < 3) {
                new Methods().sendConfigMessage(sender, "Messages.CustomChatMessageUsage");
                return true;
            }

            String messageName = args[1];
            String message = "";

            for (Integer i = 2; i < args.length; i++) {

                message = message.concat(args[i]);
                if (i < args.length - 1) message = message.concat(" ");

            }

            List<String> existingMessageNamesToLowercase = new ArrayList<>();
            for (String string : getExistingMessagesForUUID(UUID))
                existingMessageNamesToLowercase.add(string.toLowerCase());
            if (existingMessageNamesToLowercase.contains(messageName.toLowerCase())) {
                new Methods().sendConfigMessage(sender, "Messages.CustomChatMessageAlreadyExists");
                return true;
            }

            String statement = "INSERT INTO custom_chat_message(UUID, Username, MessageName, Message) VALUES("
                    + "'" + UUID + "', "
                    + "'" + username + "', "
                    + "'" + messageName + "', "
                    + "'" + message.replace("'", "''") + "'"
                    + ");";

            try {

                Database.connection.prepareStatement(statement).executeUpdate();
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig()
                        .getString("Messages.CustomChatMessageSavedMessage").replace("%messageName%", messageName).replace("%message%", message)));

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        } else if (args[0].equals("remove")) {

            if (args.length < 2) {
                new Methods().sendConfigMessage(sender, "Messages.CustomChatMessageUsage");
                return true;
            }

            String messageName = args[1];

            if (!getExistingMessagesForUUID(UUID).contains(messageName)) {
                new Methods().sendConfigMessage(sender, "Messages.CustomChatMessageDoesntExist");
                return true;
            }

            String statement = "DELETE FROM custom_chat_message WHERE MessageName = '" + messageName + "' AND UUID = '" + UUID + "';";

            try {

                Database.connection.prepareStatement(statement).executeUpdate();
                new Methods().sendConfigMessage(sender, "Messages.CustomChatMessageRemovedMessage");

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        } else if (args[0].equals("edit")) {

            if (args.length < 3) {
                new Methods().sendConfigMessage(sender, "Messages.CustomChatMessageUsage");
                return true;
            }

            String messageName = args[1];
            String message = "";

            if (!getExistingMessagesForUUID(UUID).contains(messageName)) {
                new Methods().sendConfigMessage(sender, "Messages.CustomChatMessageDoesntExist");
                return true;
            }

            for (Integer i = 2; i < args.length; i++) {

                message = message.concat(args[i]);
                if (i < args.length - 1) message = message.concat(" ");

            }

            String statement = "UPDATE custom_chat_message SET Message = '" + message + "' WHERE MessageName = '" + messageName + "' AND UUID = '" + UUID + "';";

            try {

                Database.connection.prepareStatement(statement).executeUpdate();
                new Methods().sendConfigMessage(sender, "Messages.CustomChatMessageEdited");

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        } else if (getExistingMessagesForUUID(UUID).contains(args[0])) {

            String messageName = args[0];
            String message;

            String statement = "SELECT * FROM custom_chat_message WHERE MessageName = '" + messageName + "' AND UUID = '" + UUID + "';";

            try {

                ResultSet rs = Database.connection.prepareStatement(statement).executeQuery();
                rs.next();
                message = rs.getString("Message");

            } catch (Throwable throwable) {
                throwable.printStackTrace();
                return true;
            }

            player.chat(ChatColor.translateAlternateColorCodes('&', message));

        } else {
            new Methods().sendConfigMessage(sender, "Messages.CustomChatMessageDoesntExist");
            return true;
        }

        return true;

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.CustomChatMessageCommand")) return null;

        if (!(sender instanceof Player)) return null;
        Player player = (Player) sender;

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

            for (String string : getExistingMessagesForUUID(player.getUniqueId().toString())) {
                if (string.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(string);
                }
            }

            return completions;

        } else if (args.length == 2 && args[0].equals("remove")) {

            return getExistingMessagesForUUID(player.getUniqueId().toString());

        } else if (args.length == 2 && args[0].equals("edit")) {

            return getExistingMessagesForUUID(player.getUniqueId().toString());

        }

        return null;

    }

    private List<String> getExistingMessagesForUUID(String UUID) {

        List<String> results = new ArrayList<>();

        String statement = "SELECT * FROM custom_chat_message WHERE UUID = '" + UUID + "'";

        try {

            ResultSet rs = Database.connection.prepareStatement(statement).executeQuery();

            while (rs.next()) results.add(rs.getString("MessageName"));

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return results;

    }

}