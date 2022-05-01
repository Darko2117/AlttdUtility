package com.darko.main.darko.blockBlockPlace;

import com.darko.main.AlttdUtility;
import com.darko.main.common.Methods;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;
import java.util.List;

public class BlockBlockPlace implements Listener {

    private static final List<Material> blockedMaterials = new ArrayList<>();

    public static void initiate() {

        blockedMaterials.clear();

        for (String material : AlttdUtility.getInstance().getConfig().getStringList("BlockBlockPlace.BlockedBlocks")) {
            try {
                blockedMaterials.add(Material.getMaterial(material.toUpperCase()));
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {

        if (event.getPlayer().hasPermission("utility.blockedblocksbypass")) return;

        if (blockedMaterials.contains(event.getBlockPlaced().getType())) {
            event.setCancelled(true);
            new Methods().sendConfigMessage(event.getPlayer(), "Messages.BlockedBlocksCantPlace");
        }

    }

}
