package com.darko.main.darko.logging;

import com.darko.main.common.API.APIs;
import com.darko.main.AlttdUtility;
import com.gmail.filoghost.farmlimiter.api.FarmLimitEvent;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.Date;

public class LoggingFarmLimiter implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onFarmLimit(FarmLimitEvent event) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean(Logging.logNamesAndConfigPaths.get(Logging.farmLimiterLogName) + ".Enabled"))
            return;

        for (LivingEntity entityToRemove : event.getEntitiesToRemove()) {

            String time = new Date(System.currentTimeMillis()).toString();

            String entity = entityToRemove.getType().toString();

            String location = Logging.getBetterLocationString(entityToRemove.getLocation());

            String claimOwner = "";
            if (APIs.GriefPreventionFound) {
                Claim claim = GriefPrevention.instance.dataStore.getClaimAt(entityToRemove.getLocation(), true, null);
                if (claim != null) claimOwner = claim.getOwnerName();
            }

            String message = "";
            message = message.concat("|");
            message = message.concat("Time:");
            message = message.concat(time);
            message = message.concat("|");
            message = message.concat("Entity:");
            message = message.concat(entity);
            message = message.concat("|");
            message = message.concat("Location:");
            message = message.concat(location);
            message = message.concat("|");
            message = message.concat("ClaimOwner:");
            message = message.concat(claimOwner);
            message = message.concat("|");

            Logging.addToLogWriteQueue(Logging.farmLimiterLogName, message);

        }

    }

}
