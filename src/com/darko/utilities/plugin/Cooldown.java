package com.darko.utilities.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.lucko.luckperms.common.model.User;

	public class Cooldown implements CommandExecutor {
		
		private Main main;
		public Cooldown(Main main){
			this.main = main;
		}
		
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)){
			System.out.println("This command can only be used by a player.");
		}else{
			Player player = (Player) sender;
			if(player.hasPermission("keyshop.buy")){
			
			}
			if(player.hasPermission("rtp.no")){
				
			}
		}
	return false;	
	}}