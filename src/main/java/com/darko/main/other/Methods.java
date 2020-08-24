package com.darko.main.other;

import com.darko.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Methods {

    public static void sendConfigMessage(CommandSender receiver, String path) {

        receiver.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString(path)));

    }

}
