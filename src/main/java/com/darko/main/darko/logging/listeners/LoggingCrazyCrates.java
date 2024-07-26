package com.darko.main.darko.logging.listeners;

import com.badbones69.crazycrates.api.events.PlayerPrizeEvent;
import com.darko.main.darko.logging.Logging;
import com.darko.main.darko.logging.logs.CratePrizesLog;
import libs.com.ryderbelserion.vital.paper.builders.items.ItemBuilder;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import scala.util.control.Exception;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import java.util.Date;
import java.util.List;

public class LoggingCrazyCrates implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerPrizeEvent(PlayerPrizeEvent event) {

        if (!Logging.getCachedLogFromName("CratePrizesLog").isEnabled())
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String user = event.getPlayer().getName();

        String items;
        StringBuilder itemsStringBuilder = new StringBuilder();

        List<ItemBuilder> itemsItemBuilder = event.getPrize().getItemBuilders();
        if (itemsItemBuilder.size() != 0) {
            for (ItemBuilder item : itemsItemBuilder) {

                String amount = String.valueOf(item.getStack().getAmount());
                String itemMaterial = item.getType().toString();
                String itemDisplayName = item.getDisplayName();

                if (!itemsStringBuilder.toString().isEmpty())
                    itemsStringBuilder.append(", ");

                itemsStringBuilder.append(amount).append("X").append(" ").append(itemMaterial).append(" (").append(itemDisplayName).append(")");

            }
        } else {

            ItemStack item = event.getPrize().getPrizeItem().getStack();

            String amount = String.valueOf(item.getAmount());
            String itemMaterial = item.getType().toString();
            String itemDisplayName = PlainTextComponentSerializer.plainText().serialize(item.displayName());

            if (!itemsStringBuilder.toString().isEmpty())
                itemsStringBuilder.append(", ");

            itemsStringBuilder.append(amount).append("X").append(" ").append(itemMaterial).append(" (").append(itemDisplayName).append(")");

        }

        items = itemsStringBuilder.toString();

        String commands;
        StringBuilder commandsStringBuilder = new StringBuilder();
        List<String> commandsList = event.getPrize().getCommands();
        if (commandsList.size() != 0) {
            for (String command : commandsList) {

                if (!commandsStringBuilder.toString().isEmpty())
                    commandsStringBuilder.append(", ");

                commandsStringBuilder.append(command);

            }
        }
        commands = commandsStringBuilder.toString();

        String crate = event.getCrateName();

        CratePrizesLog log = new CratePrizesLog();
        log.addArgumentValue(time);
        log.addArgumentValue(user);
        log.addArgumentValue(items);
        log.addArgumentValue(commands);
        log.addArgumentValue(crate);

        Logging.addToLogWriteQueue(log);

    }

}
