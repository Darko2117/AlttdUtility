package com.darko.main.utilities.destro.petpickup;

import de.Keyle.MyPet.api.event.MyPetPickupItemEvent;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PetPickupListener implements Listener {

    @EventHandler
    public void onPetPickup(MyPetPickupItemEvent event) {
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
