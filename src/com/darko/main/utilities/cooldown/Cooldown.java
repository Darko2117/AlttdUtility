package com.darko.main.utilities.cooldown;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.darko.main.utilities.other.APIs;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;

public class Cooldown implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        Boolean crate = false;
        Boolean rtp = false;
        if (player.hasPermission("utility.cooldown")) {
            if (args.length == 1) {
                if (args[0].toString().equals("crate")) {
                    crate = crate(player);
                } else if (args[0].toString().equals("rtp")) {
                    rtp = rtp(player);
                }
            } else {
                crate = crate(player);
                rtp = rtp(player);
            }
            if (!crate && !rtp) {
                player.sendMessage(ChatColor.YELLOW + "You have no cooldowns right now.");
            }
        } else {
            player.sendMessage(ChatColor.RED + "You do not have permission to do this.");
        }
        return false;

    }

    public static Boolean crate(Player player) {
        LuckPerms api = APIs.LuckPermsApi();
        User user = api.getUserManager().getUser(player.getUniqueId());
        Integer time = 0;
        for (Node node : user.getNodes()) {
            if (node.getKey().equals("keyshop.buy")) {
                Long time1 = node.getExpiryDuration().getSeconds();
                time = time1.intValue();
            }
        }
        Integer hours = 0;
        Integer minutes = 0;
        Integer seconds = time;

        while (seconds > 60) {
            seconds -= 60;
            minutes++;
        }
        while (minutes > 60) {
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
        LuckPerms api = APIs.LuckPermsApi();
        User user = api.getUserManager().getUser(player.getUniqueId());
        Integer time = 0;
        for (Node node : user.getNodes()) {
            if (node.getKey().equals("rtp.no")) {
                Long time1 = node.getExpiryDuration().getSeconds();
                time = time1.intValue();
            }
        }
        Integer hours = 0;
        Integer minutes = 0;
        Integer seconds = time;

        while (seconds > 60) {
            seconds -= 60;
            minutes++;
        }
        while (minutes > 60) {
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
