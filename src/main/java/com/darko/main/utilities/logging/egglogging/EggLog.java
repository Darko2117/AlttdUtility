package com.darko.main.utilities.logging.egglogging;

import com.darko.main.Main;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class EggLog implements Listener {

    @EventHandler
    public void onEggThrow(PlayerEggThrowEvent e) {

        String player = e.getPlayer().getName();
        Location location = (Location) e.getEgg().getLocation();
        StringBuilder xyz = new StringBuilder();
        xyz.append("X:" + location.getBlockX() + " ");
        xyz.append("Y:" + location.getBlockY() + " ");
        xyz.append("Z:" + location.getBlockZ());
        Date time = new Date(System.currentTimeMillis());
        StringBuilder message = new StringBuilder();
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, true, null);
        if (claim != null) {
            String claimOwner = claim.getOwnerName();
            message.append(time.toString() + " " + player + " threw an egg at " + xyz + " which is inside " + claimOwner
                    + "'s claim.");
        } else {
            message.append(time.toString() + " " + player + " threw an egg at " + xyz + ".");
        }

        new BukkitRunnable() {
            public void run() {
                try {
                    FileWriter writer = new FileWriter(Main.getInstance().getDataFolder() + "/logs/egg-log.txt", true);
                    writer.write(message.toString() + "\n");
                    writer.close();
                } catch (IOException e) {
                }
            }
        }.runTaskAsynchronously(Main.getInstance());

    }
}
