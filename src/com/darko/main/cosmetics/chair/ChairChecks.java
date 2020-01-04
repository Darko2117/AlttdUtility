package com.darko.main.cosmetics.chair;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.darko.main.utilities.other.Flags;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

public class ChairChecks {

    public static Boolean handCheck(Player player, PlayerInteractEvent e) {

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK
                && player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            return true;
        }

        return false;
    }

    public static Boolean blocksCheck(Player player, PlayerInteractEvent e) {
        Location blockloc = e.getClickedBlock().getLocation();
        Material[] bannedMaterialsBelow = { Material.AIR, Material.LAVA, Material.FIRE };
        Material[] allowedMaterialsAbove = { Material.AIR, Material.ACACIA_WALL_SIGN, Material.BIRCH_WALL_SIGN,
                Material.DARK_OAK_WALL_SIGN, Material.JUNGLE_WALL_SIGN, Material.OAK_WALL_SIGN,
                Material.SPRUCE_WALL_SIGN, Material.ITEM_FRAME, Material.WALL_TORCH };
        Boolean bannedMaterialFound = false;
        Boolean allowedMaterialFound = false;

        for (Integer i = 0; i < bannedMaterialsBelow.length; i++) {
            if (bannedMaterialsBelow[i].equals(Bukkit.getWorld(player.getWorld().getName())
                    .getBlockAt(blockloc.clone().subtract(0, 1, 0)).getType())) {
                bannedMaterialFound = true;
            }
        }
        for (Integer i = 0; i < allowedMaterialsAbove.length; i++) {
            if (allowedMaterialsAbove[i].equals(
                    Bukkit.getWorld(player.getWorld().getName()).getBlockAt(blockloc.clone().add(0, 1, 0)).getType())) {
                allowedMaterialFound = true;
            }
        }

        if (!bannedMaterialFound && allowedMaterialFound) {
            return true;
        } else {
            player.sendMessage(ChatColor.RED + "Invalid block found above/below the stairs.");
        }
        return false;
    }

    public static Boolean stairsCheck(Player player, PlayerInteractEvent e) {
        String dataString = e.getClickedBlock().getBlockData().toString();
        if (dataString.contains("stairs") && GlobalVariables.chairEnabled.get(player.getUniqueId())
                && !dataString.contains("half=top") && !player.isGliding()
                && !GlobalVariables.occupiedSeats.containsValue(e.getClickedBlock().getLocation())) {
            return true;
        }
        return false;
    }

    public static Boolean occupiedCheck(PlayerInteractEvent e) {
        if (!GlobalVariables.occupiedSeats.containsValue(e.getClickedBlock().getLocation())) {
            return true;
        }
        return false;
    }

    public static Boolean claimCheck(Player player, PlayerInteractEvent e) {
        if (!com.darko.main.utilities.other.GlobalVariables.GriefPreventionFound) {
            return true;
        }
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(e.getClickedBlock().getLocation(), true, null);
        if (claim != null) {
            if (claim.allowAccess(player) == null) {
                return true;
            } else {
                player.sendMessage(
                        ChatColor.RED + "You don't have " + claim.getOwnerName() + "'s permission to use that.");
            }
        } else {
            return true;
        }
        return false;
    }

    public static Boolean regionCheck(Player player, PlayerInteractEvent e) {
        if (!com.darko.main.utilities.other.GlobalVariables.WorldGuardFound) {
            return true;
        }
        com.sk89q.worldedit.util.Location blockloc = BukkitAdapter.adapt(e.getClickedBlock().getLocation());
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        if (query.testState(blockloc, localPlayer, Flags.SIT)) {
            return true;
        } else {
            player.sendMessage(ChatColor.RED + "You can't sit in this region.");
        }
        return false;
    }
}
