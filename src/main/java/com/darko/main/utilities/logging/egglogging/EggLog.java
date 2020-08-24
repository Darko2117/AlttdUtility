package com.darko.main.utilities.logging.egglogging;

import java.util.Date;

import com.darko.main.utilities.logging.Logging;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEggThrowEvent;

import com.darko.main.Main;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

public class EggLog implements Listener {

    @EventHandler
    public void onEggThrow(PlayerEggThrowEvent event) {

        if (Main.getInstance().getConfig().getBoolean(Logging.LogNamesAndConfigPaths.get(Logging.eggLogLogName.substring(17)) + ".Enabled")) {

            String player = event.getPlayer().getName();
            String locationString = Logging.getBetterLocationString(event.getEgg().getLocation());
            Location location = event.getEgg().getLocation();
            Date time = new Date(System.currentTimeMillis());
            StringBuilder message = new StringBuilder();
            Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, true, null);
            if (claim != null) {
                String claimOwner = claim.getOwnerName();
                message.append(time.toString() + " " + player + " threw an egg at " + locationString + " which is inside " + claimOwner
                        + "'s claim.");
            } else {
                message.append(time.toString() + " " + player + " threw an egg at " + locationString + ".");
            }

            Logging.WriteToFile(Logging.eggLogLogName, message.toString());

        }
    }
}
