package com.darko.utilities.plugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Chair implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	Player player = (Player) sender;
	if(player.hasPermission("utility.chair")){
	if(GlobalVariables.chairEnabled.get(player.getUniqueId().toString()) == false){
	GlobalVariables.chairEnabled.put(player.getUniqueId().toString(), true);
	}else{GlobalVariables.chairEnabled.put(player.getUniqueId().toString(), false);}
	
	if(GlobalVariables.chairEnabled.get(player.getUniqueId().toString()) == true){
	player.sendMessage(ChatColor.GREEN + "Chair mode enabled, right click on any stairs to sit on them.");
	}else{player.sendMessage(ChatColor.RED + "Chair mode disabled.");}
	}else{player.sendMessage(ChatColor.RED + "You do not have permission to do this.");
	}
	return false;
	}}
