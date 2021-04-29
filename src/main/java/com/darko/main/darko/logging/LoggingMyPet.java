package com.darko.main.darko.logging;

import com.darko.main.AlttdUtility;
import de.Keyle.MyPet.api.event.MyPetPickupItemEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.Date;

public class LoggingMyPet implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMyPetPickupItem(MyPetPickupItemEvent event){

        if (!AlttdUtility.getInstance().getConfig().getBoolean(Logging.logNamesAndConfigPaths.get(Logging.petItemPickupLogName) + ".Enabled"))
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String pet = event.getPet().toString();

        String owner = event.getOwner().getPlayer().getName();

        String item = event.getItem().getItemStack().toString();

        String location = Logging.getBetterLocationString(event.getPet().getLocation().get());

        String message = "";
        message = message.concat("|");
        message = message.concat("Time:");
        message = message.concat(time);
        message = message.concat("|");
        message = message.concat("Pet:");
        message = message.concat(pet);
        message = message.concat("|");
        message = message.concat("Owner:");
        message = message.concat(owner);
        message = message.concat("|");
        message = message.concat("Item:");
        message = message.concat(item);
        message = message.concat("|");
        message = message.concat("Location:");
        message = message.concat(location);
        message = message.concat("|");

        Logging.addToLogWriteQueue(Logging.petItemPickupLogName, message);

    }

}
