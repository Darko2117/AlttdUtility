package com.darko.main.utilities.logging;

import com.darko.main.Main;
import me.ryanhamshire.GriefPrevention.events.ClaimCreatedEvent;
import me.ryanhamshire.GriefPrevention.events.ClaimDeletedEvent;
import me.ryanhamshire.GriefPrevention.events.ClaimExpirationEvent;
import me.ryanhamshire.GriefPrevention.events.ClaimModifiedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.Date;

public class LoggingGriefPrevention implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClaimCreated(ClaimCreatedEvent event) {

        if (!Main.getInstance().getConfig().getBoolean(Logging.logNamesAndConfigPaths.get(Logging.claimsCreatedLogName) + ".Enabled"))
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String user = event.getCreator().getName();

        String lowestY = String.valueOf(event.getClaim().getLesserBoundaryCorner().getBlockY());

        String firstCorner = Logging.getBetterLocationString(event.getClaim().getLesserBoundaryCorner());
        String secondCorner = Logging.getBetterLocationString(event.getClaim().getGreaterBoundaryCorner());
        String area = firstCorner.concat(" - ").concat(secondCorner);

        String message = "";
        message = message.concat("|");
        message = message.concat("Time:");
        message = message.concat(time);
        message = message.concat("|");
        message = message.concat("User:");
        message = message.concat(user);
        message = message.concat("|");
        message = message.concat("LowestY:");
        message = message.concat(lowestY);
        message = message.concat("|");
        message = message.concat("Area:");
        message = message.concat(area);
        message = message.concat("|");

        Logging.WriteToFile(Logging.claimsCreatedLogName, message);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClaimDeleted(ClaimDeletedEvent event) {

        if (!Main.getInstance().getConfig().getBoolean(Logging.logNamesAndConfigPaths.get(Logging.claimsDeletedLogName) + ".Enabled"))
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String user = event.getClaim().getOwnerName();

        String lowestY = String.valueOf(event.getClaim().getLesserBoundaryCorner().getBlockY());

        String firstCorner = Logging.getBetterLocationString(event.getClaim().getLesserBoundaryCorner());
        String secondCorner = Logging.getBetterLocationString(event.getClaim().getGreaterBoundaryCorner());
        String area = firstCorner.concat(" - ").concat(secondCorner);

        String message = "";
        message = message.concat("|");
        message = message.concat("Time:");
        message = message.concat(time);
        message = message.concat("|");
        message = message.concat("User:");
        message = message.concat(user);
        message = message.concat("|");
        message = message.concat("LowestY:");
        message = message.concat(lowestY);
        message = message.concat("|");
        message = message.concat("Area:");
        message = message.concat(area);
        message = message.concat("|");

        Logging.WriteToFile(Logging.claimsDeletedLogName, message);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClaimModified(ClaimModifiedEvent event) {

        if (!Main.getInstance().getConfig().getBoolean(Logging.logNamesAndConfigPaths.get(Logging.claimsModifiedLogName) + ".Enabled"))
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String user = event.getClaim().getOwnerName();

        String lowestY = String.valueOf(event.getClaim().getLesserBoundaryCorner().getBlockY());

        String firstCorner = Logging.getBetterLocationString(event.getClaim().getLesserBoundaryCorner());
        String secondCorner = Logging.getBetterLocationString(event.getClaim().getGreaterBoundaryCorner());
        String area = firstCorner.concat(" - ").concat(secondCorner);

        String message = "";
        message = message.concat("|");
        message = message.concat("Time:");
        message = message.concat(time);
        message = message.concat("|");
        message = message.concat("User:");
        message = message.concat(user);
        message = message.concat("|");
        message = message.concat("LowestY:");
        message = message.concat(lowestY);
        message = message.concat("|");
        message = message.concat("Area:");
        message = message.concat(area);
        message = message.concat("|");

        Logging.WriteToFile(Logging.claimsModifiedLogName, message);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClaimExpiration(ClaimExpirationEvent event) {

        if (!Main.getInstance().getConfig().getBoolean(Logging.logNamesAndConfigPaths.get(Logging.claimsExpiredLogName) + ".Enabled"))
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String user = event.getClaim().getOwnerName();

        String lowestY = String.valueOf(event.getClaim().getLesserBoundaryCorner().getBlockY());

        String firstCorner = Logging.getBetterLocationString(event.getClaim().getLesserBoundaryCorner());
        String secondCorner = Logging.getBetterLocationString(event.getClaim().getGreaterBoundaryCorner());
        String area = firstCorner.concat(" - ").concat(secondCorner);

        String message = "";
        message = message.concat("|");
        message = message.concat("Time:");
        message = message.concat(time);
        message = message.concat("|");
        message = message.concat("User:");
        message = message.concat(user);
        message = message.concat("|");
        message = message.concat("LowestY:");
        message = message.concat(lowestY);
        message = message.concat("|");
        message = message.concat("Area:");
        message = message.concat(area);
        message = message.concat("|");

        Logging.WriteToFile(Logging.claimsExpiredLogName, message);

    }

}
