package com.darko.main.utilities.freeze;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class onPlayerMove implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (e.getPlayer().hasPermission("utility.freeze") && !e.getPlayer().hasPermission("utility.freeze1")) {
            e.setCancelled(true);
        }
    }
}
