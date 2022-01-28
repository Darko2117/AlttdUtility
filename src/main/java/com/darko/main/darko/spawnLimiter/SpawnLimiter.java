package com.darko.main.darko.spawnLimiter;

import lombok.Data;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.List;

@Data
public class SpawnLimiter {

    private EntityType entityType;
    private int radiusLimit;
    private int timeLimit;
    private int spawnLimit;
    private List<Location> spawnLocations;

    public SpawnLimiter(EntityType entityType, int radiusLimit, int timeLimit, int spawnLimit, List<Location> spawnLocations) {
        this.entityType = entityType;
        this.radiusLimit = radiusLimit;
        this.timeLimit = timeLimit;
        this.spawnLimit = spawnLimit;
        this.spawnLocations = spawnLocations;
    }

}
