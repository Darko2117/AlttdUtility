package com.darko.main.utilities.destro.prizelogger;

import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.darko.main.Main;

import me.badbones69.crazycrates.api.events.PlayerPrizeEvent;
import me.badbones69.crazycrates.api.objects.ItemBuilder;
import net.md_5.bungee.api.ChatColor;

public class PrizeListener implements Listener {

    @EventHandler
    public void onPrize(PlayerPrizeEvent event) {
        List<ItemBuilder> items = event.getPrize().getItemBuilders();
        if (items.size() != 0) {
            for (ItemBuilder item : items) {
                String player = event.getPlayer().getDisplayName();
                Integer amount = item.getAmount();
                String itemName = item.getName();
                String crate = event.getCrateName();
                Main.getInstance().getLogger().info(
                        player + " won " + amount + "x " + itemName + ChatColor.RESET + " out of the Crate: " + crate);
            }
        } else {
            String player = event.getPlayer().getDisplayName();
            String prize = event.getPrize().getDisplayItem().getItemMeta().getDisplayName();
            String crate = event.getCrateName();
            Main.getInstance().getLogger()
                    .info(player + ChatColor.RESET + " won " + prize + ChatColor.RESET + " out of the Crate: " + crate);
        }
        List<String> commands = event.getPrize().getCommands();
        if (commands.size() != 0) {
            String player = event.getPlayer().getDisplayName();
            Main.getInstance().getLogger().info(player + "'s prize had commands attached:");
        }
        for (String command : commands) {
            Main.getInstance().getLogger().info(command);
        }
    }
}
