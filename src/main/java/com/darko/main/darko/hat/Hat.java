package com.darko.main.darko.hat;

import com.darko.main.AlttdUtility;
import com.darko.main.common.Methods;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Hat implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.Hat"))
            return true;

        if (!(sender instanceof Player)) {
            Methods.sendConfigMessage(sender, "Messages.PlayerOnlyCommandMessage");
            return true;
        }

        Player player = (Player) sender;

        if (player.getInventory().getHelmet() == null) {
            if (player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                Methods.sendConfigMessage(player, "Messages.HatNoItem");
            } else {
                player.getInventory().setHelmet(player.getInventory().getItemInMainHand());
                player.getInventory().setItemInMainHand(null);
                Methods.sendConfigMessage(player, "Messages.HatEquipped");
            }
        } else {
            if (player.getInventory().getHelmet().getEnchantments().containsKey(Enchantment.BINDING_CURSE)) {
                Methods.sendConfigMessage(player, "Messages.HatCurseOfBinding");
            } else {
                ItemStack temp = player.getInventory().getHelmet();
                player.getInventory().setHelmet(player.getInventory().getItemInMainHand());
                player.getInventory().setItemInMainHand(temp);
                Methods.sendConfigMessage(player, "Messages.HatSwapped");
            }
        }

        return true;

    }

}
