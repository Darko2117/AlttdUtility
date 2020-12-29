package com.darko.main.utilities.griefprevention;

import com.darko.main.Main;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 * https://discordapp.com/channels/141644560005595136/677219092717109289/788192729787138048
 */
public class PublicTraders implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onVillagerTrade(PlayerInteractEntityEvent event) {
        if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.AllowNamedPublicVillagers", false)) return;

        if (event.isCancelled()) {
            Entity entity = event.getRightClicked();
            Player player = event.getPlayer();
            /*
             * This should cover all chests, enderchests, barrels, furnacetypes, lectern, ....
             */
            if(entity.getType() == EntityType.VILLAGER) {
                // early check for custom name to not waste time on getting the claim if not needed?
                Villager villager = (Villager) entity;
                if(ChatColor.stripColor(villager.getCustomName()).equalsIgnoreCase("public")) {
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
