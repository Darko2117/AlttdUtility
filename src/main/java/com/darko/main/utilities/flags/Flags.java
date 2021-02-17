package com.darko.main.utilities.flags;

import com.darko.main.AlttdUtility;
import com.destroystokyo.paper.event.block.AnvilDamagedEvent;
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

    public static StateFlag SIT, ANVIL_REPAIR, ANVIL_USE, ENCHANTING_TABLE_USE, NAME_TAG_USE;

    public static void FlagsEnable() {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.CustomWorldGuardFlags")) return;

        SitFlag();
        AnvilRepairFlag();
        AnvilUseFlag();
        EnchantingTableUseFlag();
        NameTagFlag();

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
            StateFlag flag = new StateFlag("name-tag-use", true);
            registry.register(flag);
            NAME_TAG_USE = flag;
        } catch (Throwable throwable) {
            Flag<?> existing = registry.get("name-tag-use");
            if (existing instanceof StateFlag) {
                NAME_TAG_USE = (StateFlag) existing;
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onAnvilDamaged(AnvilDamagedEvent event){

        com.sk89q.worldedit.util.Location location = BukkitAdapter.adapt(event.getInventory().getLocation());

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(location);

        if (set.size() != 0) {

            Player player = (Player) event.getInventory().getViewers().get(0);

            LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
            RegionContainer container1 = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query1 = container1.createQuery();

            if (query1.testState(location, localPlayer, Flags.ANVIL_REPAIR)) {

                event.setDamageState(AnvilDamagedEvent.DamageState.FULL);

            }

        }

    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.HIGHEST)
    public void onEnchantingTableClick(PlayerInteractEvent event) {

        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (!event.getClickedBlock().getType().equals(Material.ENCHANTING_TABLE)) return;

        com.sk89q.worldedit.util.Location location = BukkitAdapter.adapt(event.getClickedBlock().getLocation());

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(location);

        if (set.size() != 0) {

            LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(event.getPlayer());
            RegionContainer container1 = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query1 = container1.createQuery();

            if (query1.testState(location, localPlayer, Flags.ENCHANTING_TABLE_USE))
                event.setUseInteractedBlock(Event.Result.ALLOW);

        }

    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.HIGHEST)
    public void onAnvilClick(PlayerInteractEvent event) {

        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (!event.getClickedBlock().getType().toString().toLowerCase().contains("anvil")) return;

        com.sk89q.worldedit.util.Location location = BukkitAdapter.adapt(event.getClickedBlock().getLocation());

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(location);

        if (set.size() != 0) {

            LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(event.getPlayer());
            RegionContainer container1 = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query1 = container1.createQuery();

            if (query1.testState(location, localPlayer, Flags.ANVIL_USE))
                event.setUseInteractedBlock(Event.Result.ALLOW);

        }

    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.HIGHEST)
    public void onNameTagUse(PlayerInteractEntityEvent event) {

        if (!event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.NAME_TAG) && !event.getPlayer().getInventory().getItemInOffHand().getType().equals(Material.NAME_TAG))
            return;

        com.sk89q.worldedit.util.Location location = BukkitAdapter.adapt(event.getRightClicked().getLocation());

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(location);

        if (set.size() != 0) {

            LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(event.getPlayer());
            RegionContainer container1 = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query1 = container1.createQuery();

            if (!query1.testState(location, localPlayer, Flags.NAME_TAG_USE))
                event.setCancelled(true);
        }

    }

}
