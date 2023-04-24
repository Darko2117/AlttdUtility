package com.darko.main.darko.deathMessage;

import com.darko.main.AlttdUtility;
import com.darko.main.common.Methods;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class DeathMessage implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent event) {

        if (event.getEntity().hasPermission("utility.deathmsg")) {

            new BukkitRunnable() {
                @Override
                public void run() {
                    Methods.sendConfigMessage(event.getEntity(), "Messages.DeathMessage");
                }
            }.runTaskLater(AlttdUtility.getInstance(), 40);
        }

    }
}
