package com.darko.main.utilities.logging.ItemsLogging;

import com.darko.main.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class MCMMORepairUseLog implements Listener {

    @EventHandler
    public void onIronBlockUse(PlayerInteractEvent e) {

        Player player = e.getPlayer();
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (e.getClickedBlock().getType().equals(Material.IRON_BLOCK)) {
                if (!player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                    if (Main.getInstance().getConfig().getBoolean("Logging.MCMMORepairUseLog")) {

                        ItemStack itemInHand = player.getInventory().getItemInMainHand();
                        Date time = new Date(System.currentTimeMillis());

                        StringBuilder message = new StringBuilder();

                        message.append(time + " " + player.getName() + " right clicked on an iron block with the item: " + itemInHand);

                        new BukkitRunnable() {
                            public void run() {
                                try {
                                    FileWriter writer = new FileWriter(Main.getInstance().getDataFolder() + "/logs/mcmmo-repair-use-log.txt", true);
                                    writer.write(message.toString() + "\n");
                                    writer.close();
                                } catch (IOException e) {
                                }
                            }
                        }.runTaskAsynchronously(Main.getInstance());


                    }
                }
            }
        }
    }
}
