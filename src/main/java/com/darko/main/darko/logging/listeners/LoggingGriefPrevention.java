package com.darko.main.darko.logging.listeners;

import com.darko.main.AlttdUtility;
import com.darko.main.darko.logging.Logging;
import com.darko.main.darko.logging.logs.ClaimsCreatedLog;
import com.darko.main.darko.logging.logs.ClaimsDeletedLog;
import com.darko.main.darko.logging.logs.ClaimsExpiredLog;
import com.darko.main.darko.logging.logs.ClaimsModifiedLog;
import com.darko.main.darko.logging.logs.NumberOfClaimsNotificationLog;
import me.ryanhamshire.GriefPrevention.events.ClaimCreatedEvent;
import me.ryanhamshire.GriefPrevention.events.ClaimDeletedEvent;
import me.ryanhamshire.GriefPrevention.events.ClaimExpirationEvent;
import me.ryanhamshire.GriefPrevention.events.ClaimModifiedEvent;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.Date;

public class LoggingGriefPrevention implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClaimCreatedEvent(ClaimCreatedEvent event) {

        if (!Logging.getCachedLogFromName("ClaimsCreatedLog").isEnabled()) return;

        String time = new Date(System.currentTimeMillis()).toString();

        String user = event.getCreator().getName();

        String lowestY = String.valueOf(event.getClaim().getLesserBoundaryCorner().getBlockY());

        String firstCorner = Logging.getBetterLocationString(event.getClaim().getLesserBoundaryCorner());
        String secondCorner = Logging.getBetterLocationString(event.getClaim().getGreaterBoundaryCorner());
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

        if (!Logging.getCachedLogFromName("ClaimsDeletedLog").isEnabled()) return;

        String time = new Date(System.currentTimeMillis()).toString();

        String user = event.getClaim().getOwnerName();

        String lowestY = String.valueOf(event.getClaim().getLesserBoundaryCorner().getBlockY());

        String firstCorner = Logging.getBetterLocationString(event.getClaim().getLesserBoundaryCorner());
        String secondCorner = Logging.getBetterLocationString(event.getClaim().getGreaterBoundaryCorner());
        String area = firstCorner.concat(" - ").concat(secondCorner);

        ClaimsDeletedLog log = new ClaimsDeletedLog();
        log.addArgumentValue(time);
        log.addArgumentValue(user);
        log.addArgumentValue(lowestY);
        log.addArgumentValue(area);

        Logging.addToLogWriteQueue(log);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClaimModifiedEvent(ClaimModifiedEvent event) {

        if (!Logging.getCachedLogFromName("ClaimsModifiedLog").isEnabled()) return;

        String time = new Date(System.currentTimeMillis()).toString();

        String user = event.getClaim().getOwnerName();

        String lowestY = String.valueOf(event.getClaim().getLesserBoundaryCorner().getBlockY());

        String firstCorner = Logging.getBetterLocationString(event.getClaim().getLesserBoundaryCorner());
        String secondCorner = Logging.getBetterLocationString(event.getClaim().getGreaterBoundaryCorner());
        String area = firstCorner.concat(" - ").concat(secondCorner);

        ClaimsModifiedLog log = new ClaimsModifiedLog();
        log.addArgumentValue(time);
        log.addArgumentValue(user);
        log.addArgumentValue(lowestY);
        log.addArgumentValue(area);

        Logging.addToLogWriteQueue(log);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClaimExpirationEvent(ClaimExpirationEvent event) {

        if (!Logging.getCachedLogFromName("ClaimsExpiredLog").isEnabled()) return;

        String time = new Date(System.currentTimeMillis()).toString();

        String user = event.getClaim().getOwnerName();

        String lowestY = String.valueOf(event.getClaim().getLesserBoundaryCorner().getBlockY());

        String firstCorner = Logging.getBetterLocationString(event.getClaim().getLesserBoundaryCorner());
        String secondCorner = Logging.getBetterLocationString(event.getClaim().getGreaterBoundaryCorner());
        String area = firstCorner.concat(" - ").concat(secondCorner);

        ClaimsExpiredLog log = new ClaimsExpiredLog();
        log.addArgumentValue(time);
        log.addArgumentValue(user);
        log.addArgumentValue(lowestY);
        log.addArgumentValue(area);

        Logging.addToLogWriteQueue(log);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClaimCreated1(ClaimCreatedEvent event) {

        if (!Logging.getCachedLogFromName("NumberOfClaimsNotificationLog").isEnabled()) return;

        new BukkitRunnable() {
            @Override
            public void run() {

                if (!(event.getCreator() instanceof Player)) return;

                Player player = (Player) event.getCreator();

                Integer numberOfClaimsInteger = 0;
                Integer minNumberOfClaimsToFlag = AlttdUtility.getInstance().getConfig().getInt("NumberOfClaimsFlag.MinNumberOfClaimsToLog");

                for (File f : new File(AlttdUtility.getInstance().getConfig().getString("NumberOfClaimsFlag.ClaimDataDirectory")).listFiles()) {

                    if (!f.getName().contains(".yml")) continue;

                    YamlConfiguration file = YamlConfiguration.loadConfiguration(f);

                    String ownerUUID = file.getString("Owner");

                    if (ownerUUID.isEmpty()) continue;

                    if (ownerUUID.equals(player.getUniqueId().toString())) numberOfClaimsInteger++;

                }

                if (numberOfClaimsInteger < minNumberOfClaimsToFlag) return;

                String time = new Date(System.currentTimeMillis()).toString();

                String user = event.getClaim().getOwnerName();

                String numberOfClaims = numberOfClaimsInteger.toString();

                NumberOfClaimsNotificationLog log = new NumberOfClaimsNotificationLog();
                log.addArgumentValue(time);
                log.addArgumentValue(user);
                log.addArgumentValue(numberOfClaims);

                Logging.addToLogWriteQueue(log);

            }
        }.runTaskAsynchronously(AlttdUtility.getInstance());

    }

}
