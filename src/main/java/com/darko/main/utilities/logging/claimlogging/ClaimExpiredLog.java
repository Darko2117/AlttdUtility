package com.darko.main.utilities.logging.claimlogging;

import java.util.Date;

import com.darko.main.utilities.logging.Logging;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.darko.main.Main;

import me.ryanhamshire.GriefPrevention.events.ClaimExpirationEvent;

public class ClaimExpiredLog implements Listener {

    @EventHandler
    public void onClaimExpiration(ClaimExpirationEvent event) {

        if (Main.getInstance().getConfig().getBoolean(Logging.LogNamesAndConfigPaths.get(Logging.claimExpiredLogName.substring(17)) + ".Enabled")) {

            Date time = new Date(System.currentTimeMillis());
            String owner = event.getClaim().getOwnerName();
            String lowestY = String.valueOf(event.getClaim().getLesserBoundaryCorner().getBlockY());

            String firstCorner = Logging.getBetterLocationString(event.getClaim().getLesserBoundaryCorner());

            String secondCorner = Logging.getBetterLocationString(event.getClaim().getGreaterBoundaryCorner());

            StringBuilder message = new StringBuilder();
            message.append(time.toString() + " Claim of " + owner + " expired. The claim was located at " + firstCorner
                    + " - " + secondCorner + " and it went down to Y:" + lowestY + ".");

            Logging.WriteToFile(Logging.claimExpiredLogName, message.toString());

        }
    }
}
