package com.darko.main.utilities.logging.SpawnLogging;

import com.darko.main.Main;
import com.darko.main.utilities.logging.Logging;
import com.darko.main.utilities.other.APIs;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class SpawnLimitReachedLog {

    public static void onCancelledSpawn(EntitySpawnEvent e) {

        if (Main.getInstance().getConfig().getBoolean("Logging.SpawnLimitReachedLog.Enabled")) {

            EntityType entityType = e.getEntityType();
            String location = Logging.getBetterLocationString(e.getLocation());

            StringBuilder message = new StringBuilder();

            Date time = new Date(System.currentTimeMillis());
            String claimOwner = null;

            if (APIs.WorldGuardFound) {
                Claim claim = GriefPrevention.instance.dataStore.getClaimAt(e.getLocation(), true, null);
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
