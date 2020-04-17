package com.darko.main.utilities.logging.EggLogging;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import com.darko.main.utilities.logging.Logging;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.darko.main.Main;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

public class EggLog implements Listener {

    @EventHandler
    public void onEggThrow(PlayerEggThrowEvent e) {

        if (Main.getInstance().getConfig().getBoolean(Logging.LogNamesAndConfigPaths.get(Logging.eggLogLogName.substring(17)) + ".Enabled")) {

            String player = e.getPlayer().getName();
            String locationString = Logging.getBetterLocationString(e.getEgg().getLocation());
            Location location = e.getEgg().getLocation();
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
