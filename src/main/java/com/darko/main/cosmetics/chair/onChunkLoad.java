package com.darko.main.cosmetics.chair;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.darko.main.Main;

public class onChunkLoad implements Listener {

    @EventHandler
    public void onChunkLoadEvent(ChunkLoadEvent e) {

        Entity[] ent = e.getChunk().getEntities();
        for (Integer i = 0; i < ent.length; i++) {
            if (ent[i].getCustomName() != null) {
                if (ent[i].getCustomName().equals(GlobalVariables.ChairName)) {
                    ent[i].remove();
                    GlobalVariables.occupiedSeats.remove(ent[i].getUniqueId());
                    GlobalVariables.aliveSeats.remove(ent[i].getLocation());
                    Main.getInstance().getLogger().info(ent[i].getType() + " deleted at " + ent[i].getLocation() + " with onChunkLoad");
                }
            }
        }

    }
}
