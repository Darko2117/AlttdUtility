package com.darko.main.utilities.logging.itemslogging;

import com.darko.main.Main;
import com.darko.main.utilities.logging.Logging;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Date;

public class DroppedItemsLog implements Listener {

    @EventHandler
    public static void onItemDrop(PlayerDropItemEvent e) {

        if (Main.getInstance().getConfig().getBoolean(Logging.LogNamesAndConfigPaths.get(Logging.droppedItemsLogName.substring(17)) + ".Enabled")) {

            Player player = e.getPlayer();
            String location = Logging.getBetterLocationString(e.getItemDrop().getLocation());
            Date time = new Date(System.currentTimeMillis());

            String item = e.getItemDrop().getItemStack().toString();
            if(item.length() > 1000) item = item.substring(0, 999);
            item = item.replace("\n", "|");

            StringBuilder message = new StringBuilder();

            message.append(time + " " + player.getName() + " dropped " + item + " at " + location + ".");

            Logging.WriteToFile(Logging.droppedItemsLogName, message.toString());

        }
    }
}
