package com.darko.main.utilities.teleport;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.List;

public class onPlayerTeleport implements Listener {

    @EventHandler
    public void onPlayerTeleportOnCertainBlocks(PlayerTeleportEvent event) {

        List<Material> blocks = new ArrayList<>();
        blocks.add(Material.GRASS_PATH);
        blocks.add(Material.FARMLAND);

        for (Material material : blocks) {
            if (material.equals(event.getTo().getBlock().getType())) {
                event.getTo().add(0, 0.5, 0);
            }
        }

    }

}
