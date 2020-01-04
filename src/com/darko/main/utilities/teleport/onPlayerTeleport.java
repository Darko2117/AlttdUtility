package com.darko.main.utilities.teleport;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class onPlayerTeleport implements Listener {

    @EventHandler
    public void onPlayerTeleportOnCertainBlocks(PlayerTeleportEvent e) {
        Material[] blocks = { Material.GRASS_PATH, Material.FARMLAND };
        for (Integer i = 0; i < blocks.length; i++) {
            if (blocks[i].equals(e.getTo().getBlock().getType())) {
                e.getTo().add(0, 0.5, 0);
            }
        }
    }
}
