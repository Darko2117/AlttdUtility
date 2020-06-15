package com.darko.main.utilities.logging.itemslogging;

import com.darko.main.Main;
import com.darko.main.utilities.logging.Logging;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Date;

public class ItemTakenOutOfItemFrameLog implements Listener {

    @EventHandler
    public void onItemTakenOutOfItemFrame(EntityDamageByEntityEvent e) {

        if (e.getEntity() instanceof ItemFrame) {
            if (!((ItemFrame) e.getEntity()).getItem().getType().equals(Material.AIR)) {

                if (Main.getInstance().getConfig().getBoolean(Logging.LogNamesAndConfigPaths.get(Logging.itemTakenOutOfItemFrameLogName.substring(17)) + ".Enabled")) {

                    ItemFrame itemFrame = (ItemFrame) e.getEntity();
                    StringBuilder message = new StringBuilder();
                    Entity damager = e.getDamager();
                    String location = Logging.getBetterLocationString(e.getEntity().getLocation());
                    Date time = new Date(System.currentTimeMillis());
                    String itemInFrame = itemFrame.getItem().toString();
                    if(itemInFrame.length() > 1000) itemInFrame = itemInFrame.substring(0, 999);
                    itemInFrame = itemInFrame.replace("\n", "|");

                    message.append(time + " " + damager.getName() + " has taken out " + itemInFrame + " from an item frame at " + location + ".");

                    Logging.WriteToFile(Logging.itemTakenOutOfItemFrameLogName, message.toString());

                }
            }
        }
    }
}