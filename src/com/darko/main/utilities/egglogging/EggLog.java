package com.darko.main.utilities.egglogging;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEggThrowEvent;

import com.darko.main.Main;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

public class EggLog implements Listener {

    @EventHandler
    public void onEggThrow(PlayerEggThrowEvent e) throws IOException {

        String player = e.getPlayer().getName();
        Location location = (Location) e.getEgg().getLocation();
        StringBuilder xyz = new StringBuilder();
        xyz.append("X:" + location.getBlockX() + " ");
        xyz.append("Y:" + location.getBlockY() + " ");
        xyz.append("Z:" + location.getBlockZ());
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, true, null);
        Date time = new Date(System.currentTimeMillis());
        FileWriter writer = new FileWriter(Main.getInstance().getDataFolder() + "/logs/egg-log.txt", true);
        if (claim != null) {
            String claimOwner = claim.getOwnerName();
            StringBuilder message = new StringBuilder(time.toString() + " " + player + " threw an egg at " + xyz
                    + " which is inside " + claimOwner + "'s claim.");
            writer.write(message.toString() + "\n");
        } else {
            StringBuilder message = new StringBuilder(time.toString() + " " + player + " threw an egg at " + xyz + ".");
            writer.write(message.toString() + "\n");
        }
        writer.close();
    }
}
