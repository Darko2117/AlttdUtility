package com.darko.utilities.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.ChatColor;

	public class Hat implements CommandExecutor {
		
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	Player player = (Player) sender;
	if(player.hasPermission("utility.hat")){
	if(player.getInventory().getHelmet() == null){
	if(player.getInventory().getItemInMainHand().getAmount() == 0){
	player.sendMessage(ChatColor.YELLOW + "Hold the item that you wish to put on your head.");
	//		^
	//		|
	//	If the player has no helmet and it not holding an item
	}else{
	player.getInventory().setHelmet(player.getInventory().getItemInMainHand());
	player.getInventory().setItemInMainHand(null);
	player.sendMessage(ChatColor.GREEN + "Item equipped.");}}
	//		^
	//		|
	//	If the player has no helmet and but is holding an item
			
	else if(player.getInventory().getHelmet() != null){
	if(player.getInventory().getHelmet().getEnchantments().containsKey(Enchantment.BINDING_CURSE)){
	player.sendMessage(ChatColor.RED + "Can't take off an item that has the " + 
	ChatColor.LIGHT_PURPLE+ org.bukkit.ChatColor.ITALIC +"Curse of Binding" + ChatColor.RESET + ChatColor.RED + ".");
	}else{
	ItemStack temp = player.getInventory().getHelmet();
	player.getInventory().setHelmet(player.getInventory().getItemInMainHand());
	player.getInventory().setItemInMainHand(temp);
	player.sendMessage(ChatColor.GREEN + "Items swapped.");}}
	//		^
	//		|
	//	If the player has both
	}else{player.sendMessage(ChatColor.RED + "You do not have permission to do this.");
	//Teri is a dumdum
	}
	return false;
	}}
