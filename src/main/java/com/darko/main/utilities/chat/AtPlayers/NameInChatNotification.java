package com.darko.main.utilities.chat.AtPlayers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class NameInChatNotification implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent e) {

        StringBuilder message = new StringBuilder(e.getMessage());

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (message.indexOf(p.getName()) != -1 && message.indexOf("[i]") == -1 && message.indexOf("->") == -1){
                message.insert(message.indexOf(p.getName()), ChatColor.AQUA + "@" + ChatColor.RESET);
                if (p.hasPermission("utility.hear-name-ping")) {
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                }
            }
        }

        e.setMessage(message.toString());

    }
}