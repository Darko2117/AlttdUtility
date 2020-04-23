package com.darko.main.cosmetics.chair;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.darko.main.Main;
import com.darko.main.utilities.other.APIs;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.inventory.EquipmentSlot;

public class ChairChecks {

    public static Boolean handCheck(Player player, PlayerInteractEvent e) {

        return e.getAction() == Action.RIGHT_CLICK_BLOCK
                && player.getInventory().getItemInMainHand().getType() == Material.AIR && e.getHand().equals(EquipmentSlot.HAND);
    }

    public static Boolean blocksCheck(Player player, PlayerInteractEvent e) {

        Location blockloc = e.getClickedBlock().getLocation();
        Material[] bannedMaterialsBelow = {Material.LAVA, Material.FIRE};
        Material[] allowedMaterialsAbove = {Material.AIR, Material.ACACIA_WALL_SIGN, Material.BIRCH_WALL_SIGN,
                Material.DARK_OAK_WALL_SIGN, Material.JUNGLE_WALL_SIGN, Material.OAK_WALL_SIGN,
                Material.SPRUCE_WALL_SIGN, Material.WALL_TORCH};
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
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    Main.getInstance().getConfig().getString("Messages.InvalidChairBlock")));
        }
        return false;
    }

    public static Boolean stairsCheck(Player player, PlayerInteractEvent e) {
        String dataString = e.getClickedBlock().getBlockData().toString();
        return dataString.contains("stairs") && GlobalVariables.chairEnabled.contains(player)
                && !dataString.contains("half=top") && !player.isGliding()
                && !GlobalVariables.occupiedSeats.containsValue(e.getClickedBlock().getLocation());
    }

    public static Boolean occupiedCheck(PlayerInteractEvent e) {
        return !GlobalVariables.occupiedSeats.containsValue(e.getClickedBlock().getLocation());
    }

    public static Boolean claimCheck(Player player, PlayerInteractEvent e) {
        if (!APIs.GriefPreventionFound) {
            return true;
        }
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(e.getClickedBlock().getLocation(), true, null);
        if (claim != null) {
            if (claim.allowAccess(player) == null) {
                return true;
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig()
                        .getString("Messages.ChairNoClaimPerm").replace("%player%", claim.getOwnerName())));
            }
        } else {
            return true;
        }
        return false;
    }

    public static Boolean regionCheck(Player player, PlayerInteractEvent e) {
        if (!APIs.WorldGuardFound) {
            return true;
        }
        com.sk89q.worldedit.util.Location blockloc = BukkitAdapter.adapt(e.getClickedBlock().getLocation());
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        if (query.testState(blockloc, localPlayer, com.darko.main.utilities.flags.Flags.SIT)) {
            return true;
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    Main.getInstance().getConfig().getString("Messages.ChairNoRegionPerm")));
        }
        return false;
    }
}
