package com.darko.main.destro.tamedexpire;

import java.util.concurrent.TimeUnit;

import com.darko.main.AlttdUtility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ocelot;
//import org.bukkit.entity.Ocelot.Type;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import me.ryanhamshire.GriefPrevention.GriefPrevention;

public class onEntityInteractWithLead implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void EntityInteract(PlayerInteractEntityEvent event) {

        if(!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.UnclaimAnimalWithLead")) return;

        Player p = event.getPlayer();
        Entity e = event.getRightClicked();
        /**
         * Check if the player has the needed permission
         */
        if (p.hasPermission("tamed.expire")) {
            /**
             * Check if the player has a lead in his mainhand
             */
            if (p.getInventory().getItemInMainHand().getType().equals(Material.LEAD)) {
                /**
                 * Always check for null
                 */
                if (event.getRightClicked() != null) {
                    /**
                     * Can the mob be tamed
                     */
                    if (event.getRightClicked() instanceof Tameable) {
                        Tameable tameable = (Tameable) e;
                        /**
                         * Always check for null TODO, check if it's needed to check if owner is
                         * instance of player? skelehorses are tamed by default! and can be untamed
                         */
                        if (tameable.getOwner() != null) {
                            /**
                             * Since we are working with offline players change the code check if the player
                             * isn't online
                             */
                            // AnimalTamer player = ((Tameable) e).getOwner();
                            OfflinePlayer ownerInfo = Bukkit.getServer()
                                    .getOfflinePlayer(tameable.getOwner().getUniqueId());
                            if (ownerInfo.isOnline()) {
                                return;
                            }
                            /**
                             * Possible fix for skelehorses, check if the owner has played before TODO check
                             * this fix
                             */
                            if (ownerInfo.getLastPlayed() <= 0) {
                                return;
                            }
                            /**
                             * This is where we check how long the player has been offline for. MC stores
                             * this in milliseconds, so we convert the offline time to days first and
                             * compare it to GP config value for claim expirationdays.
                             *
                             */
                            if (TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - ownerInfo
                                    .getLastPlayed()) >= GriefPrevention.instance.config_claims_expirationDays) {
                                if (e instanceof Ocelot) {
                                    // ((Ocelot) e).setCatType(Type.WILD_OCELOT);
                                    // ((Ocelot) e).setSitting(false);
                                }
                                if (e instanceof Wolf) {
                                    ((Wolf) e).setSitting(false);
                                }
                                ((Tameable) e).setOwner(null);
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }
}
