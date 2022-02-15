package com.darko.main.darko.spawnLimiter;

import com.darko.main.AlttdUtility;
import com.darko.main.darko.logging.listeners.LoggingNoAPI;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class SpawnLimiterCheck implements Listener {

    private static final List<SpawnLimiter> spawnLimiters = new ArrayList<>();

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEntitySpawn(EntitySpawnEvent event) {

        SpawnLimiter spawnLimiter = null;
        for (SpawnLimiter spawnLimiter1 : spawnLimiters) {
            if (spawnLimiter1.getEntityType().equals(event.getEntityType())) {
                spawnLimiter = spawnLimiter1;
                break;
            }
        }
        if (spawnLimiter == null) return;

        Location location = event.getLocation();

        int alreadySpawned = 0;

        for (Location loc : spawnLimiter.getSpawnLocations()) {
            if (!location.getWorld().equals(loc.getWorld())) continue;
            if (location.distance(loc) <= spawnLimiter.getRadiusLimit()) {
                alreadySpawned++;
            }
        }

        if (alreadySpawned >= spawnLimiter.getSpawnLimit()) {
            event.setCancelled(true);
            LoggingNoAPI.logCancelledSpawn(event);
            return;
        }

        spawnLimiter.getSpawnLocations().add(location);

        SpawnLimiter finalSpawnLimiter = spawnLimiter;
        new BukkitRunnable() {
            @Override
            public void run() {

                finalSpawnLimiter.getSpawnLocations().remove(location);

            }
        }.runTaskLater(AlttdUtility.getInstance(), spawnLimiter.getTimeLimit() * 20L);

    }

    public static void reload() {

        spawnLimiters.clear();

        for (String entityTypeString : AlttdUtility.getInstance().getConfig().getKeys(true)) {

            if (!entityTypeString.startsWith("SpawnLimiter.")) continue;

            entityTypeString = entityTypeString.substring(13);

            if (entityTypeString.contains(".")) continue;
            if (entityTypeString.isEmpty()) continue;

            EntityType entityType = EntityType.valueOf(entityTypeString);
            int radiusLimit = AlttdUtility.getInstance().getConfig().getInt("SpawnLimiter." + entityTypeString + ".RadiusLimit");
            int timeLimit = AlttdUtility.getInstance().getConfig().getInt("SpawnLimiter." + entityTypeString + ".TimeLimit");
            int spawnLimit = AlttdUtility.getInstance().getConfig().getInt("SpawnLimiter." + entityTypeString + ".SpawnLimit");
            List<Location> locations = new ArrayList<>();

            spawnLimiters.add(new SpawnLimiter(entityType, radiusLimit, timeLimit, spawnLimit, locations));

        }

    }

}
