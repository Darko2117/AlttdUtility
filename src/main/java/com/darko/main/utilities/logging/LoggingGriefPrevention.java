package com.darko.main.utilities.logging;

import com.darko.main.AlttdUtility;
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
    public void onClaimCreated(ClaimCreatedEvent event) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean(Logging.logNamesAndConfigPaths.get(Logging.claimsCreatedLogName) + ".Enabled"))
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

        if (!AlttdUtility.getInstance().getConfig().getBoolean(Logging.logNamesAndConfigPaths.get(Logging.claimsDeletedLogName) + ".Enabled"))
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

        if (!AlttdUtility.getInstance().getConfig().getBoolean(Logging.logNamesAndConfigPaths.get(Logging.claimsModifiedLogName) + ".Enabled"))
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

        if (!AlttdUtility.getInstance().getConfig().getBoolean(Logging.logNamesAndConfigPaths.get(Logging.claimsExpiredLogName) + ".Enabled"))
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

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClaimCreated1(ClaimCreatedEvent event) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean(Logging.logNamesAndConfigPaths.get(Logging.numberOfClaimsNotificationLogName) + ".Enabled"))
            return;

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

                String message = "";
                message = message.concat("|");
                message = message.concat("Time:");
                message = message.concat(time);
                message = message.concat("|");
                message = message.concat("User:");
                message = message.concat(user);
                message = message.concat("|");
                message = message.concat("NumberOfClaims:");
                message = message.concat(numberOfClaims);
                message = message.concat("|");

                Logging.WriteToFile(Logging.numberOfClaimsNotificationLogName, message);

            }
        }.runTaskAsynchronously(AlttdUtility.getInstance());

    }

}
