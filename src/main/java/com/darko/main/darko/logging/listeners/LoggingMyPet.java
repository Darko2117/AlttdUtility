package com.darko.main.darko.logging.listeners;

import com.darko.main.AlttdUtility;
import com.darko.main.common.Methods;
import com.darko.main.darko.logging.Logging;
import com.darko.main.darko.logging.logs.DroppedItemsOnDeathLog;
import com.darko.main.darko.logging.logs.MyPetItemPickupLog;
import de.Keyle.MyPet.MyPetApi;
import de.Keyle.MyPet.api.Configuration;
import de.Keyle.MyPet.api.entity.MyPet;
import de.Keyle.MyPet.api.entity.MyPetType;
import de.Keyle.MyPet.api.event.MyPetInventoryActionEvent;
import de.Keyle.MyPet.api.event.MyPetPickupItemEvent;
import de.Keyle.MyPet.api.player.MyPetPlayer;
import de.Keyle.MyPet.api.skill.skills.Backpack;
import de.Keyle.MyPet.api.util.inventory.CustomInventory;
import de.Keyle.MyPet.skill.skills.BackpackImpl;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class LoggingMyPet implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onMyPetPickupItemEvent(MyPetPickupItemEvent event) {

        if (!Logging.getCachedLogFromName("MyPetItemPickupLog").isEnabled())
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String owner = event.getOwner().getPlayer().getName();

        String pet = event.getPet().toString();

        String item = event.getItem().getItemStack().toString();

        String location = Methods.getBetterLocationString(event.getPet().getLocation().get());

        MyPetItemPickupLog log = new MyPetItemPickupLog();
        log.addArgumentValue(time);
        log.addArgumentValue(owner);
        log.addArgumentValue(pet);
        log.addArgumentValue(item);
        log.addArgumentValue(location);

        Logging.addToLogWriteQueue(log);

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeathEvent1(PlayerDeathEvent event) {

        if (!Logging.getCachedLogFromName("DroppedItemsOnDeathLog").isEnabled())
            return;

        MyPetPlayer myPetPlayer = MyPetApi.getPlayerManager().getMyPetPlayer(event.getPlayer());

        if (!myPetPlayer.hasMyPet())
            return;

        MyPet myPet = myPetPlayer.getMyPet();

        if (!myPetPlayer.getMyPet().getStatus().equals(MyPet.PetState.Here))
            return;

        if (!Configuration.Skilltree.Skill.Backpack.DROP_WHEN_OWNER_DIES)
            return;

        if (!myPet.getSkills().isActive(BackpackImpl.class))
            return;

        CustomInventory customInventory = myPet.getSkills().get(BackpackImpl.class).getInventory();

        List<String> items = new ArrayList<>();
        for (ItemStack item : customInventory.getBukkitInventory().getContents()) {
            if (item == null)
                continue;
            items.add(item.toString());
        }

        String time = new Date(System.currentTimeMillis()).toString();

        String user = event.getEntity().getName() + "'s Mypet " + myPet.getPetName();

        String deathMessage = event.getDeathMessage();

        String location = Methods.getBetterLocationString(event.getEntity().getLocation());

        new BukkitRunnable() {
            @Override
            public void run() {

                for (String item : items) {

                    DroppedItemsOnDeathLog log = new DroppedItemsOnDeathLog();
                    log.addArgumentValue(time);
                    log.addArgumentValue(user);
                    log.addArgumentValue(deathMessage);
                    log.addArgumentValue(item);
                    log.addArgumentValue(location);

                    Logging.addToLogWriteQueue(log);

                }
            }
        }.runTaskLater(AlttdUtility.getInstance(), 1);

    }

}
