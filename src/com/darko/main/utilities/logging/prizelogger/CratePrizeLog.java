package com.darko.main.utilities.logging.prizelogger;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.darko.main.Main;

import me.badbones69.crazycrates.api.events.PlayerPrizeEvent;
import me.badbones69.crazycrates.api.objects.ItemBuilder;
import net.md_5.bungee.api.ChatColor;

public class CratePrizeLog implements Listener {

    @EventHandler
    public void onPrize(PlayerPrizeEvent event) throws IOException {
        List<ItemBuilder> items = event.getPrize().getItemBuilders();
        StringBuilder message = new StringBuilder();
        FileWriter writer = new FileWriter(Main.getInstance().getDataFolder() + "/logs/crate-prize-log.txt", true);
        if (items.size() != 0) {

            Date time = new Date(System.currentTimeMillis());
            String player = event.getPlayer().getDisplayName();
            message.append(time + " " + player + ChatColor.RESET + " won: ");

            for (ItemBuilder item : items) {

                Integer amount = item.getAmount();
                String itemName = item.getName();
                message.append(amount + "x " + itemName + ChatColor.RESET + ", ");
            }
            message.delete(message.length() - 2, message.length());
            String crate = event.getCrateName();
            message.append(" from the crate: " + crate + "\n");
        } else {
            String player = event.getPlayer().getDisplayName();
            String prize = event.getPrize().getDisplayItem().getItemMeta().getDisplayName();
            String crate = event.getCrateName();
            Date time = new Date(System.currentTimeMillis());
            message.append(time + " " + player + ChatColor.RESET + " won " + prize + ChatColor.RESET
                    + " out of the Crate: " + crate + "\n");
        }
        List<String> commands = event.getPrize().getCommands();
        if (commands.size() != 0) {
            String player = event.getPlayer().getDisplayName();
            message.append(player + "'s prize had commands attached:\n");
        }
        for (String command : commands) {
            message.append(command + "\n");
        }
        if (commands.size() != 0) {
            message.delete(message.length() - 2, message.length());
        }

        writer.write(message.toString() + "\n");
        writer.close();
    }
}
