package com.darko.main.utilities.flags;

import com.darko.main.Main;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Flags implements Listener {

    public static StateFlag SIT, ANVIL_REPAIR, ANVIL_USE, ENCHANTING_TABLE_USE, NAME_TAG_USE, BONE_MEAL_USE;

    public static void FlagsEnable() {

        if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.CustomWorldGuardFlags")) return;

        SitFlag();
        AnvilRepairFlag();
        AnvilUseFlag();
        EnchantingTableUseFlag();
        NameTagFlag();
        BoneMealFlag();

    }

    private static void SitFlag() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            StateFlag flag = new StateFlag("chair-sit", true);
            registry.register(flag);
            SIT = flag;
        } catch (Throwable throwable) {
            Flag<?> existing = registry.get("chair-sit");
            if (existing instanceof StateFlag) {
                SIT = (StateFlag) existing;
            }
        }
    }

    private static void AnvilRepairFlag() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            StateFlag flag = new StateFlag("anvil-repair", false);
            registry.register(flag);
            ANVIL_REPAIR = flag;
        } catch (Throwable throwable) {
            Flag<?> existing = registry.get("anvil-repair");
            if (existing instanceof StateFlag) {
                ANVIL_REPAIR = (StateFlag) existing;
            }
        }
    }

    private static void AnvilUseFlag() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            StateFlag flag = new StateFlag("anvil-use", false);
            registry.register(flag);
            ANVIL_USE = flag;
        } catch (Throwable throwable) {
            Flag<?> existing = registry.get("anvil-use");
            if (existing instanceof StateFlag) {
                ANVIL_USE = (StateFlag) existing;
            }
        }
    }

    private static void EnchantingTableUseFlag() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            StateFlag flag = new StateFlag("enchanting-table-use", false);
            registry.register(flag);
            ENCHANTING_TABLE_USE = flag;
        } catch (Throwable throwable) {
            Flag<?> existing = registry.get("enchanting-table-use");
            if (existing instanceof StateFlag) {
                ENCHANTING_TABLE_USE = (StateFlag) existing;
            }
        }
    }

    private static void NameTagFlag() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            StateFlag flag = new StateFlag("name-tag-use", false);
            registry.register(flag);
            NAME_TAG_USE = flag;
        } catch (Throwable throwable) {
            Flag<?> existing = registry.get("name-tag-use");
            if (existing instanceof StateFlag) {
                NAME_TAG_USE = (StateFlag) existing;
            }
        }
    }

    private static void BoneMealFlag() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            StateFlag flag = new StateFlag("bone-meal-use", false);
            registry.register(flag);
            BONE_MEAL_USE = flag;
        } catch (Throwable throwable) {
            Flag<?> existing = registry.get("bone-meal-use");
            if (existing instanceof StateFlag) {
                BONE_MEAL_USE = (StateFlag) existing;
            }
        }
    }

    @EventHandler
    public void onDamagedAnvilClick(PlayerInteractEvent event) {

        if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.CustomWorldGuardFlags")) return;

        Player player = event.getPlayer();

        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && (event.getClickedBlock().getType() == Material.ANVIL
                || event.getClickedBlock().getType() == Material.CHIPPED_ANVIL
                || event.getClickedBlock().getType() == Material.DAMAGED_ANVIL)) {

            com.sk89q.worldedit.util.Location location = BukkitAdapter.adapt(event.getClickedBlock().getLocation());

            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = container.createQuery();
            ApplicableRegionSet set = query.getApplicableRegions(location);

            if (set.size() != 0) {

                LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
                RegionContainer container1 = WorldGuard.getInstance().getPlatform().getRegionContainer();
                RegionQuery query1 = container1.createQuery();

                if (query1.testState(location, localPlayer, Flags.ANVIL_REPAIR)) {

                    String dataString = event.getClickedBlock().getBlockData().getAsString();
                    if (dataString.contains("chipped_")) {
                        dataString = dataString.replace("chipped_", "");
                    } else if (dataString.contains("damaged_")) {
                        dataString = dataString.replace("damaged_", "");
                    }

                    event.getClickedBlock().setBlockData(Bukkit.createBlockData(dataString));

                }

            }

        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBoneMealUse(PlayerInteractEvent event) {

        if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.CustomWorldGuardFlags")) return;

        Player player = event.getPlayer();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK
                && player.getInventory().getItemInMainHand().getType() == Material.BONE_MEAL) {

            com.sk89q.worldedit.util.Location location = BukkitAdapter.adapt(event.getClickedBlock().getLocation());

            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = container.createQuery();
            ApplicableRegionSet set = query.getApplicableRegions(location);

            if (set.size() != 0) {

                LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
                RegionContainer container1 = WorldGuard.getInstance().getPlatform().getRegionContainer();
                RegionQuery query1 = container1.createQuery();

                if (!query1.testState(location, localPlayer, Flags.BONE_MEAL_USE)) {
                    if (!player.hasPermission("utility.bonemeal.bypass")) {
                        event.setCancelled(true);
                    }
                }

            }

        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEnchantingTableClick(PlayerInteractEvent event) {

        if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.CustomWorldGuardFlags")) return;

        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                && (event.getClickedBlock().getType() == Material.ENCHANTING_TABLE)) {

            Player player = event.getPlayer();
            com.sk89q.worldedit.util.Location location = BukkitAdapter.adapt(event.getClickedBlock().getLocation());

            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = container.createQuery();
            ApplicableRegionSet set = query.getApplicableRegions(location);

            if (set.size() != 0) {

                LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
                RegionContainer container1 = WorldGuard.getInstance().getPlatform().getRegionContainer();
                RegionQuery query1 = container1.createQuery();

                query1.testState(location, localPlayer, Flags.ENCHANTING_TABLE_USE);

                if (event.useInteractedBlock() == Event.Result.DENY) {
                    event.setUseInteractedBlock(Event.Result.ALLOW);
                }

            }

        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAnvilClick(PlayerInteractEvent event) {

        if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.CustomWorldGuardFlags")) return;

        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                && ((event.getClickedBlock().getType() == Material.ANVIL || event.getClickedBlock().getType() == Material.CHIPPED_ANVIL || event.getClickedBlock().getType() == Material.DAMAGED_ANVIL))) {

            Player player = event.getPlayer();
            com.sk89q.worldedit.util.Location location = BukkitAdapter.adapt(event.getClickedBlock().getLocation());

            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = container.createQuery();
            ApplicableRegionSet set = query.getApplicableRegions(location);

            if (set.size() != 0) {

                LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
                RegionContainer container1 = WorldGuard.getInstance().getPlatform().getRegionContainer();
                RegionQuery query1 = container1.createQuery();

                query1.testState(location, localPlayer, Flags.ANVIL_USE);

                if (event.useInteractedBlock() == Event.Result.DENY) {
                    event.setUseInteractedBlock(Event.Result.ALLOW);
                }

            }

        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onNameTagUse(PlayerInteractEntityEvent event) {

        if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.CustomWorldGuardFlags")) return;

        Player player = event.getPlayer();

        if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.NAME_TAG
                || event.getPlayer().getInventory().getItemInOffHand().getType() == Material.NAME_TAG) {

            com.sk89q.worldedit.util.Location location = BukkitAdapter.adapt(event.getRightClicked().getLocation());

            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = container.createQuery();
            ApplicableRegionSet set = query.getApplicableRegions(location);

            if (set.size() != 0) {
                LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
                RegionContainer container1 = WorldGuard.getInstance().getPlatform().getRegionContainer();
                RegionQuery query1 = container1.createQuery();

                if (!query1.testState(location, localPlayer, Flags.NAME_TAG_USE)) {
                    if (!player.hasPermission("utility.nametag.bypass")) {
                        event.setCancelled(true);
                    }
                }

            }

        }

    }

}
