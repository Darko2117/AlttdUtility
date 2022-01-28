package com.darko.main.darko.illegalItemCheck;

import com.darko.main.AlttdUtility;
import com.darko.main.darko.logging.listeners.LoggingNoAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.ArrayList;
import java.util.List;

public class IllegalItemCheck implements Listener {

    private static final List<String> whitelistedWorlds = new ArrayList<>();
    private static final List<IllegalItem> illegalItems = new ArrayList<>();

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onInventoryClickEvent(InventoryClickEvent inventoryClickEvent) {

        if (!(inventoryClickEvent.getWhoClicked() instanceof Player player)) return;

        ItemStack item = inventoryClickEvent.getCurrentItem();
        if (item == null) return;

        if (playerBypassCheck(player)) return;

        shulkerBoxCheck(item, player, inventoryClickEvent);

        if (!illegalItemCheck(item)) return;

        //inventoryClickEvent.getClickedInventory().setItem(inventoryClickEvent.getSlot(), new ItemStack(Material.AIR));
        //inventoryClickEvent.setCancelled(true);

        LoggingNoAPI.logIllegalItems(item, player, inventoryClickEvent);

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEntityPickupItemEvent(EntityPickupItemEvent entityPickupItemEvent) {

        if (!(entityPickupItemEvent.getEntity() instanceof Player player)) return;

        ItemStack item = entityPickupItemEvent.getItem().getItemStack();
        if (item == null) return;

        if (playerBypassCheck(player)) return;

        shulkerBoxCheck(item, player, entityPickupItemEvent);

        if (!illegalItemCheck(item)) return;

        //entityPickupItemEvent.getItem().remove();
        //entityPickupItemEvent.setCancelled(true);

        LoggingNoAPI.logIllegalItems(item, player, entityPickupItemEvent);

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerDropItemEvent(PlayerDropItemEvent playerDropItemEvent) {

        ItemStack item = playerDropItemEvent.getItemDrop().getItemStack();

        Player player = playerDropItemEvent.getPlayer();

        if (playerBypassCheck(player)) return;

        shulkerBoxCheck(item, player, playerDropItemEvent);

        if (!illegalItemCheck(item)) return;

        //playerDropItemEvent.getItemDrop().remove();

        LoggingNoAPI.logIllegalItems(item, player, playerDropItemEvent);

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent playerChangedWorldEvent) {

        Player player = playerChangedWorldEvent.getPlayer();

        if (playerBypassCheck(player)) return;

        for (int i = 0; i <= 41; i++) {

            ItemStack item = player.getInventory().getItem(i);
            if (item == null) continue;

            shulkerBoxCheck(item, player, playerChangedWorldEvent);

            if (!illegalItemCheck(item)) continue;

            //player.getInventory().setItem(i, new ItemStack(Material.AIR));
            LoggingNoAPI.logIllegalItems(item, player, playerChangedWorldEvent);

        }

    }

    private void shulkerBoxCheck(ItemStack itemStack, Player player, Event event) {

        if (itemStack.getItemMeta() instanceof BlockStateMeta blockStateMeta) {
            if (blockStateMeta.getBlockState() instanceof ShulkerBox shulker) {
                for (int i = 0; i < 27; i++) {
                    if (illegalItemCheck(shulker.getInventory().getItem(i))) {

                        LoggingNoAPI.logIllegalItems(shulker.getInventory().getItem(i), player, event);

                        //shulker.getInventory().setItem(i, new ItemStack(Material.AIR));

                    }
                }
                blockStateMeta.setBlockState(shulker);
            }
            itemStack.setItemMeta(blockStateMeta);
        }

    }

    private boolean illegalItemCheck(ItemStack itemStack) {

        if (itemStack == null) return false;
        if (itemStack.getType().equals(Material.AIR)) return false;

        for (IllegalItem illegalItem : illegalItems) {

            boolean materialNeeded = !illegalItem.getItemMaterial().isEmpty();
            boolean nameNeeded = !illegalItem.getItemName().isEmpty();
            boolean loreNeeded = !illegalItem.getItemLore().isEmpty();
            boolean enchantNeeded = !illegalItem.getItemEnchant().isEmpty();

            boolean materialFound = false;
            boolean nameFound = false;
            boolean loreFound = false;
            boolean enchantFound = false;

            material:
            {
                if (!materialNeeded) break material;
                if (itemStack.getType().toString().toLowerCase().matches("(.*)" + illegalItem.getItemMaterial() + "(.*)"))
                    materialFound = true;
            }

            name:
            {
                if (!nameNeeded) break name;
                if (itemStack.getItemMeta() == null) break name;
                if (!itemStack.getItemMeta().hasDisplayName()) break name;
                if (itemStack.getItemMeta().displayName().toString().toLowerCase().matches("(.*)" + illegalItem.getItemName() + "(.*)"))
                    nameFound = true;
            }

            lore:
            {
                if (!loreNeeded) break lore;
                if (itemStack.getItemMeta() == null) break lore;
                if (!itemStack.getItemMeta().hasLore()) break lore;
                for (Component component : itemStack.getItemMeta().lore()) {
                    if (component.toString().toLowerCase().matches("(.*)" + illegalItem.getItemLore() + "(.*)")) {
                        loreFound = true;
                        break lore;
                    }
                }
            }

            enchant:
            {
                if (!enchantNeeded) break enchant;
                if (itemStack.getItemMeta() == null) break enchant;
                if (itemStack.getItemMeta().getEnchants().isEmpty()) break enchant;
                if (itemStack.getEnchantments().toString().toLowerCase().matches("(.*)" + illegalItem.getItemEnchant() + "(.*)"))
                    enchantFound = true;
            }

            if (materialNeeded == materialFound && nameNeeded == nameFound && loreNeeded == loreFound && enchantNeeded == enchantFound)
                return true;

        }

        return false;

    }

    private boolean playerBypassCheck(Player player) {

        if (player.hasPermission("utility.illegal-item-bypass")) return true;

        for (String string : whitelistedWorlds) {
            if (string.equals(player.getLocation().getWorld().getName())) {
                return true;
            }
        }

        return false;

    }

    public static void loadIllegalItems() {

        whitelistedWorlds.clear();
        whitelistedWorlds.addAll(AlttdUtility.getInstance().getConfig().getStringList("IllegalItemCheckWhitelistedWorlds"));

        illegalItems.clear();

        for (String illegalItemName : AlttdUtility.getInstance().getConfig().getKeys(true)) {

            if (!illegalItemName.startsWith("IllegalItemCheck.")) continue;

            illegalItemName = illegalItemName.substring(17);

            if (illegalItemName.contains(".")) continue;
            if (illegalItemName.isEmpty()) continue;

            String illegalItemItemMaterial = AlttdUtility.getInstance().getConfig().getString("IllegalItemCheck." + illegalItemName + ".Material").toLowerCase();
            String illegalItemItemName = AlttdUtility.getInstance().getConfig().getString("IllegalItemCheck." + illegalItemName + ".Name").toLowerCase();
            String illegalItemItemLore = AlttdUtility.getInstance().getConfig().getString("IllegalItemCheck." + illegalItemName + ".Lore").toLowerCase();
            String illegalItemItemEnchant = AlttdUtility.getInstance().getConfig().getString("IllegalItemCheck." + illegalItemName + ".Enchant").toLowerCase();

            if (illegalItemItemMaterial.isEmpty() && illegalItemItemName.isEmpty() && illegalItemItemLore.isEmpty() && illegalItemItemEnchant.isEmpty())
                continue;

            illegalItems.add(new IllegalItem(illegalItemName, illegalItemItemMaterial, illegalItemItemName, illegalItemItemLore, illegalItemItemEnchant));

        }

    }

}
