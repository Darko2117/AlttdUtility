package com.darko.main.destro.claimraids;

import com.darko.main.AlttdUtility;
import org.bukkit.Raid;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.raid.RaidTriggerEvent;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

public class RaidListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onRaidStart(RaidTriggerEvent event) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.BlockRaidsInClaimWithoutAccessTrust")) return;

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
