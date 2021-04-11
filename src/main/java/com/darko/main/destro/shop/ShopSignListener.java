package com.darko.main.destro.shop;

import com.alttd.destro174.shop.AbstractShop;
import com.alttd.destro174.shop.Shop;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class ShopSignListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onSignChange(SignChangeEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        if (!(block.getState() instanceof Sign)) return;

        AbstractShop shop = Shop.getPlugin().getShopHandler().getShop(block.getLocation());
        if (shop != null || !shop.isInitialized()) event.setCancelled(true);
    }
}
