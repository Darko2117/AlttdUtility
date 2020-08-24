package com.darko.main.utilities.deathMessage;

import com.darko.main.Main;
import com.darko.main.other.Methods;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathMessage implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {

        if(!Main.getInstance().getConfig().getBoolean("FeatureToggles.DeathMessage")) return;

        if (event.getEntity().hasPermission("utility.deathmsg")) {
            Methods.sendConfigMessage(event.getEntity(), "Messages.DeathMessage");
        }

    }
}
