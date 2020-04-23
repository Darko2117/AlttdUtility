package com.darko.main.cosmetics.chair;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class onStairsRightClick implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {

        Player player = e.getPlayer();

        if (ChairChecks.handCheck(player, e)) {
            if (ChairChecks.stairsCheck(player, e)) {
                if (ChairChecks.blocksCheck(player, e)) {
                    if (ChairChecks.occupiedCheck(e)) {
                        if (ChairChecks.claimCheck(player, e)) {
                            if (ChairChecks.regionCheck(player, e)) {

                                Location location = e.getClickedBlock().getLocation();
                                Stairs stairs = (Stairs) e.getClickedBlock().getBlockData();
                                BlockFace facing = stairs.getFacing();
                                Vector direction = null;
                                if (stairs.getFacing().equals(BlockFace.EAST)
                                        || stairs.getFacing().equals(BlockFace.WEST)) {
                                    direction = facing.getDirection().rotateAroundZ(180);
                                } else if (stairs.getFacing().equals(BlockFace.NORTH)
                                        || stairs.getFacing().equals(BlockFace.SOUTH)) {
                                    direction = facing.getDirection().rotateAroundX(180);
                                }
                                location.setDirection(direction);
                                ArmorStand chair = (ArmorStand) Bukkit.getWorld(player.getWorld().getName())
                                        .spawnEntity(location.clone().add(0.5, -1.25, 0.5), EntityType.ARMOR_STAND);
                                chair.setVisible(false);
                                chair.setInvulnerable(true);
                                chair.setCustomName(GlobalVariables.ChairName);
                                chair.setGravity(false);
                                chair.setBasePlate(false);
                                chair.addPassenger(player);

                                GlobalVariables.occupiedSeats.put(player.getUniqueId(),
                                        e.getClickedBlock().getLocation());
                                GlobalVariables.aliveSeats.put(e.getClickedBlock().getLocation(), chair);
                            }
                        }
                    }
                }
            }
        }
    }
}