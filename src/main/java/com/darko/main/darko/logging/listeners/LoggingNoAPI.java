package com.darko.main.darko.logging.listeners;

import com.darko.main.AlttdUtility;
import com.darko.main.common.API.APIs;
import com.darko.main.common.BukkitTasksCache;
import com.darko.main.common.Methods;
import com.darko.main.darko.logging.Logging;
import com.darko.main.darko.logging.logs.ChatWithLocationLog;
import com.darko.main.darko.logging.logs.CommandsWithLocationLog;
import com.darko.main.darko.logging.logs.DroppedItemsLog;
import com.darko.main.darko.logging.logs.DroppedItemsOnDeathLog;
import com.darko.main.darko.logging.logs.EggsThrownLog;
import com.darko.main.darko.logging.logs.IllegalItemsLog;
import com.darko.main.darko.logging.logs.ItemsBrokenLog;
import com.darko.main.darko.logging.logs.ItemsDespawnedLog;
import com.darko.main.darko.logging.logs.ItemsDestroyedLog;
import com.darko.main.darko.logging.logs.ItemsPlacedInItemFramesLog;
import com.darko.main.darko.logging.logs.ItemsTakenOutOfItemFramesLog;
import com.darko.main.darko.logging.logs.LeadUsageLog;
import com.darko.main.darko.logging.logs.LightningStrikesLog;
import com.darko.main.darko.logging.logs.MCMMORepairUseLog;
import com.darko.main.darko.logging.logs.MinecartsDestroyedLog;
import com.darko.main.darko.logging.logs.MountingLog;
import com.darko.main.darko.logging.logs.PickedUpItemsLog;
import com.darko.main.darko.logging.logs.PlayerLocationLog;
import com.darko.main.darko.logging.logs.SpawnLimiterLog;
import com.darko.main.darko.logging.logs.TridentsLog;
import com.darko.main.darko.logging.logs.UIClicksLog;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityUnleashEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.entity.EntityDismountEvent;
import org.spigotmc.event.entity.EntityMountEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class LoggingNoAPI implements Listener {

    private static final HashSet<Item> damagedItemsDestroyedItemsLog = new HashSet<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerEggThrowEvent(PlayerEggThrowEvent event) {

        if (!Logging.getCachedLogFromName("EggsThrownLog").isEnabled())
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String user = event.getPlayer().getName();

        String location = Methods.getBetterLocationString(event.getEgg().getLocation());

        String claimOwner = "";
        if (APIs.isGriefPreventionFound()) {
            Claim claim = GriefPrevention.instance.dataStore.getClaimAt(event.getEgg().getLocation(), true, null);
            if (claim != null)
                claimOwner = claim.getOwnerName();
        }

        EggsThrownLog log = new EggsThrownLog();
        log.addArgumentValue(time);
        log.addArgumentValue(user);
        log.addArgumentValue(location);
        log.addArgumentValue(claimOwner);

        Logging.addToLogWriteQueue(log);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {

        if (!Logging.getCachedLogFromName("DroppedItemsLog").isEnabled())
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String user = event.getPlayer().getName();

        String item = event.getItemDrop().getItemStack().toString();

        String location = Methods.getBetterLocationString(event.getItemDrop().getLocation());

        DroppedItemsLog log = new DroppedItemsLog();
        log.addArgumentValue(time);
        log.addArgumentValue(user);
        log.addArgumentValue(item);
        log.addArgumentValue(location);

        Logging.addToLogWriteQueue(log);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {

        if (!Logging.getCachedLogFromName("ItemsPlacedInItemFramesLog").isEnabled())
            return;

        if (!(event.getRightClicked() instanceof ItemFrame))
            return;
        if (!((ItemFrame) event.getRightClicked()).getItem().getType().equals(Material.AIR))
            return;
        if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR))
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String user = event.getPlayer().getName();

        String item = event.getPlayer().getInventory().getItemInMainHand().toString();

        String location = Methods.getBetterLocationString(event.getRightClicked().getLocation());

        ItemsPlacedInItemFramesLog log = new ItemsPlacedInItemFramesLog();
        log.addArgumentValue(time);
        log.addArgumentValue(user);
        log.addArgumentValue(item);
        log.addArgumentValue(location);

        Logging.addToLogWriteQueue(log);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {

        if (!Logging.getCachedLogFromName("ItemsTakenOutOfItemFramesLog").isEnabled())
            return;

        if (!(event.getEntity() instanceof ItemFrame))
            return;
        if (((ItemFrame) event.getEntity()).getItem().getType().equals(Material.AIR))
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String user = event.getDamager().getName();

        String item = ((ItemFrame) event.getEntity()).getItem().toString();

        String location = Methods.getBetterLocationString(event.getEntity().getLocation());

        ItemsTakenOutOfItemFramesLog log = new ItemsTakenOutOfItemFramesLog();
        log.addArgumentValue(time);
        log.addArgumentValue(user);
        log.addArgumentValue(item);
        log.addArgumentValue(location);

        Logging.addToLogWriteQueue(log);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteractEvent(PlayerInteractEvent event) {

        if (!Logging.getCachedLogFromName("MCMMORepairUseLog").isEnabled())
            return;

        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;
        if (!event.getHand().equals(EquipmentSlot.HAND))
            return;
        if (!event.getClickedBlock().getType().equals(Material.IRON_BLOCK))
            return;
        if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR))
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String user = event.getPlayer().getName();

        String item = event.getPlayer().getInventory().getItemInMainHand().toString();

        MCMMORepairUseLog log = new MCMMORepairUseLog();
        log.addArgumentValue(time);
        log.addArgumentValue(user);
        log.addArgumentValue(item);

        Logging.addToLogWriteQueue(log);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityPickupItem(EntityPickupItemEvent event) {

        if (!Logging.getCachedLogFromName("PickedUpItemsLog").isEnabled())
            return;

        if (!(event.getEntity() instanceof Player))
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String user = event.getEntity().getName();

        String item = event.getItem().getItemStack().toString();

        String location = Methods.getBetterLocationString(event.getItem().getLocation());

        PickedUpItemsLog log = new PickedUpItemsLog();
        log.addArgumentValue(time);
        log.addArgumentValue(user);
        log.addArgumentValue(item);
        log.addArgumentValue(location);

        Logging.addToLogWriteQueue(log);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClickEvent(InventoryClickEvent event) {

        if (!Logging.getCachedLogFromName("UIClicksLog").isEnabled())
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String user = event.getWhoClicked().getName();

        String inventoryName = event.getView().getTitle();

        String clickedItem = "";
        if (event.getCurrentItem() != null && !event.getCurrentItem().getType().equals(Material.AIR))
            clickedItem = event.getCurrentItem().toString();

        String location = Methods.getBetterLocationString(event.getWhoClicked().getLocation());

        UIClicksLog log = new UIClicksLog();
        log.addArgumentValue(time);
        log.addArgumentValue(user);
        log.addArgumentValue(inventoryName);
        log.addArgumentValue(clickedItem);
        log.addArgumentValue(location);

        Logging.addToLogWriteQueue(log);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerItemBreakEvent(PlayerItemBreakEvent event) {

        if (!Logging.getCachedLogFromName("ItemsBrokenLog").isEnabled())
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String user = event.getPlayer().getName();

        String item = event.getBrokenItem().toString();

        String location = Methods.getBetterLocationString(event.getPlayer().getLocation());

        ItemsBrokenLog log = new ItemsBrokenLog();
        log.addArgumentValue(time);
        log.addArgumentValue(user);
        log.addArgumentValue(item);
        log.addArgumentValue(location);

        Logging.addToLogWriteQueue(log);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onItemDespawnEvent(ItemDespawnEvent event) {

        if (!Logging.getCachedLogFromName("ItemsDespawnedLog").isEnabled())
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String item = event.getEntity().getItemStack().toString();

        String location = Methods.getBetterLocationString(event.getLocation());

        ItemsDespawnedLog log = new ItemsDespawnedLog();
        log.addArgumentValue(time);
        log.addArgumentValue(item);
        log.addArgumentValue(location);

        Logging.addToLogWriteQueue(log);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageEvent(EntityDamageEvent event) {

        if (!Logging.getCachedLogFromName("ItemsDestroyedLog").isEnabled())
            return;

        if (!(event.getEntity() instanceof Item))
            return;
        if (((Item) event.getEntity()).getItemStack().getType().equals(Material.CACTUS))
            return;
        if (((Item) event.getEntity()).getItemStack().getType().equals(Material.COBBLESTONE))
            return;

        Item itemEntity = (Item) event.getEntity();

        damagedItemsDestroyedItemsLog.add(itemEntity);

        new BukkitRunnable() {
            public void run() {

                if (!itemEntity.isDead() || !damagedItemsDestroyedItemsLog.contains(itemEntity))
                    return;

                damagedItemsDestroyedItemsLog.remove(itemEntity);

                String time = new Date(System.currentTimeMillis()).toString();

                String item = itemEntity.getItemStack().toString();

                String location = Methods.getBetterLocationString(itemEntity.getLocation());

                String cause = event.getCause().toString();

                ItemsDestroyedLog log = new ItemsDestroyedLog();
                log.addArgumentValue(time);
                log.addArgumentValue(item);
                log.addArgumentValue(location);
                log.addArgumentValue(cause);

                Logging.addToLogWriteQueue(log);

            }
        }.runTaskLater(AlttdUtility.getInstance(), 1);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {

        if (!Logging.getCachedLogFromName("CommandsWithLocationLog").isEnabled())
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String user = event.getPlayer().getName();

        String command = event.getMessage();

        String location = Methods.getBetterLocationString(event.getPlayer().getLocation());

        CommandsWithLocationLog log = new CommandsWithLocationLog();
        log.addArgumentValue(time);
        log.addArgumentValue(user);
        log.addArgumentValue(command);
        log.addArgumentValue(location);

        Logging.addToLogWriteQueue(log);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeathEvent(PlayerDeathEvent event) {

        if (!Logging.getCachedLogFromName("DroppedItemsOnDeathLog").isEnabled())
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String user = event.getEntity().getName();

        String deathMessage = event.getDeathMessage();

        List<String> items = new ArrayList<>();
        for (ItemStack item : event.getDrops()) {
            items.add(item.toString());
        }

        String location = Methods.getBetterLocationString(event.getEntity().getLocation());

        for (String item : items) {

            DroppedItemsOnDeathLog log = new DroppedItemsOnDeathLog();
            log.addArgumentValue(time);
            log.addArgumentValue(user);
            log.addArgumentValue(deathMessage);
            log.addArgumentValue(item);
            log.addArgumentValue(location);

            Logging.addToLogWriteQueue(log);

        }

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onVehicleDestroyEvent(VehicleDestroyEvent event) {

        if (!Logging.getCachedLogFromName("MinecartsDestroyedLog").isEnabled())
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String attacker = "Not a player";
        try {
            attacker = event.getAttacker().getName();
        } catch (Throwable ignored) {
        }

        String location = Methods.getBetterLocationString(event.getVehicle().getLocation());

        String claimOwner = "";
        if (APIs.isGriefPreventionFound()) {
            Claim claim = GriefPrevention.instance.dataStore.getClaimAt(event.getVehicle().getLocation(), true, null);
            if (claim != null)
                claimOwner = claim.getOwnerName();
        }

        MinecartsDestroyedLog log = new MinecartsDestroyedLog();
        log.addArgumentValue(time);
        log.addArgumentValue(attacker);
        log.addArgumentValue(location);
        log.addArgumentValue(claimOwner);

        Logging.addToLogWriteQueue(log);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLightningStrikeEvent(LightningStrikeEvent event) {

        if (!Logging.getCachedLogFromName("LightningStrikesLog").isEnabled())
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String location = Methods.getBetterLocationString(event.getLightning().getLocation());

        String cause = event.getCause().toString();

        LightningStrikesLog log = new LightningStrikesLog();
        log.addArgumentValue(time);
        log.addArgumentValue(cause);
        log.addArgumentValue(location);

        Logging.addToLogWriteQueue(log);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onProjectileLaunchEvent(ProjectileLaunchEvent event) {

        if (!Logging.getCachedLogFromName("TridentsLog").isEnabled())
            return;

        if (!event.getEntity().getType().equals(EntityType.TRIDENT))
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String player;
        if (event.getEntity().getShooter() instanceof Player) {
            player = ((Player) event.getEntity().getShooter()).getName();
        } else {
            player = event.getEntity().getShooter().toString();
        }

        String trident = ((Trident) event.getEntity()).getItem().toString();

        String location = Methods.getBetterLocationString(event.getEntity().getLocation());

        String action = "THROW";

        String target = "";

        TridentsLog log = new TridentsLog();
        log.addArgumentValue(time);
        log.addArgumentValue(player);
        log.addArgumentValue(trident);
        log.addArgumentValue(location);
        log.addArgumentValue(action);
        log.addArgumentValue(target);

        Logging.addToLogWriteQueue(log);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerPickupArrowEvent(PlayerPickupArrowEvent event) {

        if (!Logging.getCachedLogFromName("TridentsLog").isEnabled())
            return;

        if (!event.getArrow().getType().equals(EntityType.TRIDENT))
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String player = event.getPlayer().getName();

        String trident = event.getItem().getItemStack().toString();

        String location = Methods.getBetterLocationString(event.getArrow().getLocation());

        String action = "PICKUP";

        String target = "";

        TridentsLog log = new TridentsLog();
        log.addArgumentValue(time);
        log.addArgumentValue(player);
        log.addArgumentValue(trident);
        log.addArgumentValue(location);
        log.addArgumentValue(action);
        log.addArgumentValue(target);

        Logging.addToLogWriteQueue(log);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageByEntityEvent1(EntityDamageByEntityEvent event) {

        if (!Logging.getCachedLogFromName("TridentsLog").isEnabled())
            return;

        if (!event.getDamager().getType().equals(EntityType.TRIDENT))
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String player;
        if (((Trident) event.getDamager()).getShooter() instanceof Player) {
            player = ((Player) ((Trident) event.getDamager()).getShooter()).getName();
        } else {
            player = ((Trident) event.getDamager()).getShooter().toString();
        }

        String trident = ((Trident) event.getDamager()).getItem().toString();

        String location = Methods.getBetterLocationString(event.getEntity().getLocation());

        String action = "HIT";

        String target;
        if (event.getEntity() instanceof Player) {
            if (event.getEntity().getCustomName() != null) {
                target = event.getEntity().getCustomName();
            } else {
                target = event.getEntity().getName();
            }
        } else {
            target = event.getEntity().getType().toString();
        }

        TridentsLog log = new TridentsLog();
        log.addArgumentValue(time);
        log.addArgumentValue(player);
        log.addArgumentValue(trident);
        log.addArgumentValue(location);
        log.addArgumentValue(action);
        log.addArgumentValue(target);

        Logging.addToLogWriteQueue(log);

    }

    public static void startPlayerLocationLog() {
        BukkitTasksCache.addTask(new BukkitRunnable() {
            @Override
            public void run() {

                if (!Logging.getCachedLogFromName("PlayerLocationLog").isEnabled())
                    return;

                for (Player player : Bukkit.getOnlinePlayers()) {

                    String time = new Date(System.currentTimeMillis()).toString();

                    String playerName = player.getName();

                    String location = Methods.getBetterLocationString(player.getLocation());

                    PlayerLocationLog log = new PlayerLocationLog();
                    log.addArgumentValue(time);
                    log.addArgumentValue(playerName);
                    log.addArgumentValue(location);

                    Logging.addToLogWriteQueue(log);

                }

            }
        }.runTaskTimer(AlttdUtility.getInstance(), 1, 200));
    }

    public static void logCancelledSpawn(EntitySpawnEvent event) {

        if (!Logging.getCachedLogFromName("SpawnLimiterLog").isEnabled())
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String entityType = event.getEntityType().toString();

        String location = Methods.getBetterLocationString(event.getLocation());

        String claimOwner = "";
        if (APIs.isGriefPreventionFound()) {
            Claim claim = GriefPrevention.instance.dataStore.getClaimAt(event.getLocation(), true, null);
            if (claim != null)
                claimOwner = claim.getOwnerName();
        }

        SpawnLimiterLog log = new SpawnLimiterLog();
        log.addArgumentValue(time);
        log.addArgumentValue(entityType);
        log.addArgumentValue(location);
        log.addArgumentValue(claimOwner);

        Logging.addToLogWriteQueue(log);

    }

    public static void logIllegalItems(ItemStack itemStack, ItemStack replacedWithItem, Player player, Event event) {

        if (!Logging.getCachedLogFromName("IllegalItemsLog").isEnabled())
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String player1 = player.getName();

        String item = itemStack.toString();

        String replacedWithItemString = replacedWithItem.toString();

        String location = Methods.getBetterLocationString(player.getLocation());

        String eventName = event.getEventName();

        IllegalItemsLog log = new IllegalItemsLog();
        log.addArgumentValue(time);
        log.addArgumentValue(player1);
        log.addArgumentValue(item);
        log.addArgumentValue(replacedWithItemString);
        log.addArgumentValue(location);
        log.addArgumentValue(eventName);

        Logging.addToLogWriteQueue(log);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAsyncChatEvent(AsyncChatEvent event) {

        if (!Logging.getCachedLogFromName("ChatWithLocationLog").isEnabled())
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String user = event.getPlayer().getName();

        Component message = event.message();
        String messageString;
        boolean messageIsJustContent = false;
        if (message instanceof TextComponent && !message.hasStyling() && message.children().isEmpty()) {
            messageString = ((TextComponent) message).content();
            messageIsJustContent = true;
        } else {
            messageString = message.toString();
        }

        String originalMessageString = messageIsJustContent ? "" : event.originalMessage().toString();

        String location = Methods.getBetterLocationString(event.getPlayer().getLocation());

        ChatWithLocationLog log = new ChatWithLocationLog();
        log.addArgumentValue(time);
        log.addArgumentValue(user);
        log.addArgumentValue(messageString);
        log.addArgumentValue(originalMessageString);
        log.addArgumentValue(location);

        Logging.addToLogWriteQueue(log);

    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityMount(EntityMountEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();
        Entity mount = event.getMount();

        String time = new Date().toString();
        String user = player.getName();
        String mountType = mount.getType().toString();
        String location = Methods.getBetterLocationString(mount.getLocation());

        MountingLog log = new MountingLog();
        log.addArgumentValue(time);
        log.addArgumentValue(user);
        log.addArgumentValue(mountType);
        log.addArgumentValue("MOUNT");
        log.addArgumentValue(location);

        Logging.addToLogWriteQueue(log);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDismount(EntityDismountEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();
        Entity dismounted = event.getDismounted();

        String time = new Date().toString();
        String user = player.getName();
        String dismountedType = dismounted.getType().toString();
        String location = Methods.getBetterLocationString(dismounted.getLocation());

        MountingLog log = new MountingLog();
        log.addArgumentValue(time);
        log.addArgumentValue(user);
        log.addArgumentValue(dismountedType);
        log.addArgumentValue("DISMOUNT");
        log.addArgumentValue(location);

        Logging.addToLogWriteQueue(log);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLeashEntityEvent(PlayerLeashEntityEvent event) {

        if (!Logging.getCachedLogFromName("LeadUsageLog").isEnabled())
            return;

        String time = new Date(System.currentTimeMillis()).toString();
        String user = event.getPlayer().getName();
        String entity = event.getEntity().getType().toString();
        String location = Methods.getBetterLocationString(event.getEntity().getLocation());

        String action;
        if (!event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.LEAD)) {
            action = "TIED_TO_FENCE";
        } else {
            action = "LEASHED";
        }

        LeadUsageLog log = new LeadUsageLog();
        log.addArgumentValue(time);
        log.addArgumentValue(user);
        log.addArgumentValue(entity);
        log.addArgumentValue(action);
        log.addArgumentValue(location);

        Logging.addToLogWriteQueue(log);
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onLeadTiedToFence(PlayerInteractEvent event) {

        if (!Logging.getCachedLogFromName("LeadUsageLog").isEnabled())
            return;

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if (event.getClickedBlock() == null || !event.getClickedBlock().getType().name().contains("FENCE"))
            return;

        Player player = event.getPlayer();
        if (player.getInventory().getItemInMainHand().getType() != Material.LEAD)
            return;

        List<String> entityTypes = new ArrayList<>();
        for (LivingEntity entity : player.getWorld().getLivingEntities()) {
            if (entity.isLeashed() && player.equals(entity.getLeashHolder())) {
                entityTypes.add(entity.getType().toString());
            }
        }

        if (entityTypes.isEmpty())
            return;

        String time = new Date(System.currentTimeMillis()).toString();
        String user = player.getName();
        String action = "TIED_TO_FENCE";
        String location = Methods.getBetterLocationString(event.getClickedBlock().getLocation());

        for (String entityType : entityTypes) {
            LeadUsageLog log = new LeadUsageLog();
            log.addArgumentValue(time);
            log.addArgumentValue(user);
            log.addArgumentValue(entityType);
            log.addArgumentValue(action);
            log.addArgumentValue(location);

            Logging.addToLogWriteQueue(log);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityUnleashed(EntityUnleashEvent event) {

        if (!Logging.getCachedLogFromName("LeadUsageLog").isEnabled())
            return;

        if (event.getReason() != EntityUnleashEvent.UnleashReason.PLAYER_UNLEASH)
            return;

        String time = new Date(System.currentTimeMillis()).toString();
        String entity = event.getEntity().getType().toString();
        String location = Methods.getBetterLocationString(event.getEntity().getLocation());

        LeadUsageLog log = new LeadUsageLog();
        log.addArgumentValue(time);
        log.addArgumentValue("UNKNOWN");
        log.addArgumentValue(entity);
        log.addArgumentValue("UNLEASHED");
        log.addArgumentValue(location);

        Logging.addToLogWriteQueue(log);
    }

}
