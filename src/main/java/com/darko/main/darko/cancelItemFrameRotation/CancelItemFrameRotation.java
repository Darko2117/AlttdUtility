package com.darko.main.darko.cancelItemFrameRotation;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import com.darko.main.AlttdUtility;
import io.papermc.paper.event.player.PlayerItemFrameChangeEvent;
import io.papermc.paper.event.player.PlayerItemFrameChangeEvent.ItemFrameChangeAction;
import net.md_5.bungee.api.ChatColor;

public class CancelItemFrameRotation implements Listener {

    private static final NamespacedKey namespacedKey = new NamespacedKey(AlttdUtility.getInstance(), "itemframe-allow-rotation");

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {

        if (!event.getHand().equals(EquipmentSlot.HAND))
            return;

        if (!(event.getRightClicked() instanceof ItemFrame))
            return;

        ItemFrame itemFrame = (ItemFrame) event.getRightClicked();

        if (itemFrame.getItem().getType().equals(Material.AIR))
            return;

        if (!event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.SLIME_BALL))
            return;

        Player player = event.getPlayer();

        PersistentDataContainer persistentDataContainer = itemFrame.getPersistentDataContainer();

        int allowedRotation = 1;

        if (persistentDataContainer.has(namespacedKey, PersistentDataType.INTEGER) && persistentDataContainer.get(namespacedKey, PersistentDataType.INTEGER) == 0)
            allowedRotation = 0;

        if (allowedRotation == 1 && !player.getGameMode().equals(GameMode.CREATIVE)) {
            player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
        }

        if (allowedRotation == 0) {
            itemFrame.getPersistentDataContainer().set(namespacedKey, PersistentDataType.INTEGER, 1);
            player.sendMessage(ChatColor.YELLOW + "Item frame rotation " + ChatColor.GOLD + "enabled.");
        } else {
            itemFrame.getPersistentDataContainer().set(namespacedKey, PersistentDataType.INTEGER, 0);
            player.sendMessage(ChatColor.YELLOW + "Item frame rotation " + ChatColor.GOLD + "disabled.");
        }

        event.setCancelled(true);

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerItemFrameChange(PlayerItemFrameChangeEvent event) {

        if (!event.getAction().equals(ItemFrameChangeAction.ROTATE))
            return;

        ItemFrame itemFrame = event.getItemFrame();

        PersistentDataContainer persistentDataContainer = itemFrame.getPersistentDataContainer();

        if (persistentDataContainer.has(namespacedKey, PersistentDataType.INTEGER) && persistentDataContainer.get(namespacedKey, PersistentDataType.INTEGER) == 0) {
            event.getPlayer().sendMessage(ChatColor.RED + "Can't rotate this item frame.");
            event.setCancelled(true);
        }

    }

}
