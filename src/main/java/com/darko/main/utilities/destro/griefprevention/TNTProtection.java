package com.darko.main.utilities.destro.griefprevention;

import com.darko.main.Main;
import com.destroystokyo.paper.event.block.TNTPrimeEvent;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * https://discordapp.com/channels/141644560005595136/677219092717109289/784166704250880021
 */
public class TNTProtection implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTNTPrime(TNTPrimeEvent event) {
        if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.ProtectTNTArrowDamage", false)) return;
        if(event.getReason() == TNTPrimeEvent.PrimeReason.PROJECTILE) {
            Entity entity = event.getPrimerEntity();
            if(entity instanceof Arrow) {
                Arrow arrow = (Arrow) entity;
                if(arrow.getShooter() instanceof Player) {
                    Player player = (Player) arrow.getShooter();
                    Claim claim = GriefPrevention.instance.dataStore.getClaimAt(event.getBlock().getLocation(), true, null);
                    if (claim != null) {
                        if (claim.allowAccess(player) != null) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
