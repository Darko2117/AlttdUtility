package com.darko.main.utilities.logging.ItemsLogging;

import com.darko.main.Main;
import com.darko.main.utilities.logging.Logging;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
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
                    ItemStack itemInFrame = itemFrame.getItem();

                    message.append(time + " " + damager.getName() + " has taken out " + itemInFrame + " from an item frame at " + location + ".");

                    Logging.WriteToFile(Logging.itemTakenOutOfItemFrameLogName, message.toString());

                }
            }
        }
    }
}