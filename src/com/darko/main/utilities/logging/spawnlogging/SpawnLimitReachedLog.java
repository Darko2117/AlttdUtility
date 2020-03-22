package com.darko.main.utilities.logging.spawnlogging;

import com.darko.main.Main;
import com.darko.main.utilities.other.APIs;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class SpawnLimitReachedLog{

    public static void onCancelledSpawn(EntitySpawnEvent e) {

        EntityType entityType = e.getEntityType();
        Integer locationX = e.getLocation().getBlockX();
        Integer locationY = e.getLocation().getBlockY();
        Integer locationZ = e.getLocation().getBlockZ();

        StringBuilder message = new StringBuilder();

        Date time = new Date(System.currentTimeMillis());
        String claimOwner = null;

        if(APIs.WorldGuardFound){
            Claim claim = GriefPrevention.instance.dataStore.getClaimAt(e.getLocation(), true, null);
            if (claim != null) {
                claimOwner = claim.getOwnerName();
                message.append(time + " " + entityType + " tried to spawn at X:" + locationX + " Y:" + locationY + " Z:" + locationZ + " which is in " + claimOwner + "'s claim but the spawn max is reached.");
            }else{
                message.append(time + " " + entityType + " tried to spawn at X:" + locationX + " Y:" + locationY + " Z:" + locationZ + " but the spawn max is reached.");
            }
            }else{
            message.append(time + " " + entityType + " tried to spawn at X:" + locationX + " Y:" + locationY + " Z:" + locationZ + " but the spawn max is reached.");
        }

        new BukkitRunnable() {
            public void run() {
                try {
                    FileWriter writer = new FileWriter(Main.getInstance().getDataFolder() + "/logs/spawn-limit-reached-log.txt", true);
                    writer.write(message.toString() + "\n");
                    writer.close();
                } catch (IOException e) {
                }
            }
        }.runTaskAsynchronously(Main.getInstance());

    }
}
