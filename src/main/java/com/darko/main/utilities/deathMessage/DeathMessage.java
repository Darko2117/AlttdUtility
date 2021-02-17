package com.darko.main.utilities.deathMessage;

import com.darko.main.other.Methods;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathMessage implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent event) {

        if (event.getEntity().hasPermission("utility.deathmsg")) {
            new Methods().sendConfigMessage(event.getEntity(), "Messages.DeathMessage");
        }

    }
}
