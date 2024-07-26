package com.darko.main.darko.witherOutsideClaim;

import com.darko.main.common.API.APIs;
import de.Keyle.MyPet.api.entity.MyPetBukkitEntity;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.List;

public class WitherOutsideClaim implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEntityExplode(EntityExplodeEvent event) {

        if (!(event.getEntity().getType().equals(EntityType.WITHER_SKULL) || event.getEntity().getType().equals(EntityType.WITHER)))
            return;

        if (APIs.isGriefPreventionFound()) {

            List<Block> blocksToNotBreak = new ArrayList<>();

            for (Block block : event.blockList()) {

                Claim claim = GriefPrevention.instance.dataStore.getClaimAt(block.getLocation(), true, null);
                if (claim == null) {
                    blocksToNotBreak.add(block);
                }

            }

            event.blockList().removeAll(blocksToNotBreak);

        }

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        if (!(event.getDamager().getType().equals(EntityType.WITHER_SKULL) || event.getDamager().getType().equals(EntityType.WITHER)))
            return;

        if (APIs.isGriefPreventionFound()) {
            Claim claim = GriefPrevention.instance.dataStore.getClaimAt(event.getEntity().getLocation(), true, null);
            if (claim != null) {
                return;
            }
        }

        if (!(event.getEntity().getType().equals(EntityType.PLAYER) || event.getEntity().getType().equals(EntityType.ITEM))) {
            if (!APIs.isMyPetFound()) {
                event.setCancelled(true);
                return;
            }
            if (!(event.getEntity() instanceof MyPetBukkitEntity)) {
                event.setCancelled(true);
                return;
            }
        }

    }

}
