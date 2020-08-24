package com.darko.main.utilities.logging.claimlogging;

import java.util.Date;

import com.darko.main.utilities.logging.Logging;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.darko.main.Main;

import me.ryanhamshire.GriefPrevention.events.ClaimCreatedEvent;

public class ClaimCreatedLog implements Listener {

    @EventHandler
    public void onClaimCreate(ClaimCreatedEvent event) {

        if (Main.getInstance().getConfig().getBoolean(Logging.LogNamesAndConfigPaths.get(Logging.claimCreatedLogName.substring(17)) + ".Enabled")) {

            Date time = new Date(System.currentTimeMillis());
            String creator = event.getCreator().getName();
            String lowestY = String.valueOf(event.getClaim().getLesserBoundaryCorner().getBlockY());

            String firstCorner = Logging.getBetterLocationString(event.getClaim().getLesserBoundaryCorner());

            String secondCorner = Logging.getBetterLocationString(event.getClaim().getGreaterBoundaryCorner());

            StringBuilder message = new StringBuilder();
            message.append(time.toString() + " New claim created at " + firstCorner + " - " + secondCorner + " by "
                    + creator + ". The claim goes down to Y:" + lowestY + ".");


            Logging.WriteToFile(Logging.claimCreatedLogName, message.toString());

        }
    }
}
