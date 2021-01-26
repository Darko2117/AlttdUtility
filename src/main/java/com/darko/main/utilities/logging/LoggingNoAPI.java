package com.darko.main.utilities.logging;

import com.darko.main.API.APIs;
import com.darko.main.AlttdUtility;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Date;

public class LoggingNoAPI implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerEggThrow(PlayerEggThrowEvent event) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean(Logging.logNamesAndConfigPaths.get(Logging.eggsThrownLogName) + ".Enabled"))
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String user = event.getPlayer().getName();

        String location = Logging.getBetterLocationString(event.getEgg().getLocation());

        String claimOwner = "";
        if (APIs.GriefPreventionFound) {
            Claim claim = GriefPrevention.instance.dataStore.getClaimAt(event.getEgg().getLocation(), true, null);
            if (claim != null) claimOwner = claim.getOwnerName();
        }

        String message = "";
        message = message.concat("|");
        message = message.concat("Time:");
        message = message.concat(time);
        message = message.concat("|");
        message = message.concat("User:");
        message = message.concat(user);
        message = message.concat("|");
        message = message.concat("Location:");
        message = message.concat(location);
        message = message.concat("|");
        message = message.concat("ClaimOwner:");
        message = message.concat(claimOwner);
        message = message.concat("|");

        Logging.WriteToFile(Logging.eggsThrownLogName, message);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDropItem(PlayerDropItemEvent event) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean(Logging.logNamesAndConfigPaths.get(Logging.droppedItemsLogName) + ".Enabled"))
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String user = event.getPlayer().getName();

        String item = event.getItemDrop().getItemStack().toString();
//        if (item.length() > 1000) {
//            item = item.substring(0, 1000);
//            item = item.concat(" Item string too long, ending at 1000 characters.");
//        }

        String location = Logging.getBetterLocationString(event.getItemDrop().getLocation());

        String message = "";
        message = message.concat("|");
        message = message.concat("Time:");
        message = message.concat(time);
        message = message.concat("|");
        message = message.concat("User:");
        message = message.concat(user);
        message = message.concat("|");
        message = message.concat("Item:");
        message = message.concat(item);
        message = message.concat("|");
        message = message.concat("Location:");
        message = message.concat(location);
        message = message.concat("|");

        Logging.WriteToFile(Logging.droppedItemsLogName, message);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {

        if (event.getRightClicked() instanceof ItemFrame) {
            if (((ItemFrame) event.getRightClicked()).getItem().getType().equals(Material.AIR)) {
                if (!event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)) {

                    if (!AlttdUtility.getInstance().getConfig().getBoolean(Logging.logNamesAndConfigPaths.get(Logging.itemsPlacedInItemFramesLogName) + ".Enabled"))
                        return;

                    String time = new Date(System.currentTimeMillis()).toString();

                    String user = event.getPlayer().getName();

                    String item = event.getPlayer().getInventory().getItemInMainHand().toString();
//                    if (item.length() > 1000) {
//                        item = item.substring(0, 1000);
//                        item = item.concat(" Item string too long, ending at 1000 characters.");
//                    }

                    String location = Logging.getBetterLocationString(event.getRightClicked().getLocation());

                    String message = "";
                    message = message.concat("|");
                    message = message.concat("Time:");
                    message = message.concat(time);
                    message = message.concat("|");
                    message = message.concat("User:");
                    message = message.concat(user);
                    message = message.concat("|");
                    message = message.concat("Item:");
                    message = message.concat(item);
                    message = message.concat("|");
                    message = message.concat("Location:");
                    message = message.concat(location);
                    message = message.concat("|");

                    Logging.WriteToFile(Logging.itemsPlacedInItemFramesLogName, message);

                }
            }
        }

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        if (event.getEntity() instanceof ItemFrame) {
            if (!((ItemFrame) event.getEntity()).getItem().getType().equals(Material.AIR)) {

                if (!AlttdUtility.getInstance().getConfig().getBoolean(Logging.logNamesAndConfigPaths.get(Logging.itemsTakenOutOfItemFramesLogName) + ".Enabled"))
                    return;

                String time = new Date(System.currentTimeMillis()).toString();

                String user = event.getDamager().getName();

                String item = ((ItemFrame) event.getEntity()).getItem().toString();
//                if (item.length() > 1000) {
//                    item = item.substring(0, 1000);
//                    item = item.concat(" Item string too long, ending at 1000 characters.");
//                }

                String location = Logging.getBetterLocationString(event.getEntity().getLocation());

                String message = "";
                message = message.concat("|");
                message = message.concat("Time:");
                message = message.concat(time);
                message = message.concat("|");
                message = message.concat("User:");
                message = message.concat(user);
                message = message.concat("|");
                message = message.concat("Item:");
                message = message.concat(item);
                message = message.concat("|");
                message = message.concat("Location:");
                message = message.concat(location);
                message = message.concat("|");

                Logging.WriteToFile(Logging.itemsTakenOutOfItemFramesLogName, message);

            }
        }

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (event.getHand().equals(EquipmentSlot.HAND)) {
                if (event.getClickedBlock().getType().equals(Material.IRON_BLOCK)) {
                    if (!event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)) {

                        if (!AlttdUtility.getInstance().getConfig().getBoolean(Logging.logNamesAndConfigPaths.get(Logging.mcmmoRepairUseLogName) + ".Enabled"))
                            return;

                        String time = new Date(System.currentTimeMillis()).toString();

                        String user = event.getPlayer().getName();

                        String item = event.getPlayer().getInventory().getItemInMainHand().toString();
//                        if (item.length() > 1000) {
//                            item = item.substring(0, 1000);
//                            item = item.concat(" Item string too long, ending at 1000 characters.");
//                        }

                        String message = "";
                        message = message.concat("|");
                        message = message.concat("Time:");
                        message = message.concat(time);
                        message = message.concat("|");
                        message = message.concat("User:");
                        message = message.concat(user);
                        message = message.concat("|");
                        message = message.concat("Item:");
                        message = message.concat(item);
                        message = message.concat("|");

                        Logging.WriteToFile(Logging.mcmmoRepairUseLogName, message);

                    }

                }
            }
        }

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityPickupItem(EntityPickupItemEvent event) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean(Logging.logNamesAndConfigPaths.get(Logging.pickedUpItemsLogName) + ".Enabled"))
            return;

        if (!(event.getEntity() instanceof Player)) return;

        String time = new Date(System.currentTimeMillis()).toString();

        String user = event.getEntity().getName();

        String item = event.getItem().getItemStack().toString();
