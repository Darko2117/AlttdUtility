package com.darko.main.teri.FreezeMail;

import com.darko.main.AlttdUtility;
import com.darko.main.database.Database;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class FreezeMail implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.FreezeMail")) return true;

        if (args.length == 0) {
            sendHelpMessage(sender, HelpType.ALL);
            return true;
        }

        OfflinePlayer offlinePlayer;
        switch (args[0].toLowerCase()) {
            case "list":
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        handleList(sender, args);
                    }
                }.runTaskAsynchronously(AlttdUtility.getInstance());
                break;
            case "send":
                if (sender.hasPermission("utility.freezemail.send")) {
                    if (args.length >= 3) {
                        offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                        if (exists(offlinePlayer, sender, args[1])) {
                            String message = StringUtils.join(args, " ").substring(args[0].length() + args[1].length() + 2);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    storeMessage(offlinePlayer, message, sender);
                                }
                            }.runTaskAsynchronously(AlttdUtility.getInstance());
                        }
                    } else {
                        sendHelpMessage(sender, HelpType.SEND);
                    }
                } else {
                    noPermission(sender);
                }
                break;
            default:
                sendHelpMessage(sender, HelpType.ALL);
        }
        return true;
    }

    private void handleList(CommandSender sender, String[] args) {

        if (sender.hasPermission("utility.freezemail.list")) {
            if (args.length > 1) {
                if (args[1].equalsIgnoreCase("all")) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage("&cThis command can only be executed as a player.");
                        return;
                    }

                    ArrayList<String> messagesForPlayerRead = getMessagesForPlayer(
                            sender.getServer().getPlayer(sender.getName()).getUniqueId().toString(), true);
                    ArrayList<String> messagesForPlayerUnread = getMessagesForPlayer(
                            sender.getServer().getPlayer(sender.getName()).getUniqueId().toString(), false);

                    sendPlayersFreezeMail(sender, messagesForPlayerRead, messagesForPlayerUnread, sender.getName());
                } else if (args[1].equalsIgnoreCase("unread")) {
                    if (sender.hasPermission("utility.freezemail.list.unread")) {
                        HashMap<String, ArrayList<String>> allUnreadMessages = getAllUnreadMessages();

                        sendAllUnreadFreezeMail(sender, allUnreadMessages);
                    } else {
                        noPermission(sender);
                    }
                } else if (sender.hasPermission("utility.freezemail.list.other")) {

                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);

                    if (exists(offlinePlayer, sender, args[1])) {
                        if (args.length == 2) {

                            ArrayList<String> messagesForPlayer = getMessagesForPlayer(
                                    offlinePlayer.getUniqueId().toString(), false);

                            sendPlayersFreezeMail(sender, messagesForPlayer, offlinePlayer.getName());
                        } else if (args.length == 3 && args[2].equalsIgnoreCase("all")) {

                            ArrayList<String> messagesForPlayerRead = getMessagesForPlayer(
                                    offlinePlayer.getUniqueId().toString(), true);
                            ArrayList<String> messagesForPlayerUnread = getMessagesForPlayer(
                                    offlinePlayer.getUniqueId().toString(), false);

                            sendPlayersFreezeMail(sender, messagesForPlayerRead, messagesForPlayerUnread, offlinePlayer.getName());
                        }
                    } else {
                        sendHelpMessage(sender, HelpType.LIST_OTHERS);
                    }
                } else {
                    noPermission(sender);
                }
            } else {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("&cThis command can only be executed as a player.");
                    return;
                }
                ArrayList<String> messagesForPlayer = getMessagesForPlayer(sender.getServer().getPlayer(sender.getName()).getUniqueId().toString(), false);

                sendPlayersFreezeMail(sender, messagesForPlayer, sender.getName());
            }
        } else {
            noPermission(sender);
        }
    }

    private void sendAllUnreadFreezeMail(CommandSender sender, HashMap<String, ArrayList<String>> allUnreadMessages) {
        String message = AlttdUtility.getInstance().getConfig().getString("Messages.FreezeMailListAllUnread") +
                buildMessageFromMails(allUnreadMessages);

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    private void sendPlayersFreezeMail(CommandSender sender, ArrayList<String> messagesForPlayerRead, String playerName) {
        String message = AlttdUtility.getInstance().getConfig().getString("Messages.FreezeMailListRead") +
                buildMessageFromMails(messagesForPlayerRead);

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message)
                .replace("%player%", playerName));
    }

    private void sendPlayersFreezeMail(CommandSender sender, ArrayList<String> messagesForPlayerRead, ArrayList<String> messagesForPlayerUnread, String playerName) {
        if (messagesForPlayerRead.isEmpty() && messagesForPlayerUnread.isEmpty()) {
            sender.sendMessage("&cNo mails found for " + playerName + ".");
            return;
        }

        StringBuilder message = new StringBuilder();
        message.append(AlttdUtility.getInstance().getConfig().getString("Messages.FreezeMailListAll"));

        if (!messagesForPlayerRead.isEmpty()) {
            message.append("\n&fRead:");
            message.append(buildMessageFromMails(messagesForPlayerRead));
        }
        if (!messagesForPlayerUnread.isEmpty()) {
            message.append("\n&fUnread:");
            message.append(buildMessageFromMails(messagesForPlayerUnread));
        }

        String finalMessage = ChatColor.translateAlternateColorCodes('&', message.toString());
        sender.sendMessage(finalMessage.replace("%player%", playerName));
    }

    private String buildMessageFromMails(ArrayList<String> messages) {
        StringBuilder stringBuilder = new StringBuilder();
        int i = 1;
        for (String s : messages) {
            stringBuilder.append("\n&6").append(i).append(":&7 ").append(s);
            i++;
        }
        return stringBuilder.toString();
    }

    private String buildMessageFromMails(HashMap<String, ArrayList<String>> messages) {
        StringBuilder stringBuilder = new StringBuilder();
        int i = 1;
        for (String username : messages.keySet()) {
            for (String s : messages.get(username)) {
                stringBuilder.append("\n&6").append(i).append(" &d(").append(username).append(")&6:&7 ").append(s);
                i++;
            }
        }
        return stringBuilder.toString();
    }

    private HashMap<String, ArrayList<String>> getAllUnreadMessages() {
        HashMap<String, ArrayList<String>> messages = new HashMap<>();
        HashMap<String, String> users = new HashMap<>();

        String query = "SELECT UUID,Message FROM freeze_message WHERE IsRead = 0";

        try {
            PreparedStatement preparedStatement = Database.connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String uuid = resultSet.getString("UUID");
                if (users.containsKey(uuid)) {
                    ArrayList<String> strings = messages.get(users.get(uuid));
                    strings.add(resultSet.getString("Message"));
                } else {
                    ArrayList<String> tmp = new ArrayList<>();
                    tmp.add(resultSet.getString("Message"));

                    users.put(uuid, Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName());
                    messages.put(users.get(uuid), tmp);
                }
            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return messages;
    }

    private ArrayList<String> getMessagesForPlayer(String uuid, boolean read) {
        ArrayList<String> messages = new ArrayList<>();

        String query = "SELECT Message FROM freeze_message WHERE UUID = ? AND IsRead = ?";

        try {
            PreparedStatement preparedStatement = Database.connection.prepareStatement(query);
            preparedStatement.setString(1, uuid);
            preparedStatement.setInt(2, read ? 1 : 0);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                messages.add(resultSet.getString("Message"));
            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return messages;
    }

    private void storeMessage(OfflinePlayer player, String message, CommandSender sender) {

        String query = "INSERT INTO freeze_message (UUID, Message, IsRead) VALUES (?, ?, ?)";

        try {
            PreparedStatement preparedStatement = Database.connection.prepareStatement(query);
            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setString(2, message);
            preparedStatement.setInt(3, 0);

            preparedStatement.execute();

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        String messageToSend = ChatColor.translateAlternateColorCodes('&', AlttdUtility.getInstance().getConfig().getString("Messages.FreezeMailSuccessfullySend"));
        sender.sendMessage(messageToSend.replace("%player%", player.getName()));
    }

    private boolean exists(OfflinePlayer offlinePlayer, CommandSender sender, String targetName) {
        if (offlinePlayer.hasPlayedBefore()) {
            return true;
        } else {
            String messageToSend = ChatColor.translateAlternateColorCodes('&', AlttdUtility.getInstance().getConfig().getString("Messages.FreezeMailPlayerDoesntExist"));
            sender.sendMessage(messageToSend.replace("%target%", targetName));
            return false;
        }
    }

    private void noPermission(CommandSender sender) {
        sender.sendMessage("&cYou don't have permission for this command");
    }

    private void sendHelpMessage(CommandSender sender, HelpType type) {
        String message =
                "&f--- &bFreezemail Help &f---\n"
                        + getHelpMessage(sender, type) +
                        "&f---------------------------";
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    private String getHelpMessage(CommandSender sender, HelpType type) {
        StringBuilder message = new StringBuilder();
        switch (type) {
            case ALL:
                message.append(getHelpMessage(sender, HelpType.LIST));
                message.append(getHelpMessage(sender, HelpType.LIST_OTHERS));
                message.append(getHelpMessage(sender, HelpType.LIST_UNREAD));
                message.append(getHelpMessage(sender, HelpType.SEND));
                break;
            case LIST:
                if (sender.hasPermission("utility.freezemail.list")) {
                    message.append("&6/freezemail list&f - Shows all your unread freezemails.\n" +
                            "&6/freezemail list all&f - Shows both your read and unread freezemails.\n");
                }
                break;
            case LIST_OTHERS:
                if (sender.hasPermission("utility.freezemail.list.other")) {
                    message.append("&6/freezemail list <username>&f - Shows all unread freezemails for the specified user.\n" +
                            "&6/freezemail list <username> all&f - Shows both read and unread freezemails for the specified user.\n");
                }
                break;
            case LIST_UNREAD:
                if (sender.hasPermission("utility.freezemail.list.unread")) {
                    message.append("&6/freezemail list unread&f - Shows all unread freezemails for all users.\n");
                }
                break;
            case SEND:
                if (sender.hasPermission("utility.freezemail.send")) {
                    message.append("&6/freezemail send <username>&f - Sends a freezemail to the specified user. " +
                            "Keep in mind freezemails are only to be send to players who need to take immediate action, not just for letting them know something.\n");
                }
                break;
        }
        return message.toString();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.FreezeMail")) return null;

        if (args.length == 1) {
            List<String> choices = new ArrayList<>();
            if (sender.hasPermission("utility.freezemail.send")) {
                choices.add("send");
            }
            if (sender.hasPermission("utility.freezemail.list")) {
                choices.add("list");
            }

            List<String> completions = new ArrayList<>();
            for (String s : choices) {
                if (s.startsWith(args[0])) {
                    completions.add(s);
                }
            }

            return completions;
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("list")) {
                List<String> choices = new ArrayList<>();
                List<String> onlinePlayers = new ArrayList<>();
                Bukkit.getOnlinePlayers().forEach(a -> onlinePlayers.add(a.getName()));

                if (sender.hasPermission("utility.freezemail.list.unread")) {
                    choices.add("unread");
                }
                if (sender.hasPermission("utility.freezemail.list.other")) {
                    choices.addAll(onlinePlayers);
                }

                List<String> completions = new ArrayList<>();
                for (String s : choices) {
                    if (s.startsWith(args[1])) {
                        completions.add(s);
                    }
                }

                return completions;
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("list") && !args[1].equalsIgnoreCase("unread")) {
                List<String> choices = new ArrayList<>();
                if (sender.hasPermission("utility.freezemail.list.other")) {
                    choices.add("all");
                }

                List<String> completions = new ArrayList<>();
                for (String s : choices) {
                    if (s.startsWith(args[2])) {
                        completions.add(s);
                    }
                }

                return completions;
            }
        }

        return null;
    }

    private enum HelpType {
        ALL,
        LIST,
        LIST_OTHERS,
        LIST_UNREAD,
        SEND
    }

}
