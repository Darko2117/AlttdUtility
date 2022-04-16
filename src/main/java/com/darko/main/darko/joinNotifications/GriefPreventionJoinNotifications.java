package com.darko.main.darko.joinNotifications;

import com.darko.main.common.API.APIs;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class GriefPreventionJoinNotifications implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        if (!player.hasPermission("utility.canseejoinnotifications")) return;

        GriefPrevention griefPreventionAPI = APIs.getGriefPreventionAPI();

        String message = "";

        if (griefPreventionAPI.dataStore.getPlayerData(player.getUniqueId()).ignoreClaims) {
            message = message.concat(ChatColor.GOLD + "Ignore claims " + ChatColor.GREEN + "ON\n");
        }

        if (!message.isEmpty()) {
            player.sendMessage(message);
        }

    }

}
