package com.darko.main.utilities.griefprevention;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Nameable;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;

/**
 * https://discordapp.com/channels/141644560005595136/677219092717109289/788192729787138048
 */
public class PublicTraders {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onVillagerTrade(PlayerInteractEntityEvent event) {
        //if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.AllowNamedPublicVillagers")) return;//TODO add this toggle

        if (event.isCancelled()) {
            Entity entity = event.getRightClicked();
            Player player = event.getPlayer();
            /*
             * This should cover all chests, enderchests, barrels, furnacetypes, lectern, ....
             */
            if(entity.getType() == EntityType.VILLAGER) {
                // early check for custom name to not waste time on getting the claim if not needed?
                Villager villager = (Villager) entity;
                if(villager.getCustomName().equalsIgnoreCase("public")) {
                    // Do we need to check for claims? i assume it's cancelled because of no permission as we only continue if the event is canceled
                    // we don't need to check for worldguard regions either as spawn should also be claimed?
                    Claim claim = GriefPrevention.instance.dataStore.getClaimAt(entity.getLocation(), true, null);
                    if (claim != null) {
                        if (claim.allowAccess(player) != null) {
                            event.setCancelled(false);
                        }
                    }
                }
            }
        }
    }

}