//        if (item.length() > 1000) {
//            item = item.substring(0, 1000);
//            item = item.concat(" Item string too long, ending at 1000 characters.");
//        }

        String location = Logging.getBetterLocationString(event.getItem().getLocation());

        String message = "";
        message = message.concat("|");
        message = message.concat("Time:");
        message = message.concat(time);
        message = message.concat("|");
        message = message.concat("User:");
        message = message.concat(user);
        message = message.concat("|");
        message = message.concat("Item:");
        message = message.concat(item);
        message = message.concat("|");
        message = message.concat("Location:");
        message = message.concat(location);
        message = message.concat("|");

        Logging.WriteToFile(Logging.pickedUpItemsLogName, message);

    }

    public static void logCancelledSpawn(EntitySpawnEvent event) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean(Logging.logNamesAndConfigPaths.get(Logging.spawnLimitReachedLogName) + ".Enabled"))
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String entityType = event.getEntityType().toString();

        String location = Logging.getBetterLocationString(event.getLocation());

        String claimOwner = "";
        if (APIs.GriefPreventionFound) {
            Claim claim = GriefPrevention.instance.dataStore.getClaimAt(event.getLocation(), true, null);
            if (claim != null) claimOwner = claim.getOwnerName();
        }

        String message = "";
        message = message.concat("|");
        message = message.concat("Time:");
        message = message.concat(time);
        message = message.concat("|");
        message = message.concat("EntityType:");
        message = message.concat(entityType);
        message = message.concat("|");
        message = message.concat("Location:");
        message = message.concat(location);
        message = message.concat("|");
        message = message.concat("ClaimOwner:");
        message = message.concat(claimOwner);
        message = message.concat("|");

        Logging.WriteToFile(Logging.spawnLimitReachedLogName, message);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public static void onInventoryClick(InventoryClickEvent event) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean(Logging.logNamesAndConfigPaths.get(Logging.uiClicksLogName) + ".Enabled"))
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String user = event.getWhoClicked().getName();

        String inventoryName = event.getView().getTitle();

        String clickedItem = "";
        if (event.getCurrentItem() != null && !event.getCurrentItem().getType().equals(Material.AIR))
            clickedItem = event.getCurrentItem().toString();
