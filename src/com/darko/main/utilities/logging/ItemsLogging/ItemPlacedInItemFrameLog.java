package com.darko.main.utilities.logging.ItemsLogging;

import com.darko.main.Main;
import com.darko.main.utilities.logging.Logging;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class ItemPlacedInItemFrameLog implements Listener {

    @EventHandler
    public void onItemPlaceInItemFrame(PlayerInteractEntityEvent e) {

        if (e.getRightClicked() instanceof ItemFrame) {
            if (((ItemFrame) e.getRightClicked()).getItem().getType().equals(Material.AIR)) {
                if (!e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)) {

                    if (Main.getInstance().getConfig().getBoolean("Logging.ItemPlacedInItemFrameLog")) {

                        StringBuilder message = new StringBuilder();
                        Player player = e.getPlayer();
                        String location = Logging.getBetterLocationString(e.getRightClicked().getLocation());
                        Date time = new Date(System.currentTimeMillis());
                        ItemStack itemInFrame = player.getInventory().getItemInMainHand();

                        message.append(time + " " + player.getName() + " has placed " + itemInFrame + " in an item frame at " + location + ".");

                        new BukkitRunnable() {
                            public void run() {
                                try {
                                    FileWriter writer = new FileWriter(Main.getInstance().getDataFolder() + "/logs/item-placed-in-item-frame-log.txt", true);
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
