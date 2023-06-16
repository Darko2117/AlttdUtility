package com.darko.main.darko.logging.listeners;

import com.darko.main.common.Methods;
import com.darko.main.darko.logging.Logging;
import com.darko.main.darko.logging.logs.ClaimsCreatedLog;
import com.darko.main.darko.logging.logs.ClaimsDeletedLog;
import com.darko.main.darko.logging.logs.ClaimsExpiredLog;
import com.darko.main.darko.logging.logs.ClaimsResizedLog;
import me.ryanhamshire.GriefPrevention.events.ClaimCreatedEvent;
import me.ryanhamshire.GriefPrevention.events.ClaimDeletedEvent;
import me.ryanhamshire.GriefPrevention.events.ClaimExpirationEvent;
import me.ryanhamshire.GriefPrevention.events.ClaimResizeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.Date;

public class LoggingGriefPrevention implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClaimCreatedEvent(ClaimCreatedEvent event) {

        if (!Logging.getCachedLogFromName("ClaimsCreatedLog").isEnabled())
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String user = event.getCreator().getName();

        String lowestY = String.valueOf(event.getClaim().getLesserBoundaryCorner().getBlockY());

        String firstCorner = Methods.getBetterLocationString(event.getClaim().getLesserBoundaryCorner());
        String secondCorner = Methods.getBetterLocationString(event.getClaim().getGreaterBoundaryCorner());
        String area = firstCorner.concat(" - ").concat(secondCorner);

        ClaimsCreatedLog log = new ClaimsCreatedLog();
        log.addArgumentValue(time);
        log.addArgumentValue(user);
        log.addArgumentValue(lowestY);
        log.addArgumentValue(area);

        Logging.addToLogWriteQueue(log);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClaimDeletedEvent(ClaimDeletedEvent event) {

        if (!Logging.getCachedLogFromName("ClaimsDeletedLog").isEnabled())
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String user = event.getClaim().getOwnerName();

        String lowestY = String.valueOf(event.getClaim().getLesserBoundaryCorner().getBlockY());

        String firstCorner = Methods.getBetterLocationString(event.getClaim().getLesserBoundaryCorner());
        String secondCorner = Methods.getBetterLocationString(event.getClaim().getGreaterBoundaryCorner());
        String area = firstCorner.concat(" - ").concat(secondCorner);

        ClaimsDeletedLog log = new ClaimsDeletedLog();
        log.addArgumentValue(time);
        log.addArgumentValue(user);
        log.addArgumentValue(lowestY);
        log.addArgumentValue(area);

        Logging.addToLogWriteQueue(log);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClaimResizeEvent(ClaimResizeEvent event) {

        if (!Logging.getCachedLogFromName("ClaimsResizedLog").isEnabled())
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String user = event.getClaim().getOwnerName();

        String lowestY = String.valueOf(event.getClaim().getLesserBoundaryCorner().getBlockY());

        String firstCorner = Methods.getBetterLocationString(event.getClaim().getLesserBoundaryCorner());
        String secondCorner = Methods.getBetterLocationString(event.getClaim().getGreaterBoundaryCorner());
        String area = firstCorner.concat(" - ").concat(secondCorner);

        ClaimsResizedLog log = new ClaimsResizedLog();
        log.addArgumentValue(time);
        log.addArgumentValue(user);
        log.addArgumentValue(lowestY);
        log.addArgumentValue(area);

        Logging.addToLogWriteQueue(log);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClaimExpirationEvent(ClaimExpirationEvent event) {

        if (!Logging.getCachedLogFromName("ClaimsExpiredLog").isEnabled())
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String user = event.getClaim().getOwnerName();

        String lowestY = String.valueOf(event.getClaim().getLesserBoundaryCorner().getBlockY());

        String firstCorner = Methods.getBetterLocationString(event.getClaim().getLesserBoundaryCorner());
        String secondCorner = Methods.getBetterLocationString(event.getClaim().getGreaterBoundaryCorner());
        String area = firstCorner.concat(" - ").concat(secondCorner);

        ClaimsExpiredLog log = new ClaimsExpiredLog();
        log.addArgumentValue(time);
        log.addArgumentValue(user);
        log.addArgumentValue(lowestY);
        log.addArgumentValue(area);

        Logging.addToLogWriteQueue(log);

    }

}
