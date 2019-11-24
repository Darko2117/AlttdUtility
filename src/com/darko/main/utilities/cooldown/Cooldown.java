package com.darko.main.utilities.cooldown;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.darko.main.utilities.other.GlobalVariables;

import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.User;

public class Cooldown implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		Boolean rtp = false, crate = false;
		
		if(player.hasPermission("utility.cooldown")){
		if(args.length != 0){
		if(args[0].toString().equalsIgnoreCase("rtp")){
		rtp = rtpCheck(player);}
		
		else if(args[0].toString().equalsIgnoreCase("crate")){
		crate = crateCheck(player);}}
		
		else{
		rtp = rtpCheck(player);
		crate = crateCheck(player);}
		
		if(!rtp && !crate){player.sendMessage(ChatColor.YELLOW + "You don't have any cooldowns right now.");}
		
		}
		
		return false;}
	
	
	
	
	
	
	public static Boolean rtpCheck(Player player){
		if(player.hasPermission("rtp.no")){
		LuckPermsApi api = GlobalVariables.LuckPermsApi();
		User user = api.getUser(player.getUniqueId());
		for (Node n : user.getPermissions()) {
		if(n.getPermission().equalsIgnoreCase("rtp.no")) {
		Long time = n.getSecondsTilExpiry();
		Integer hours = 0, minutes = 0, seconds = 0;
		while(time>3600){time-=3600; hours++;}
		while(time>60){time-=60; minutes++;}
		seconds = time.intValue();
		if(hours != 0 && minutes != 0){player.sendMessage(ChatColor.GREEN + "Cooldown on the RTP portal is " + hours + " hours " + minutes + " minutes " + seconds + " seconds.");}
		else if(hours == 0 && minutes != 0){player.sendMessage(ChatColor.GREEN + "Cooldown on the RTP portal is " + minutes + " minutes " + seconds + " seconds.");}
		if(hours == 0 && minutes == 0){player.sendMessage(ChatColor.GREEN + "Cooldown on the RTP portal is " + seconds + " seconds.");}
		return true;
		}}}
		return false;
		}
	
	public static Boolean crateCheck(Player player){
		if(!player.hasPermission("keyshop.buy")){
		LuckPermsApi api = GlobalVariables.LuckPermsApi();
		User user = api.getUser(player.getUniqueId());
		for (Node n : user.getPermissions()) {
		if(n.getPermission().equalsIgnoreCase("keyshop.buy")) {
		Long time = n.getSecondsTilExpiry();
		Integer hours = 0, minutes = 0, seconds = 0;
		while(time>3600){time-=3600; hours++;}
		while(time>60){time-=60; minutes++;}
		seconds = time.intValue();
		if(hours != 0 && minutes != 0){player.sendMessage(ChatColor.GREEN + "Cooldown on the SuperCrate is " + hours + " hours " + minutes + " minutes " + seconds + " seconds.");}
		else if(hours == 0 && minutes != 0){player.sendMessage(ChatColor.GREEN + "Cooldown on the SuperCrate is " + minutes + " minutes " + seconds + " seconds.");}
		if(hours == 0 && minutes == 0){player.sendMessage(ChatColor.GREEN + "Cooldown on the SuperCrate is " + seconds + " seconds.");}
		return true;
		}}}
		return false;
		}
		}
