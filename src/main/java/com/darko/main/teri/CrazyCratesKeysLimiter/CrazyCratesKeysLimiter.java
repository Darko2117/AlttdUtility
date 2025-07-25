package com.darko.main.teri.CrazyCratesKeysLimiter;

import com.badbones69.crazycrates.paper.CrazyCrates;
import com.badbones69.crazycrates.paper.api.events.PlayerReceiveKeyEvent;
import com.badbones69.crazycrates.paper.api.objects.Crate;
import com.darko.main.AlttdUtility;
import com.darko.main.common.Methods;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class CrazyCratesKeysLimiter implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPlayerReceiveKeyEvent(PlayerReceiveKeyEvent event) {

        if (event.getPlayer().hasPermission("utility.bypass-key-limit"))
            return;

        Crate crate = event.getCrate();
        Player player = event.getPlayer();

        int keyLimit = AlttdUtility.getInstance().getConfig().getInt("CrazyCratesKeysLimiter." + crate.getCrateName() + ".KeyLimit");
        if (keyLimit == 0)
            return;

        int playerKeysAfterEvent = CrazyCrates.getPlugin(CrazyCrates.class).getUserManager().getVirtualKeys(player.getUniqueId(), crate.getCrateName()) + event.getAmount();

        if (playerKeysAfterEvent == keyLimit - 1) {
            Methods.sendConfigMessage(player, "Messages.CrazyCratesKeysLimiterAtLimitMinusOne");
        } else if (playerKeysAfterEvent == keyLimit) {
            Methods.sendConfigMessage(player, "Messages.CrazyCratesKeysLimiterAtLimit");
        } else if (playerKeysAfterEvent > keyLimit) {
            Methods.sendConfigMessage(player, "Messages.CrazyCratesKeysLimiterOverLimit");
            event.setCancelled(true);
        }

    }

}
