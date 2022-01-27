package com.darko.main.darko.joinNotifications;

import com.Zrips.CMI.CMI;
import com.darko.main.common.API.APIs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class CMIJoinNotifications implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        if (!player.hasPermission("utility.canseejoinnotifications")) return;

        CMI cmiAPI = APIs.CMIApiCheck();

        String message = "";

        if (cmiAPI.getPlayerManager().getUser(player).isGod()) {
            message = message.concat(ChatColor.GOLD + "God mode " + ChatColor.GREEN + "ON\n");
        }
        if (cmiAPI.getPlayerManager().getUser(player).isAllowFlight()) {
            message = message.concat(ChatColor.GOLD + "Fly " + ChatColor.GREEN + "ON");
        }

        if (!message.isEmpty()) {
            player.sendMessage(message);
        }

    }

}
