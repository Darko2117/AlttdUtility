package com.darko.main.darko.atPlayers;

import com.darko.main.common.API.APIs;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import com.gmail.nossr50.api.ChatAPI;

public class NameInChatNotification implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {

        StringBuilder message = new StringBuilder(event.getMessage());

        Boolean adminChatEnabled = false;
        Boolean partyChatEnabled = false;

        if (APIs.isMCMMOFound()) {
            try {
                adminChatEnabled = ChatAPI.isUsingAdminChat(event.getPlayer());
                partyChatEnabled = ChatAPI.isUsingPartyChat(event.getPlayer());
            } catch (Throwable ignored) {
            }
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!adminChatEnabled && !partyChatEnabled) {
                if (message.indexOf(p.getName()) != -1 && message.indexOf("[i]") == -1) {
                    message.insert(message.indexOf(p.getName()), ChatColor.AQUA + "@" + ChatColor.RESET);
                    if (p.hasPermission("utility.hear-name-ping")) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                    }
                }
            }
        }

        event.setMessage(message.toString());

    }

}
