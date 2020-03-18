package com.darko.main.utilities.claimlogging;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.darko.main.Main;

import me.ryanhamshire.GriefPrevention.events.ClaimCreatedEvent;

public class claimCreatedEventLog implements Listener {

    @EventHandler
    public void onClaimCreate(ClaimCreatedEvent e) throws IOException {
        Date time = new Date(System.currentTimeMillis());
        String creator = e.getCreator().getName();
        String height = String.valueOf(e.getClaim().getHeight());
        StringBuilder lowerPoint = new StringBuilder();
        lowerPoint.append("X:" + e.getClaim().getLesserBoundaryCorner().getBlockX() + " ");
        lowerPoint.append("Y:" + e.getClaim().getLesserBoundaryCorner().getBlockY() + " ");
        lowerPoint.append("Z:" + e.getClaim().getLesserBoundaryCorner().getBlockZ());
        StringBuilder message = new StringBuilder();
        message.append(time.toString() + " New claim created at " + lowerPoint + " by " + creator + ". The heights is "
                + height);
        FileWriter writer = new FileWriter(Main.getInstance().getDataFolder() + "/logs/claim-creation-log.txt", true);
        writer.write(message.toString() + "\n");
        writer.close();
    }
}
