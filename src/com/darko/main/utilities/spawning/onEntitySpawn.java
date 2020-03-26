package com.darko.main.utilities.spawning;

import com.darko.main.Main;
import com.darko.main.utilities.logging.spawnlogging.SpawnLimitReachedLog;
import org.bukkit.Location;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.EnumUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class onEntitySpawn implements Listener {

    public static HashMap<EntityType, List<Location>> spawnLoc = new HashMap<>();

    @EventHandler
    public void onSpawn(EntitySpawnEvent e){

        for(String entityTypeString : Main.getInstance().getConfig().getKeys(true)){

            StringBuilder stringEdit = new StringBuilder(entityTypeString);

            if(entityTypeString.length() >= 12 && entityTypeString.startsWith("SpawnLimiter")){
                stringEdit.delete(0, 13);
            }

            if ( EnumUtils.isValidEnum(EntityType.class, stringEdit.toString()) ) {

                EntityType entityType = EntityType.valueOf(stringEdit.toString());

                if (e.getEntity().getType().equals(entityType)) {

                    if(!spawnLoc.containsKey(entityType)){
                        List <Location> emptyLocationsList = new ArrayList<>();
                        spawnLoc.put(entityType, emptyLocationsList);
                    }

                    Location location = e.getLocation();

                    Integer distanceLimit = Main.getInstance().getConfig().getInt("SpawnLimiter." + stringEdit + ".RadiusLimit");
                    Integer timeLimit = Main.getInstance().getConfig().getInt("SpawnLimiter." + stringEdit + ".TimeLimit");
                    Integer spawnLimit = Main.getInstance().getConfig().getInt("SpawnLimiter." + stringEdit + ".SpawnLimit");

                    Integer alreadySpawned = 0;

                    for (Location loc : spawnLoc.get(entityType)) {
                        if (location.distance(loc) <= distanceLimit) {
                            alreadySpawned++;
                        }
                    }

                    if (alreadySpawned >= spawnLimit) {
                        e.setCancelled(true);
                        SpawnLimitReachedLog.onCancelledSpawn(e);
                    }

                    if (!e.isCancelled()) {

                        List <Location> addedLocation = new ArrayList<>(spawnLoc.get(entityType));
                        addedLocation.add(location);
                        spawnLoc.put(entityType, addedLocation);

                        new BukkitRunnable() {
                            @Override
                            public void run() {

                                List <Location> removedLocation = new ArrayList<>(spawnLoc.get(entityType));
                                removedLocation.remove(location);
                                spawnLoc.put(entityType, removedLocation);
                            }
                        }.runTaskLater(Main.getInstance(), timeLimit * 20);
                    }
                }
            }
        }
    }
}
