package com.darko.main.utilities.destro;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class LavaSponge implements Listener {

    @EventHandler
    public void onLavaTouchSponge(BlockFromToEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Block block = e.getBlock();
        World world = block.getWorld();
        int radius = 2;
        int blockX = block.getX();
        int blockY = block.getY();
        int blockZ = block.getZ();

        for (int fromX = -(radius + 1); fromX <= radius + 1; fromX++) {

            for (int fromY = -(radius + 1); fromY <= radius + 1; fromY++) {

                for (int fromZ = -(radius + 1); fromZ <= radius + 1; fromZ++) {

                    Block b = world.getBlockAt(blockX + fromX, blockY + fromY, blockZ + fromZ);
                    if (b.getType().equals(Material.SPONGE)) {
                        if (e.isCancelled())
                            return;
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Player player = e.getPlayer();
        if (e.getBlock().getType() == Material.SPONGE) {
            //TODO improve this code
            int radius = 5;//in theory we should check starting from the sponge and go outwards...
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        Location loc = new Location(player.getWorld(), (e.getBlock().getX() + x), (e.getBlock().getY() + y), (e.getBlock().getZ() + z));
                        if (loc.getBlock().getType() == Material.LAVA) {
                            loc.getBlock().setType(Material.AIR);
                        }
                    }
                }
            }
        }
    }
}
