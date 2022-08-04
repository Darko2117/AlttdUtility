package com.darko.main.darko.databaseLogs;

import com.alttd.events.SpawnShopEvent;
import com.darko.main.AlttdUtility;
import com.darko.main.common.database.Database;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class DatabaseVillagerShopLog implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onSpawnShopEvent(SpawnShopEvent spawnShopEvent) {

        if (Database.connection == null) return;

        String userUUID = spawnShopEvent.player().getUniqueId().toString();

        String userUsername = spawnShopEvent.player().getName();

        String item = spawnShopEvent.item().toString();

        int itemAmount = spawnShopEvent.amount();

        double balanceChange = spawnShopEvent.price();
        if (spawnShopEvent.buy()) balanceChange *= -1;

        String type;
        if (spawnShopEvent.buy()) {
            type = "Buy";
        } else {
            type = "Sell";
        }

        long time = System.currentTimeMillis();

        String statement = "INSERT INTO villager_shop_log(user_UUID, user_username, item, item_amount, balance_change, type, time) VALUES("
                + "'" + userUUID + "', "
                + "'" + userUsername + "', "
                + "'" + item + "', "
                + itemAmount + ", "
                + balanceChange + ", "
                + "'" + type + "', "
                + time + ");";

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Database.connection.prepareStatement(statement).executeUpdate();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }.runTaskAsynchronously(AlttdUtility.getInstance());

    }

}
