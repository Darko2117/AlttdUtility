package com.darko.main.destro.petpickup;

import com.darko.main.AlttdUtility;
import de.Keyle.MyPet.api.entity.MyPet;
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

        MyPet pet = event.getPet();
        Player player = pet.getOwner().getPlayer();

        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(pet.getLocation().get(), true, null);

        if (claim != null && claim.allowContainers(player) != null) {

            event.setCancelled(true);

        }

    }

}