package com.darko.main.teri.CrazyCratesKeysLimiter;

import com.badbones69.crazycrates.api.events.PlayerReceiveKeyEvent;
import com.badbones69.crazycrates.api.objects.Crate;
import com.darko.main.AlttdUtility;
import com.darko.main.common.Methods;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class CrazyCratesKeysLimiter implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPlayerReceiveKeyEvent(PlayerReceiveKeyEvent event) {

        if (event.getPlayer().hasPermission("utility.bypass-key-limit")) return;

        Crate crate = event.getCrate();
        Player player = event.getPlayer();

        int keyLimit = AlttdUtility.getInstance().getConfig().getInt("CrazyCratesKeysLimiter." + crate.getName() + ".KeyLimit");
        if (keyLimit == 0) return;

        int playerKeysAfterEvent = com.badbones69.crazycrates.api.CrazyManager.getInstance().getVirtualKeys(player, crate) + event.getAmount();

        if (playerKeysAfterEvent == keyLimit - 1) {
            new Methods().sendConfigMessage(player, "Messages.CrazyCratesKeysLimiterAtLimitMinusOne");
        } else if (playerKeysAfterEvent == keyLimit) {
            new Methods().sendConfigMessage(player, "Messages.CrazyCratesKeysLimiterAtLimit");
        } else if (playerKeysAfterEvent > keyLimit) {
            new Methods().sendConfigMessage(player, "Messages.CrazyCratesKeysLimiterOverLimit");
            event.setCancelled(true);
        }

    }

}