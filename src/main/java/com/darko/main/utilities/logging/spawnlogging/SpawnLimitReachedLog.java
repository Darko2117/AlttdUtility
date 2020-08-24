package com.darko.main.utilities.logging.spawnlogging;

import com.darko.main.Main;
import com.darko.main.utilities.logging.Logging;
import com.darko.main.API.APIs;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.Date;

public class SpawnLimitReachedLog {

    public static void onCancelledSpawn(EntitySpawnEvent event) {

        if (Main.getInstance().getConfig().getBoolean(Logging.LogNamesAndConfigPaths.get(Logging.spawnLimitReachedLogName.substring(17)) + ".Enabled")) {

            EntityType entityType = event.getEntityType();
            String location = Logging.getBetterLocationString(event.getLocation());

            StringBuilder message = new StringBuilder();

            Date time = new Date(System.currentTimeMillis());
            String claimOwner = null;

            if (APIs.WorldGuardFound) {
                Claim claim = GriefPrevention.instance.dataStore.getClaimAt(event.getLocation(), true, null);
                if (claim != null) {
                    claimOwner = claim.getOwnerName();
                    message.append(time + " " + entityType + " tried to spawn at " + location + " which is in " + claimOwner + "'s claim but the spawn max is reached.");
                } else {
                    message.append(time + " " + entityType + " tried to spawn at " + location + " but the spawn max is reached.");
                }
            } else {
                message.append(time + " " + entityType + " tried to spawn at " + location + " but the spawn max is reached.");
            }

            Logging.WriteToFile(Logging.spawnLimitReachedLogName, message.toString());

        }
    }
}
