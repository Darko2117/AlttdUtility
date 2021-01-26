package com.darko.main.destro.griefprevention;

import com.darko.main.AlttdUtility;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.ChatColor;
import org.bukkit.Nameable;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;

/**
 * https://discordapp.com/channels/141644560005595136/677219092717109289/788689097064579083
 * adapted to be for all chests, barrels and shulkers
 */
public class PublicChests implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChestOpen(PlayerInteractEvent event) {
        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.AllowNamedPublicChests")) return;

        if (event.isCancelled() && event.hasBlock()) {
            Block block = event.getClickedBlock();
            Player player = event.getPlayer();
            /*
             * This should cover all chests, enderchests, barrels, furnacetypes, lectern, ....
             */
            if(block.getState() instanceof InventoryHolder) {
                if (block.getState() instanceof Nameable) {
                    Nameable nameable = (Nameable) block.getState();
                    if(nameable.getCustomName() == null) return;
                    if (ChatColor.stripColor(nameable.getCustomName()).equalsIgnoreCase("public")) {
                        // Do we need to check for claims? i assume it's cancelled because of no permission as we only continue if the event is canceled
                        // we don't need to check for worldguard regions either as spawn should also be claimed?
                        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(block.getLocation(), true, null);
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

}
