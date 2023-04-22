package com.darko.main.darko.savedItems;

import com.darko.main.AlttdUtility;
import com.darko.main.common.Methods;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SaveItem implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.SaveItemCommand"))
            return true;

        if (!(commandSender instanceof Player player)) {
            new Methods().sendConfigMessage(commandSender, "Messages.PlayerOnlyCommandMessage");
            return true;
        }

        if (player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
            new Methods().sendConfigMessage(commandSender, "Messages.SaveItemEmptyHand");
            return true;
        }

        int ID = 0;
        while (AlttdUtility.getInstance().getConfig().contains("SavedItems." + ID))
            ID++;

        // AlttdUtility.getInstance().getConfig().set("SavedItems." + ID, new
        // Methods().serializeItemStack(player.getInventory().getItemInMainHand()));
        AlttdUtility.getInstance().getConfig().set("SavedItems." + ID, player.getInventory().getItemInMainHand());
        AlttdUtility.getInstance().saveConfig();

        SavedItem.loadSavedItems();

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', AlttdUtility.getInstance().getConfig().getString("Messages.SaveItemSaved").replace("%ID%", ID + "")));

        return true;

    }

}
