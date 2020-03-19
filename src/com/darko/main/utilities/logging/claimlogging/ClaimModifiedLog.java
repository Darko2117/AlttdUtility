package com.darko.main.utilities.logging.claimlogging;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.darko.main.Main;

import me.ryanhamshire.GriefPrevention.events.ClaimModifiedEvent;

public class ClaimModifiedLog implements Listener {

    @EventHandler
    public void onClaimModify(ClaimModifiedEvent e) throws IOException {

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
        message.append(time.toString() + " Existing claim by " + owner + " edited, new data is " + firstCorner + " - "
                + secondCorner + ". The claim goes down to Y:" + lowestY + ".");
        FileWriter writer = new FileWriter(Main.getInstance().getDataFolder() + "/logs/claim-modified-log.txt", true);
        writer.write(message.toString() + "\n");
        writer.close();
    }
}
