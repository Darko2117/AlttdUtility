package com.darko.main.utilities.deathMessage;

import com.darko.main.Main;
import com.darko.main.other.Methods;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathMessage implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent event) {

        if (event.isCancelled()) return;
        if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.DeathMessage")) return;

        if (event.getEntity().hasPermission("utility.deathmsg")) {
            new Methods().sendConfigMessage(event.getEntity(), "Messages.DeathMessage");
        }

    }
}
