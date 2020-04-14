package com.darko.main.utilities.durability;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class onDurabilityUse implements Listener {

    @EventHandler
    public void onDurability(PlayerItemDamageEvent e) {
        Player player = (Player) e.getPlayer();
        if (AutoFix.AutoFix.contains(player)) {
            Damageable durability = (Damageable) e.getItem().getItemMeta();
            durability.setDamage(0);
            e.getItem().setItemMeta((ItemMeta) durability);
            e.setCancelled(true);
        }
    }

}
