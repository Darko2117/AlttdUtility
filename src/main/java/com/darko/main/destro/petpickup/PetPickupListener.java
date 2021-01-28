package com.darko.main.destro.petpickup;

import com.darko.main.AlttdUtility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import de.Keyle.MyPet.api.event.MyPetPickupItemEvent;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

public class PetPickupListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPetPickup(MyPetPickupItemEvent event) {

        if (event.isCancelled()) return;
        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.BlockPetPickupInClaimWithoutContainerTrust"))
            return;

        if (event.isCancelled())
            return;
        Player player = event.getPet().getOwner().getPlayer();
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(player.getLocation(), true, null);
        if (claim != null) {
            if (claim.allowContainers(player) != null) {
                event.setCancelled(true);
            }
        }
    }

}