//        if (clickedItem.length() > 1000) {
//            clickedItem = clickedItem.substring(0, 1000);
//            clickedItem = clickedItem.concat(" Item string too long, ending at 1000 characters.");
//        }

        String location = Logging.getBetterLocationString(event.getWhoClicked().getLocation());

        String message = "";
        message = message.concat("|");
        message = message.concat("Time:");
        message = message.concat(time);
        message = message.concat("|");
        message = message.concat("User:");
        message = message.concat(user);
        message = message.concat("|");
        message = message.concat("InventoryName:");
        message = message.concat(inventoryName);
        message = message.concat("|");
        message = message.concat("ClickedItem:");
        message = message.concat(clickedItem);
        message = message.concat("|");
        message = message.concat("Location:");
        message = message.concat(location);
        message = message.concat("|");

        Logging.WriteToFile(Logging.uiClicksLogName, message);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public static void onPlayerItemBreak(PlayerItemBreakEvent event) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean(Logging.logNamesAndConfigPaths.get(Logging.itemsBrokenLogName) + ".Enabled"))
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String user = event.getPlayer().getName();

        String item = event.getBrokenItem().toString();

        String location = Logging.getBetterLocationString(event.getPlayer().getLocation());

        String message = "";
        message = message.concat("|");
        message = message.concat("Time:");
        message = message.concat(time);
        message = message.concat("|");
        message = message.concat("User:");
        message = message.concat(user);
        message = message.concat("|");
        message = message.concat("Item:");
        message = message.concat(item);
        message = message.concat("|");
        message = message.concat("Location:");
        message = message.concat(location);
        message = message.concat("|");

        Logging.WriteToFile(Logging.itemsBrokenLogName, message);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public static void onItemDespawn(ItemDespawnEvent event) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean(Logging.logNamesAndConfigPaths.get(Logging.itemsDespawnedLogName) + ".Enabled"))
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String item = event.getEntity().getItemStack().toString();

        String location = Logging.getBetterLocationString(event.getLocation());

        String message = "";
        message = message.concat("|");
        message = message.concat("Time:");
        message = message.concat(time);
        message = message.concat("|");
        message = message.concat("Item:");
        message = message.concat(item);
        message = message.concat("|");
        message = message.concat("Location:");
        message = message.concat(location);
        message = message.concat("|");

        Logging.WriteToFile(Logging.itemsDespawnedLogName, message);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public static void onEntityDamage(EntityDamageEvent event) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean(Logging.logNamesAndConfigPaths.get(Logging.itemsDestroyedLogName) + ".Enabled"))
            return;

        if (!(event.getEntity() instanceof Item)) return;

        if (((Item) event.getEntity()).getItemStack().getType().equals(Material.CACTUS)) return;

        String time = new Date(System.currentTimeMillis()).toString();

        String item = ((Item) event.getEntity()).getItemStack().toString();

        String location = Logging.getBetterLocationString(event.getEntity().getLocation());

        String cause = event.getCause().toString();

        String message = "";
        message = message.concat("|");
        message = message.concat("Time:");
        message = message.concat(time);
        message = message.concat("|");
        message = message.concat("Item:");
        message = message.concat(item);
        message = message.concat("|");
        message = message.concat("Location:");
        message = message.concat(location);
        message = message.concat("|");
        message = message.concat("Cause:");
        message = message.concat(cause);
        message = message.concat("|");

        Logging.WriteToFile(Logging.itemsDestroyedLogName, message);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public static void onPlayerCommandSend(PlayerCommandPreprocessEvent event) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean(Logging.logNamesAndConfigPaths.get(Logging.commandsWithLocationLogName) + ".Enabled"))
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String user = event.getPlayer().getName();

        String command = event.getMessage();

        String location = Logging.getBetterLocationString(event.getPlayer().getLocation());

        String message = "";
        message = message.concat("|");
        message = message.concat("Time:");
        message = message.concat(time);
        message = message.concat("|");
        message = message.concat("User:");
        message = message.concat(user);
        message = message.concat("|");
        message = message.concat("Command:");
        message = message.concat(command);
        message = message.concat("|");
        message = message.concat("Location:");
        message = message.concat(location);
        message = message.concat("|");

        Logging.WriteToFile(Logging.commandsWithLocationLogName, message);

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public static void onPlayerDeath(PlayerDeathEvent event) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean(Logging.logNamesAndConfigPaths.get(Logging.droppedItemsOnDeathLogName) + ".Enabled"))
            return;

        String time = new Date(System.currentTimeMillis()).toString();

        String user = event.getEntity().getName();

        String items = "";

        for (ItemStack item : event.getDrops()) {
            if (!items.isEmpty()) {
                items = items.concat(", ");
            }
            items = items.concat(item.toString());
        }

        String location = Logging.getBetterLocationString(event.getEntity().getLocation());

        String message = "";
        message = message.concat("|");
        message = message.concat("Time:");
        message = message.concat(time);
        message = message.concat("|");
        message = message.concat("User:");
        message = message.concat(user);
        message = message.concat("|");
        message = message.concat("Items:");
        message = message.concat(items);
        message = message.concat("|");
        message = message.concat("Location:");
        message = message.concat(location);
        message = message.concat("|");

        Logging.WriteToFile(Logging.droppedItemsOnDeathLogName, message);

    }

}