package com.darko.main.utilities.logging.itemslogging;

import com.darko.main.Main;
import com.darko.main.utilities.logging.Logging;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Date;

public class ItemPlacedInItemFrameLog implements Listener {

    @EventHandler
    public void onItemPlaceInItemFrame(PlayerInteractEntityEvent event) {

        if (event.getRightClicked() instanceof ItemFrame) {
            if (((ItemFrame) event.getRightClicked()).getItem().getType().equals(Material.AIR)) {
                if (!event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)) {

                    if (Main.getInstance().getConfig().getBoolean(Logging.LogNamesAndConfigPaths.get(Logging.itemPlacedInItemFrameLogName.substring(17)) + ".Enabled")) {

                        StringBuilder message = new StringBuilder();
                        Player player = event.getPlayer();
                        String location = Logging.getBetterLocationString(event.getRightClicked().getLocation());
                        Date time = new Date(System.currentTimeMillis());
                        String itemInFrame = player.getInventory().getItemInMainHand().toString();
                        if(itemInFrame.length() > 1000) itemInFrame = itemInFrame.substring(0, 999);
                        itemInFrame = itemInFrame.replace("\n", "|");

                        message.append(time + " " + player.getName() + " has placed " + itemInFrame + " in an item frame at " + location + ".");

                        Logging.WriteToFile(Logging.itemPlacedInItemFrameLogName, message.toString());

                    }
                }
            }
        }
    }
}
