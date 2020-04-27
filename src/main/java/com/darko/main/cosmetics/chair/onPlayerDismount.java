package com.darko.main.cosmetics.chair;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.entity.EntityDismountEvent;

import com.darko.main.Main;

public class onPlayerDismount implements Listener {

    @EventHandler
    public void onDismount(EntityDismountEvent e) {

        Entity ent = e.getDismounted();
        if (ent.getCustomName() != null && ent.getCustomName().equals(GlobalVariables.ChairName)) {

            ent.remove();

            GlobalVariables.aliveSeats.remove(e.getEntity().getLocation());
            GlobalVariables.occupiedSeats.remove(e.getEntity().getUniqueId());

            new BukkitRunnable() {
                public void run() {
                    if (e.getEntity().getVehicle() == null) {
                        e.getEntity().teleport(e.getEntity().getLocation().add(0, 1, 0));
                    }
                }
            }.runTaskLater(Main.getInstance(), 0);

        }
    }
}
