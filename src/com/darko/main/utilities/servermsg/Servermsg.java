package com.darko.main.utilities.servermsg;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

	public class Servermsg implements CommandExecutor {
		
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	if(sender.hasPermission("utility.servermsg")){
	try{
	Player player = (Player) Bukkit.getPlayer(args[0].toString());
	String text = "";
	for(int z = 1; z < args.length; z++){
	text += args[z];
	text += " ";
	}
	Integer i = 0;
	if(text.contains("^n")){
	do{player.sendMessage(ChatColor.translateAlternateColorCodes('&', text.substring(i, text.indexOf("^n"))));
	i += text.indexOf("^n") + 2;
	text = text.substring(i, text.length());
	i = 0;
	}while(text.indexOf("^n") != -1);}
	player.sendMessage(ChatColor.translateAlternateColorCodes('&', text));
	}catch(Exception e){sender.sendMessage(ChatColor.RED + "Usage is /servermsg <username> <message>");
						sender.sendMessage(ChatColor.RED + "Color codes work and a new line is ^n");}
	}else{sender.sendMessage(ChatColor.RED + "You do not have permission to do this.");
	}
	return false;
	}}