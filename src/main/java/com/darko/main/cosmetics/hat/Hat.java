package com.darko.main.cosmetics.hat;

import com.darko.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Hat implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        if (player.hasPermission("utility.hat")) {
            if (player.getInventory().getHelmet() == null) {
                if (player.getInventory().getItemInMainHand().getAmount() == 0) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            Main.getInstance().getConfig().getString("Messages.HatNoItem")));
                    // ^
                    // |
                    // If the player has no helmet and it not holding an item
                } else {
                    player.getInventory().setHelmet(player.getInventory().getItemInMainHand());
                    player.getInventory().setItemInMainHand(null);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            Main.getInstance().getConfig().getString("Messages.HatEquipped")));
                }
            }
            // ^
            // |
            // If the player has no helmet and but is holding an item

            else if (player.getInventory().getHelmet() != null) {
                if (player.getInventory().getHelmet().getEnchantments().containsKey(Enchantment.BINDING_CURSE)) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            Main.getInstance().getConfig().getString("Messages.HatCurseOfBinding")));
                } else {
                    ItemStack temp = player.getInventory().getHelmet();
                    player.getInventory().setHelmet(player.getInventory().getItemInMainHand());
                    player.getInventory().setItemInMainHand(temp);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            Main.getInstance().getConfig().getString("Messages.HatSwapped")));
                }
            }
            // ^
            // |
            // If the player has both
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    Main.getInstance().getConfig().getString("Messages.NoPermission")));
            // Teri is a dumdum
        }
        return false;
    }
}
