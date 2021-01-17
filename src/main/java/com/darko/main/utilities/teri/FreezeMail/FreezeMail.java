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

public class FreezeMail implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length >= 2) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
            if (offlinePlayer.hasPlayedBefore()){
                String message = StringUtils.join(args, " ").substring(args[0].length());
                new BukkitRunnable(){

                    @Override
                    public void run() {
                        storeMessage(offlinePlayer, message, sender);
                    }
                }.runTaskAsynchronously(Main.getInstance());
            } else {
                String messageToSend = ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Messages.FreezeMailPlayerDoesntExist"));
                sender.sendMessage(messageToSend.replace("%target%", args[0]));
            }

        } else {
            String messageToSend = ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Messages.FreezeMailSpecifyPlayer"));
            sender.sendMessage(messageToSend.replace("%target%", args[0]));
        }

        return true;
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
