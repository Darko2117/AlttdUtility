package com.darko.main.utilities.logging.uiclicklogging;

import com.darko.main.Main;
import com.darko.main.utilities.logging.Logging;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.Date;

public class UIClicksLog implements Listener {

    @EventHandler
    public static void onInventoryCLick(InventoryClickEvent e) {

        if (Main.getInstance().getConfig().getBoolean(Logging.LogNamesAndConfigPaths.get(Logging.uiClickLogName.substring(17)) + ".Enabled")) {

            Player player = (Player) e.getWhoClicked();
            Date time = new Date(System.currentTimeMillis());
            InventoryView inventory = e.getView();
            ItemStack item = e.getCurrentItem();
            String itemString = "";

            if (item != null) {
                itemString = item.toString();
                if (itemString.length() > 1000) itemString = itemString.substring(0, 999);
                itemString = itemString.replace("\n", "|");
            }

            StringBuilder message = new StringBuilder();

            message.append(time + " " + player.getName() + " clicked in the inventory with the name: " + inventory.getTitle() + ".");
            if (item != null && !item.getType().equals(Material.AIR)) {
                message.append(" Their clicked item was " + itemString + ".");
            }

            Logging.WriteToFile(Logging.uiClickLogName, message.toString());

        }
    }
}
