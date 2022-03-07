package com.darko.main.darko.invsaveOnPlayerQuit;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class InvsaveOnPlayerQuit implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cmi invsave " + event.getPlayer().getName());

    }

}
