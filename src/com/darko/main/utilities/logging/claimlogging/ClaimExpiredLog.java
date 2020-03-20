package com.darko.main.utilities.logging.claimlogging;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import com.darko.main.Main;

import me.ryanhamshire.GriefPrevention.events.ClaimExpirationEvent;

public class ClaimExpiredLog implements Listener {

    @EventHandler
    public void onClaimExpiration(ClaimExpirationEvent e) {

        Date time = new Date(System.currentTimeMillis());
        String owner = e.getClaim().getOwnerName();
        String lowestY = String.valueOf(e.getClaim().getLesserBoundaryCorner().getBlockY());

        StringBuilder firstCorner = new StringBuilder();
        firstCorner.append("X:" + e.getClaim().getLesserBoundaryCorner().getBlockX() + " ");
        firstCorner.append("Z:" + e.getClaim().getLesserBoundaryCorner().getBlockZ());

        StringBuilder secondCorner = new StringBuilder();
        secondCorner.append("X:" + e.getClaim().getGreaterBoundaryCorner().getBlockX() + " ");
        secondCorner.append("Z:" + e.getClaim().getGreaterBoundaryCorner().getBlockZ());

        StringBuilder message = new StringBuilder();
        message.append(time.toString() + " Claim of " + owner + " expired. The claim was located at " + firstCorner
                + " - " + secondCorner + " and it went down to Y:" + lowestY);

        new BukkitRunnable() {
            public void run() {
                try {
                    FileWriter writer = new FileWriter(
                            Main.getInstance().getDataFolder() + "/logs/claim-expired-log.txt", true);
                    writer.write(message.toString() + "\n");
                    writer.close();
                } catch (IOException e) {
                }
            }
        }.runTaskAsynchronously(Main.getInstance());

    }
}
