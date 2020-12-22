package com.darko.main.temporary_holiday_presents;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PresentClaimingListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event){

        if(!event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "&2H&4a&2p&4p&2y &4H&2o&4l&2i&4d&2a&4y&2s&4!"))) return;

        event.setCancelled(true);

    }

}
