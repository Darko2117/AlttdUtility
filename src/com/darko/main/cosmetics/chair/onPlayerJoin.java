package com.darko.main.cosmetics.chair;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class onPlayerJoin implements Listener {

    @EventHandler
    public static void onJoin(PlayerJoinEvent e) {
        Player player = (Player) e.getPlayer();
        if (!GlobalVariables.chairEnabled.containsKey(player.getUniqueId())) {
            GlobalVariables.chairEnabled.put(player.getUniqueId(), false);
        }

    }

}
