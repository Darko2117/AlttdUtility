package com.darko.main.darko.storepetonpvp;

import com.darko.main.AlttdUtility;
import com.darko.main.common.Methods;
import de.Keyle.MyPet.MyPetApi;
import de.Keyle.MyPet.api.Configuration;
import de.Keyle.MyPet.api.entity.Pet;
import de.Keyle.MyPet.api.player.MyPetPlayer;
import de.Keyle.MyPet.skill.skills.BackpackImpl;
import me.chancesd.pvpmanager.event.PlayerTogglePvPEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class StorePetOnPVP implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerTogglePvPEvent(PlayerTogglePvPEvent event) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.StorePetOnPVP"))
            return;

        if (!event.getPvPState())
            return;

        MyPetPlayer myPetPlayer = MyPetApi.getPlayerManager().getMyPetPlayer(event.getPlayer());

        if (!myPetPlayer.hasPet())
            return;

        Pet myPet = myPetPlayer.getPet();

        if (!myPetPlayer.getPet().getStatus().equals(Pet.PetState.Here))
            return;

        if (!Configuration.Skilltree.Skill.Backpack.DROP_WHEN_OWNER_DIES)
            return;

        if (!myPet.getSkills().isActive(BackpackImpl.class))
            return;

        myPet.removePet();
        Methods.sendConfigMessage(event.getPlayer(), "Messages.StorePetOnPVPPetStored");

    }

}
