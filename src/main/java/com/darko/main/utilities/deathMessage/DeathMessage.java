package com.darko.main.utilities.deathMessage;

import com.darko.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathMessage implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {

        if (e.getEntity().hasPermission("utility.deathmsg")) {
            e.getEntity().sendMessage(ChatColor.translateAlternateColorCodes('&',
                    Main.getInstance().getConfig().getString("Messages.DeathMessage")));
        }
    }
}
