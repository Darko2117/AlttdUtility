package com.darko.main.utilities.logging.ItemsLogging;

import com.darko.main.Main;
import com.darko.main.utilities.logging.Logging;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class DroppedItemsLog implements Listener {

    @EventHandler
    public static void onItemDrop(PlayerDropItemEvent e) {

        if (Main.getInstance().getConfig().getBoolean("Logging.DroppedItemsLog")) {

            Player player = e.getPlayer();
            String location = Logging.getBetterLocationString(e.getItemDrop().getLocation());
            Date time = new Date(System.currentTimeMillis());

            ItemStack item = e.getItemDrop().getItemStack();

            StringBuilder message = new StringBuilder();

            message.append(time + " " + player.getName() + " dropped " + item + " at " + location + ".");


            new BukkitRunnable() {
                public void run() {
                    try {
                        FileWriter writer = new FileWriter(Main.getInstance().getDataFolder() + "/logs/dropped-items-log.txt", true);
                        writer.write(message.toString() + "\n");
                        writer.close();
                    } catch (IOException e) {
                    }
                }
            }.runTaskAsynchronously(Main.getInstance());
        }
    }
}
