package com.darko.main.utilities.logging.prizelogging;

import java.util.Date;
import java.util.List;

import com.darko.main.utilities.logging.Logging;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.darko.main.Main;

import me.badbones69.crazycrates.api.events.PlayerPrizeEvent;
import me.badbones69.crazycrates.api.objects.ItemBuilder;

public class CratePrizeLog implements Listener {

    @EventHandler
    public void onPrize(PlayerPrizeEvent event) {

        if (Main.getInstance().getConfig().getBoolean(Logging.LogNamesAndConfigPaths.get(Logging.cratePrizeLogName.substring(17)) + ".Enabled")) {

            List<ItemBuilder> items = event.getPrize().getItemBuilders();
            StringBuilder message = new StringBuilder();
            if (items.size() != 0) {

                Date time = new Date(System.currentTimeMillis());
                String player = event.getPlayer().getName();
                message.append(time + " " + player + " won: ");

                for (ItemBuilder item : items) {

                    Integer amount = item.getAmount();
                    String itemName = item.getName();
                    message.append(amount + "x " + itemName + ", ");
                }
                message.delete(message.length() - 2, message.length());
                String crate = event.getCrateName();
                message.append(" from the crate: " + crate);
            } else {
                String player = event.getPlayer().getName();
                String crate = event.getCrateName();
                Date time = new Date(System.currentTimeMillis());
                String prize;

                if (event.getPrize().getDisplayItem().getItemMeta().hasDisplayName()) {
                    prize = event.getPrize().getDisplayItem().getItemMeta().getDisplayName();
                } else {
                    prize = event.getPrize().getDisplayItem().getType().toString();
                }

                message.append(time + " " + player + " won " + prize + " out of the Crate: " + crate);
            }
            List<String> commands = event.getPrize().getCommands();
            if (commands.size() != 0) {
                String player = event.getPlayer().getName();
                message.append("  |  " + player + "'s prize had commands attached:  |  ");
            }
            for (String command : commands) {
                message.append(command + "  |  ");
            }
            if (commands.size() != 0) {
                message.delete(message.length() - 5, message.length());
            }

            Logging.WriteToFile(Logging.cratePrizeLogName, message.toString());

        }
    }
}
