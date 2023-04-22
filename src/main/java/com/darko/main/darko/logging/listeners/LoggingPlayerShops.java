package com.darko.main.darko.logging.listeners;

import com.alttd.playershops.events.PlayerExchangeShopEvent;
import com.alttd.playershops.shop.PlayerShop;
import com.darko.main.darko.logging.Logging;
import com.darko.main.darko.logging.logs.ShopTransactionsLog;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.Date;

public class LoggingPlayerShops implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerExchangeShopEvent(PlayerExchangeShopEvent playerExchangeShopEvent) {

        if (!Logging.getCachedLogFromName("ShopTransactionsLog").isEnabled())
            return;

        PlayerShop playerShop = playerExchangeShopEvent.getShop();

        String time = new Date(System.currentTimeMillis()).toString();

        String ownerName = playerShop.getOwnerName();

        String customerName = playerExchangeShopEvent.getPlayer().getName();

        String shopType = playerShop.getType().toString();

        String location = Logging.getBetterLocationString(playerShop.getSignLocation());

        int amount = playerShop.getAmount();
        if (playerExchangeShopEvent.getPlayer().isSneaking())
            amount = playerShop.getItemStack().getMaxStackSize();
        String amountString = String.valueOf(amount);

        double price = amount * playerShop.getPricePerItem();
        String priceString = String.valueOf(price);

        String item = playerShop.getItemStack().toString();

        String shopBalance = String.valueOf(playerShop.getBalance());

        ShopTransactionsLog log = new ShopTransactionsLog();
        log.addArgumentValue(time);
        log.addArgumentValue(ownerName);
        log.addArgumentValue(customerName);
        log.addArgumentValue(shopType);
        log.addArgumentValue(location);
        log.addArgumentValue(amountString);
        log.addArgumentValue(priceString);
        log.addArgumentValue(item);
        log.addArgumentValue(shopBalance);

        Logging.addToLogWriteQueue(log);

    }

}
