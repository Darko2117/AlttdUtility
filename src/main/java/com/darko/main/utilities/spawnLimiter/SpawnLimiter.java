package com.darko.main.utilities.spawnLimiter;

import com.darko.main.Main;
import com.darko.main.utilities.logging.spawnlogging.SpawnLimiterLog;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpawnLimiter implements Listener {

    static List<EntityType> limitedEntities = new ArrayList<>();
    static HashMap<EntityType, Integer> radiusLimit = new HashMap<>();
    static HashMap<EntityType, Integer> timeLimit = new HashMap<>();
    static HashMap<EntityType, Integer> spawnLimit = new HashMap<>();
    static HashMap<EntityType, List<Location>> spawnLocations = new HashMap<>();

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {

        if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.SpawnLimiter")) return;

        EntityType entityType = event.getEntityType();

        if(!limitedEntities.contains(entityType)) return;

        Location location = event.getLocation();

        Integer radiusLimit1 = radiusLimit.get(entityType);
        Integer timeLimit1 = timeLimit.get(entityType);
        Integer spawnLimit1 = spawnLimit.get(entityType);

        Integer alreadySpawned = 0;

        for (Location loc : spawnLocations.get(entityType)) {
            if (!location.getWorld().equals(loc.getWorld())) continue;
            if (location.distance(loc) <= radiusLimit1) {
                alreadySpawned++;
            }
        }

        if (alreadySpawned >= spawnLimit1) {
            event.setCancelled(true);
            SpawnLimiterLog.onCancelledSpawn(event);
            return;
        }

        List<Location> addedLocation = new ArrayList<>(spawnLocations.get(entityType));
        addedLocation.add(location);
        spawnLocations.put(entityType, addedLocation);

        new BukkitRunnable() {
            @Override
            public void run() {

                List<Location> removedLocation = new ArrayList<>(spawnLocations.get(entityType));
                removedLocation.remove(location);
                spawnLocations.put(entityType, removedLocation);

            }
        }.runTaskLater(Main.getInstance(), timeLimit1 * 20);

    }

    public static void reloadLimitedEntities() {
        try {

            limitedEntities.clear();
            radiusLimit.clear();
            timeLimit.clear();
            spawnLimit.clear();
            spawnLocations.clear();

            for (String key : Main.getInstance().getConfig().getKeys(true)) {

                StringBuilder entityTypeName = new StringBuilder(key);

                if (entityTypeName.length() >= 12 && entityTypeName.toString().startsWith("SpawnLimiter")) {

                    entityTypeName.delete(0, 13);

                    if (!entityTypeName.toString().contains(".") && !entityTypeName.toString().isEmpty()) {

                        for (EntityType entityTypes : EntityType.values()) {

                            if (entityTypes.name().equals(entityTypeName.toString())) {

                                EntityType entityType = EntityType.valueOf(entityTypeName.toString());

                                limitedEntities.add(entityType);

                                radiusLimit.put(entityType, Main.getInstance().getConfig().getInt("SpawnLimiter." + entityTypeName + ".RadiusLimit"));
                                timeLimit.put(entityType, Main.getInstance().getConfig().getInt("SpawnLimiter." + entityTypeName + ".TimeLimit"));
                                spawnLimit.put(entityType, Main.getInstance().getConfig().getInt("SpawnLimiter." + entityTypeName + ".SpawnLimit"));
                                spawnLocations.put(entityType, new ArrayList<>());

                            }

                        }

                    }

                }

            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

}
