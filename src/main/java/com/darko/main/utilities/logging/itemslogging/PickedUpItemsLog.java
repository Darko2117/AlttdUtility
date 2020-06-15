package com.darko.main.utilities.logging.itemslogging;

import com.darko.main.Main;
import com.darko.main.utilities.logging.Logging;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Date;

public class PickedUpItemsLog implements Listener {

    @EventHandler
    public static void onItemPickup(EntityPickupItemEvent e) {

        if (Main.getInstance().getConfig().getBoolean(Logging.LogNamesAndConfigPaths.get(Logging.pickedUpItemsLogName.substring(17)) + ".Enabled")) {

            if (e.getEntity() instanceof Player) {
                Player player = (Player) e.getEntity();
                String location = Logging.getBetterLocationString(player.getLocation());
                Date time = new Date(System.currentTimeMillis());

                String item = e.getItem().getItemStack().toString();
                if (item.length() > 1000) item = item.substring(0, 999);
                item = item.replace("\n", "|");

                StringBuilder message = new StringBuilder();

                message.append(time + " " + player.getName() + " picked up " + item + " at " + location + ".");

                Logging.WriteToFile(Logging.pickedUpItemsLogName, message.toString());
            }

        }
    }
}
