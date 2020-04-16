package com.darko.main.utilities.cooldown;

import com.darko.main.Main;
import com.darko.main.utilities.other.APIs;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.ZoneId;

public class Cooldown implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        Boolean crate = false;
        Boolean rtp = false;
        if (player.hasPermission("utility.cooldown")) {
            if (args.length == 1) {
                if (args[0].equals("crate")) {
                    crate = crate(player);
                } else if (args[0].equals("rtp")) {
                    rtp = rtp(player);
                }
            } else {
                crate = crate(player);
                rtp = rtp(player);
            }
            if (!crate && !rtp) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        Main.getInstance().getConfig().getString("Messages.NoCooldowns")));
            }
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    Main.getInstance().getConfig().getString("Messages.NoPermission")));
        }
        return false;

    }

    public static Boolean crate(Player player) {
        LuckPerms api = APIs.LuckPermsApiCheck();
        User user = api.getUserManager().getUser(player.getUniqueId());
        Integer time = 0;
        for (Node node : user.getNodes()) {
            if (node.getKey().equals("keyshop.buy")) {
                Long time1 = node.getExpiry().getEpochSecond() - System.currentTimeMillis() / 1000l;
                time = time1.intValue();
            }
        }
        Integer hours = 0;
        Integer minutes = 0;
        Integer seconds = time;

        while (seconds >= 60) {
            seconds -= 60;
            minutes++;
        }
        while (minutes >= 60) {
            minutes -= 60;
            hours++;
        }
        if (hours != 0 && minutes != 0) {
            player.sendMessage(ChatColor.GREEN + "Cooldown on the SuperCrate is " + hours + " hours " + minutes
                    + " minutes " + seconds + " seconds.");
            return true;
        } else if (hours == 0 && minutes != 0) {
            player.sendMessage(
                    ChatColor.GREEN + "Cooldown on the SuperCrate is " + minutes + " minutes " + seconds + " seconds.");
            return true;
        } else if (hours == 0 && minutes == 0 && seconds > 0) {
            player.sendMessage(ChatColor.GREEN + "Cooldown on the SuperCrate is " + seconds + " seconds.");
            return true;
        }
        return false;
    }

    public static Boolean rtp(Player player) {
        LuckPerms api = APIs.LuckPermsApiCheck();
        User user = api.getUserManager().getUser(player.getUniqueId());
        Integer time = 0;
        for (Node node : user.getNodes()) {
            if (node.getKey().equals("rtp.no")) {
                Long time1 = node.getExpiry().getEpochSecond() - System.currentTimeMillis() / 1000l;
                time = time1.intValue();
            }
        }
        Integer hours = 0;
        Integer minutes = 0;
        Integer seconds = time;

        while (seconds >= 60) {
            seconds -= 60;
            minutes++;
        }
        while (minutes >= 60) {
            minutes -= 60;
            hours++;
        }
        if (hours != 0 && minutes != 0) {
            player.sendMessage(ChatColor.GREEN + "Cooldown on the RTP is " + hours + " hours " + minutes + " minutes "
                    + seconds + " seconds.");
            return true;
        } else if (hours == 0 && minutes != 0) {
            player.sendMessage(
                    ChatColor.GREEN + "Cooldown on the RTP is " + minutes + " minutes " + seconds + " seconds.");
            return true;
        } else if (hours == 0 && minutes == 0 && seconds > 0) {
            player.sendMessage(ChatColor.GREEN + "Cooldown on the RTP is " + seconds + " seconds.");
            return true;
        }
        return false;
    }

}
