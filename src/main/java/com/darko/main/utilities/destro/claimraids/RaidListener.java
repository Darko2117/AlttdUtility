package com.darko.main.utilities.destro.claimraids;

import com.darko.main.Main;
import org.bukkit.Raid;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.raid.RaidTriggerEvent;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

public class RaidListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRaidStart(RaidTriggerEvent event) {

        if(event.isCancelled()) return;
        if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.BlockRaidsInClaimWithoutAccessTrust")) return;

        if (event.isCancelled())
            return;
        Player player = event.getPlayer();
        Raid raid = event.getRaid();
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(raid.getLocation(), true, null);
        if (claim != null) {
            if (claim.allowAccess(player) != null) {
                event.setCancelled(true);
            }
        }
    }

}
