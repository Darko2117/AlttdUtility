package com.darko.main.utilities.logging;

import com.darko.main.Main;
import me.badbones69.crazycrates.api.events.PlayerPrizeEvent;
import me.badbones69.crazycrates.api.objects.ItemBuilder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.Date;
import java.util.List;

public class LoggingCrazyCrates implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerPrize(PlayerPrizeEvent event) {

        if (!Main.getInstance().getConfig().getBoolean(Logging.logNamesAndConfigPaths.get(Logging.cratePrizesLogName) + ".Enabled"))
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String user = event.getPlayer().getName();

        String items;
        StringBuilder itemsStringBuilder = new StringBuilder();
        List<ItemBuilder> itemsItemBuilder = event.getPrize().getItemBuilders();
        if (itemsItemBuilder.size() != 0) {
            for (ItemBuilder item : itemsItemBuilder) {

                String amount = item.getAmount().toString();
                String itemMaterial = item.getMaterial().toString();
                String itemDisplayName = item.getName();

                if (!itemsStringBuilder.toString().isEmpty()) itemsStringBuilder.append(", ");

                itemsStringBuilder.append(amount).append("X").append(" ").append(itemMaterial).append(" (").append(itemDisplayName).append(")");

            }
        }
        items = itemsStringBuilder.toString();

        String commands;
        StringBuilder commandsStringBuilder = new StringBuilder();
        List<String> commandsList = event.getPrize().getCommands();
        if (commandsList.size() != 0) {
            for (String command : commandsList) {

                if (!commandsStringBuilder.toString().isEmpty()) commandsStringBuilder.append(", ");

                commandsStringBuilder.append(command);

            }
        }
        commands = commandsStringBuilder.toString();

        String crate = event.getCrateName();

        String message = "";
        message = message.concat("|");
        message = message.concat("Time:");
        message = message.concat(time);
        message = message.concat("|");
        message = message.concat("User:");
        message = message.concat(user);
        message = message.concat("|");
        message = message.concat("Items:");
        message = message.concat(items);
        message = message.concat("|");
        message = message.concat("Commands:");
        message = message.concat(commands);
        message = message.concat("|");
        message = message.concat("Crate:");
        message = message.concat(crate);
        message = message.concat("|");

        Logging.WriteToFile(Logging.cratePrizesLogName, message);

    }

}
