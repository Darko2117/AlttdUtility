package com.darko.main.darko.logging;

import com.alttd.events.SpawnShopEvent;
import com.darko.main.AlttdUtility;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.Date;

public class LoggingVillagerShopUI implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSpawnShop(SpawnShopEvent event) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean(Logging.logNamesAndConfigPaths.get(Logging.spawnShopLogName) + ".Enabled"))
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String user = event.player().getName();

        String amount = String.valueOf(event.amount());

        String price = String.valueOf(event.price());

        String item = event.item().toString();

        String pointsBefore = String.valueOf(event.pointsBefore());

        String pointsAfter = String.valueOf(event.pointsAfter());

        String type;
        if (event.buy()) {
            type = "Buy";
        } else {
            type = "Sell";
        }

        String message = "";
        message = message.concat("|");
        message = message.concat("Time:");
        message = message.concat(time);
        message = message.concat("|");
        message = message.concat("User:");
        message = message.concat(user);
        message = message.concat("|");
        message = message.concat("Amount:");
        message = message.concat(amount);
        message = message.concat("|");
        message = message.concat("Price:");
        message = message.concat(price);
        message = message.concat("|");
        message = message.concat("Item:");
        message = message.concat(item);
        message = message.concat("|");
        message = message.concat("PointsBefore:");
        message = message.concat(pointsBefore);
        message = message.concat("|");
        message = message.concat("PointsAfter:");
        message = message.concat(pointsAfter);
        message = message.concat("|");
        message = message.concat("Type:");
        message = message.concat(type);
        message = message.concat("|");

        Logging.addToLogWriteQueue(Logging.spawnShopLogName, message);

    }

}
