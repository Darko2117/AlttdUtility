package com.darko.main.utilities.spawning;

import com.darko.main.Main;
import org.bukkit.Location;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.EnumUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class onEntitySpawn implements Listener {

    public static List<Location> spawnLoc = new ArrayList<>();

    @EventHandler
    public void onSpawn(EntitySpawnEvent e){

        for(String entityTypeString : Main.getInstance().getConfig().getKeys(true)){

            StringBuilder stringEdit = new StringBuilder(entityTypeString);

            if(entityTypeString.length() >= 12 && entityTypeString.substring(0, 12).equals("SpawnLimiter")){
                stringEdit.delete(0, 13);
            }

            if ( EnumUtils.isValidEnum(EntityType.class, stringEdit.toString()) ) {

                EntityType entityType = EntityType.valueOf(stringEdit.toString());

                if (e.getEntity().getType().equals(entityType)) {

                    Location location = e.getLocation();

                    Integer distanceLimit = Main.getInstance().getConfig().getInt("SpawnLimiter." + stringEdit + ".RadiusLimit");
                    Integer timeLimit = Main.getInstance().getConfig().getInt("SpawnLimiter." + stringEdit + ".TimeLimit");
                    Integer spawnLimit = Main.getInstance().getConfig().getInt("SpawnLimiter." + stringEdit + ".SpawnLimit");

                    Integer alreadySpawned = 0;

                    for (Location loc : spawnLoc) {
                        if (location.distance(loc) <= distanceLimit) {
                            alreadySpawned++;
                        }
                    }

                    if (alreadySpawned >= spawnLimit) {
                        e.setCancelled(true);
                        System.out.println(e.getEntityType().toString() + " tried to spawn but the cap is reached.");
                    }

                    if (!e.isCancelled()) {
                        spawnLoc.add(location);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                spawnLoc.remove(location);
                            }
                        }.runTaskLater(Main.getInstance(), timeLimit * 20);
                    }
                }
            }
        }
    }
}
