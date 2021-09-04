package com.darko.main.darko.temporaryArmorStandRemover;

import com.darko.main.AlttdUtility;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TemporaryArmorStandRemover implements Listener {

    List<Location> savedLocations = new ArrayList<>();

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {

                if (!event.getChunk().isLoaded()) return;

                for (Entity entity : Arrays.asList(event.getChunk().getEntities())) {
                    if (!(entity instanceof ArmorStand)) continue;
                    if (!((ArmorStand) entity).hasBasePlate()) continue;
                    if (((ArmorStand) entity).isSmall()) continue;
                    if (((ArmorStand) entity).isVisible()) continue;
                    if (((ArmorStand) entity).isSmall()) continue;
                    if (((ArmorStand) entity).hasGravity()) continue;
                    if (((ArmorStand) entity).hasArms()) continue;
                    if (((ArmorStand) entity).isCustomNameVisible()) continue;
                    if (((ArmorStand) entity).isGlowing()) continue;
                    if (!((ArmorStand) entity).isInvulnerable()) continue;

                    Location location = entity.getLocation();

                    if (savedLocations.contains(location)) return;
                    savedLocations.add(location);

                    AlttdUtility.getInstance().getLogger().info("A potential ArmorStand to be removed: " + entity.getLocation());
                }

            }
        }.runTaskLater(AlttdUtility.getInstance(), 100);
    }

}