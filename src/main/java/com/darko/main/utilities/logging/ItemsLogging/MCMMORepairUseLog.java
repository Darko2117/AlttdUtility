package com.darko.main.utilities.logging.ItemsLogging;

import com.darko.main.Main;
import com.darko.main.utilities.logging.Logging;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Date;

public class MCMMORepairUseLog implements Listener {

    @EventHandler
    public void onIronBlockUse(PlayerInteractEvent e) {

        Player player = e.getPlayer();

        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (e.getHand().equals(EquipmentSlot.HAND)) {
                if (e.getClickedBlock().getType().equals(Material.IRON_BLOCK)) {
                    if (!player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                        if (Main.getInstance().getConfig().getBoolean(Logging.LogNamesAndConfigPaths.get(Logging.mcmmoRepairUseLogName.substring(17)) + ".Enabled")) {

                            ItemStack itemInHand = player.getInventory().getItemInMainHand();

                            if (!itemInHand.getType().equals(Material.IRON_BLOCK)) {

                                Date time = new Date(System.currentTimeMillis());

                                StringBuilder message = new StringBuilder();

                                message.append(time + " " + player.getName() + " right clicked on an iron block with the item: " + itemInHand);

                                Logging.WriteToFile(Logging.mcmmoRepairUseLogName, message.toString());

                            }
                        }
                    }
                }
            }
        }
    }
}
