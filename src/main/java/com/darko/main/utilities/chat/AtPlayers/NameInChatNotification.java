package com.darko.main.utilities.chat.AtPlayers;

import com.darko.main.API.APIs;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import com.gmail.nossr50.api.ChatAPI;

public class NameInChatNotification implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

        StringBuilder message = new StringBuilder(e.getMessage());

        Boolean adminChatEnabled = false;
        Boolean partyCheatEnabled = false;

        if(APIs.mcMMOFound) {
            adminChatEnabled = ChatAPI.isUsingAdminChat(e.getPlayer());
            partyCheatEnabled = ChatAPI.isUsingPartyChat(e.getPlayer());
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!adminChatEnabled && !partyCheatEnabled) {
                if (message.indexOf(p.getName()) != -1 && message.indexOf("[i]") == -1) {
                    message.insert(message.indexOf(p.getName()), ChatColor.AQUA + "@" + ChatColor.RESET);
                    if (p.hasPermission("utility.hear-name-ping")) {
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                    }
                }
            }
        }

        e.setMessage(message.toString());

    }
}
