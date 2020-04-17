package com.darko.main.utilities.logging.ClaimLogging;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import com.darko.main.utilities.logging.Logging;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import com.darko.main.Main;

import me.ryanhamshire.GriefPrevention.events.ClaimModifiedEvent;

public class ClaimModifiedLog implements Listener {

    @EventHandler
    public void onClaimModify(ClaimModifiedEvent e) {

        if (Main.getInstance().getConfig().getBoolean(Logging.LogNamesAndConfigPaths.get(Logging.claimModifiedLogName.substring(17)) + ".Enabled")) {

            Date time = new Date(System.currentTimeMillis());
            String owner = e.getClaim().getOwnerName();
            String lowestY = String.valueOf(e.getClaim().getLesserBoundaryCorner().getBlockY());

            String firstCorner = Logging.getBetterLocationString(e.getClaim().getLesserBoundaryCorner());

            String secondCorner = Logging.getBetterLocationString(e.getClaim().getGreaterBoundaryCorner());

            StringBuilder message = new StringBuilder();
            message.append(time.toString() + " Existing claim by " + owner + " edited, new data is " + firstCorner + " - "
                    + secondCorner + ". The claim goes down to Y:" + lowestY + ".");

            Logging.WriteToFile(Logging.claimModifiedLogName, message.toString());

        }
    }
}
