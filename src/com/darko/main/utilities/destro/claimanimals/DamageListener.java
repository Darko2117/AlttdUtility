package com.darko.main.utilities.destro.claimanimals;

import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

public class DamageListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled())
            return;
        Entity damager = event.getDamager();
        Entity entity = event.getEntity();
        if (damager instanceof Player) {
            if (entity instanceof Animals) {
                Claim claim = GriefPrevention.instance.dataStore.getClaimAt(entity.getLocation(), true, null);
                if (claim != null) {
                    if (claim.allowEdit((Player) damager) != null) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

}
