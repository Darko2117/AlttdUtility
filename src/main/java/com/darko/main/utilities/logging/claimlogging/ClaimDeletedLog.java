package com.darko.main.utilities.logging.ClaimLogging;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import com.darko.main.utilities.logging.Logging;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import com.darko.main.Main;

import me.ryanhamshire.GriefPrevention.events.ClaimDeletedEvent;

public class ClaimDeletedLog implements Listener {

    @EventHandler
    public void onClaimDelete(ClaimDeletedEvent e) {

        if (Main.getInstance().getConfig().getBoolean(Logging.LogNamesAndConfigPaths.get(Logging.claimDeletedLogName.substring(17)) + ".Enabled")) {

            Date time = new Date(System.currentTimeMillis());
            String owner = e.getClaim().getOwnerName();
            String lowestY = String.valueOf(e.getClaim().getLesserBoundaryCorner().getBlockY());

            String firstCorner = Logging.getBetterLocationString(e.getClaim().getLesserBoundaryCorner());

            String secondCorner = Logging.getBetterLocationString(e.getClaim().getGreaterBoundaryCorner());

            StringBuilder message = new StringBuilder();
            message.append(time.toString() + " Claim of " + owner + " deleted. The claim was located at " + firstCorner
                    + " - " + secondCorner + " and it went down to Y:" + lowestY);

            Logging.WriteToFile(Logging.claimDeletedLogName, message.toString());

        }
    }
}
