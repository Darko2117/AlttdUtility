package com.darko.main.utilities.teri.FreezeMail;

import com.darko.main.Main;
import com.darko.main.database.Database;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class FreezeMail implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 0){
            //TODO freezemail help menu
            return true;
        }

        OfflinePlayer offlinePlayer;
        switch (args[0].toLowerCase()){
            case "list":
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        handleList(sender, args);
                    }
                }.runTaskAsynchronously(Main.getInstance());
                break;
            case "send":
                    if (sender.hasPermission("utility.freezemail.send")){
                        if (args.length >= 3){
                            offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                            if (exists(offlinePlayer, sender, args[1])){
                                String message = StringUtils.join(args, " ").substring(args[1].length() + args[2].length() + 3);
                                new BukkitRunnable(){
                                    @Override
                                    public void run() {
                                        storeMessage(offlinePlayer, message, sender);
                                    }
                                }.runTaskAsynchronously(Main.getInstance());
                            }
                        } else {
                            String messageToSend = ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Messages.FreezeMailSpecifyPlayer"));
                            sender.sendMessage(messageToSend.replace("%target%", args[0]));
                        }
                    } else {
                        noPermission(sender);
                    }
                break;
            default:
                //TODO send proper command usage
        }
        return true;
    }

    private void handleList(CommandSender sender, String[] args) {
        OfflinePlayer offlinePlayer;
        if (sender.hasPermission("utility.freezemail.list")){
            if (args.length > 1){
                if (args[1].equalsIgnoreCase("all")){
                    ArrayList<String> messagesForPlayerRead = getMessagesForPlayer(sender.getServer().getPlayer(sender.getName()).getUniqueId().toString(), true);
                    ArrayList<String> messagesForPlayerUnread = getMessagesForPlayer(sender.getServer().getPlayer(sender.getName()).getUniqueId().toString(), false);
                    sendPlayersFreezeMail(sender, messagesForPlayerRead, messagesForPlayerUnread, sender.getName());
                }
                if (sender.hasPermission("utility.freezemail.list.other")) {
                    offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                    if (exists(offlinePlayer, sender, args[1])) {
                        if (args.length == 2) {
                            ArrayList<String> messagesForPlayer = getMessagesForPlayer(offlinePlayer.getUniqueId().toString(), false);
                            sendPlayersFreezeMail(sender, messagesForPlayer, offlinePlayer.getName());
                        } else if (args.length == 3 && args[2].equalsIgnoreCase("all")) {
                            ArrayList<String> messagesForPlayerRead = getMessagesForPlayer(offlinePlayer.getUniqueId().toString(), true);
                            ArrayList<String> messagesForPlayerUnread = getMessagesForPlayer(offlinePlayer.getUniqueId().toString(), false);
                            sendPlayersFreezeMail(sender, messagesForPlayerRead, messagesForPlayerUnread, offlinePlayer.getName());
                        }
                    } else {
                        //TODO send proper command usage
                    }
                } else {
                    noPermission(sender);
                }
            } else {
                ArrayList<String> messagesForPlayer = getMessagesForPlayer(sender.getServer().getPlayer(sender.getName()).getUniqueId().toString(), false);
                sendPlayersFreezeMail(sender, messagesForPlayer, sender.getName());
            }
        } else {
            noPermission(sender);
        }
    }

    private void sendPlayersFreezeMail(CommandSender sender, ArrayList<String> messagesForPlayerRead, String playerName) {
        StringBuilder message = new StringBuilder();
        message.append(Main.getInstance().getConfig().getString("Messages.FreezeMailListRead"));

        int i = 1;
        for (String s : messagesForPlayerRead){
            message.append("\n&6").append(i).append(":&7 ").append(s);
            i++;
        }

        String finalMessage = ChatColor.translateAlternateColorCodes('&' , message.toString());
        sender.sendMessage(finalMessage.replace("%player%", playerName));
    }

    private void sendPlayersFreezeMail(CommandSender sender, ArrayList<String> messagesForPlayerRead, ArrayList<String> messagesForPlayerUnread, String playerName) {
        if (messagesForPlayerRead.isEmpty() && messagesForPlayerUnread.isEmpty()){
            sender.sendMessage("&cNo mails found for " + playerName + ".");
            return;
        }

        StringBuilder message = new StringBuilder();
        message.append(Main.getInstance().getConfig().getString("Messages.FreezeMailListAll"));

        if (!messagesForPlayerRead.isEmpty()) {
            message.append("\n&fRead:");
            int i = 1;
            for (String s : messagesForPlayerRead) {
                message.append("\n&6").append(i).append(":&7 ").append(s);
                i++;
            }
        }
        if (!messagesForPlayerUnread.isEmpty()) {
            message.append("\n&fUnread:");
            int i = 1;
            for (String s : messagesForPlayerUnread) {
                message.append("\n&6").append(i).append(":&7 ").append(s);
                i++;
            }
        }

        String finalMessage = ChatColor.translateAlternateColorCodes('&' , message.toString());
        sender.sendMessage(finalMessage.replace("%player%", playerName));
    }

    private ArrayList<String> getMessagesForPlayer(String uuid, boolean read) {
        ArrayList<String> messages = new ArrayList<>();

        String query = "SELECT Message FROM freeze_message WHERE UUID = ? AND IsRead = ?";

        try {
            PreparedStatement preparedStatement = Database.connection.prepareStatement(query);
            preparedStatement.setString(1, uuid);
            preparedStatement.setInt(2, read ? 1 : 0);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                messages.add(resultSet.getString("Message"));
            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return messages;
    }

    private boolean exists(OfflinePlayer offlinePlayer, CommandSender sender, String targetName) {
        if (offlinePlayer.hasPlayedBefore()){
            return true;
        } else {
            String messageToSend = ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Messages.FreezeMailPlayerDoesntExist"));
            sender.sendMessage(messageToSend.replace("%target%", targetName));
            return false;
        }
    }

    private void noPermission(CommandSender sender) {
        sender.sendMessage("&cYou don't have permission for this command");
    }

    private void storeMessage(OfflinePlayer player, String message, CommandSender sender){

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

        String messageToSend = ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Messages.FreezeMailSuccessfullySend"));
        sender.sendMessage(messageToSend.replace("%player%", player.getName()));
    }
}
