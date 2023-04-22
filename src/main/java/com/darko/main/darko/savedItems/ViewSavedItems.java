package com.darko.main.darko.savedItems;

import com.darko.main.AlttdUtility;
import com.darko.main.common.Methods;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ViewSavedItems implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.ViewSavedItemsCommand"))
            return true;

        if (!(commandSender instanceof Player player)) {
            new Methods().sendConfigMessage(commandSender, "Messages.PlayerOnlyCommandMessage");
            return true;
        }

        Inventory inventory = Bukkit.createInventory(null, 54, "Saved items");

        int startingIndex = 0;
        if (strings.length != 0) {
            try {
                startingIndex = Integer.parseInt(strings[0]) * 54;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        List<SavedItem> savedItems = SavedItem.getSavedItems();

        int inventorySlotIndex = 0;
        for (int i = startingIndex; i < startingIndex + 54; i++) {
            if (savedItems.size() <= i)
                break;
            inventory.setItem(inventorySlotIndex, savedItems.get(i).getItemStack());
            inventorySlotIndex++;
        }

        player.openInventory(inventory);

        return true;

    }

}
