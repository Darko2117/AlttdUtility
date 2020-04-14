package com.darko.main.utilities.logging.claimlogging;

import com.darko.main.Main;
import me.ryanhamshire.GriefPrevention.events.ClaimCreatedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class ClaimCreatedLog implements Listener {

    @EventHandler
    public void onClaimCreate(ClaimCreatedEvent e) {

        Date time = new Date(System.currentTimeMillis());
        String creator = e.getCreator().getName();
        String lowestY = String.valueOf(e.getClaim().getLesserBoundaryCorner().getBlockY());

        StringBuilder firstCorner = new StringBuilder();
        firstCorner.append("X:" + e.getClaim().getLesserBoundaryCorner().getBlockX() + " ");
        firstCorner.append("Z:" + e.getClaim().getLesserBoundaryCorner().getBlockZ());

        StringBuilder secondCorner = new StringBuilder();
        secondCorner.append("X:" + e.getClaim().getGreaterBoundaryCorner().getBlockX() + " ");
        secondCorner.append("Z:" + e.getClaim().getGreaterBoundaryCorner().getBlockZ());

        StringBuilder message = new StringBuilder();
        message.append(time.toString() + " New claim created at " + firstCorner + " - " + secondCorner + " by "
                + creator + ". The claim goes down to Y:" + lowestY + ".");

        new BukkitRunnable() {
            public void run() {
                try {
                    FileWriter writer = new FileWriter(
                            Main.getInstance().getDataFolder() + "/logs/claim-created-log.txt", true);
                    writer.write(message.toString() + "\n");
                    writer.close();
                } catch (IOException e) {
                }
            }
        }.runTaskAsynchronously(Main.getInstance());

    }
}
