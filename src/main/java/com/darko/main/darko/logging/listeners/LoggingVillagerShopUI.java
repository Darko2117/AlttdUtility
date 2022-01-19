package com.darko.main.darko.logging.listeners;

import com.alttd.events.SpawnShopEvent;
import com.darko.main.darko.logging.Logging;
import com.darko.main.darko.logging.logs.VillagerShopUILog;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.Date;

public class LoggingVillagerShopUI implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSpawnShopEvent(SpawnShopEvent event) {

        if (!Logging.getCachedLogFromName("VillagerShopUILog").isEnabled()) return;

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

        VillagerShopUILog log = new VillagerShopUILog();
        log.addArgumentValue(time);
        log.addArgumentValue(user);
        log.addArgumentValue(amount);
        log.addArgumentValue(price);
        log.addArgumentValue(item);
        log.addArgumentValue(pointsBefore);
        log.addArgumentValue(pointsAfter);
        log.addArgumentValue(type);

        Logging.addToLogWriteQueue(log);

    }

}
