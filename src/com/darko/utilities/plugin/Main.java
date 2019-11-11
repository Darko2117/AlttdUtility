package com.darko.utilities.plugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin{
	
	@Override
	public void onEnable(){
		System.out.println("Utility plugin started... Teri bring back " + ConsoleColors.GREEN_BOLD + "Sugar Cane" + ConsoleColors.RESET + " please thank you :)");
		Bukkit.getPluginManager().registerEvents(new onStairsRightClick(), this);
		Bukkit.getPluginManager().registerEvents(new onPlayerDismount(this), this);
		Bukkit.getPluginManager().registerEvents(new onPlayerQuit(), this);
		Bukkit.getPluginManager().registerEvents(new onBlockBreak(), this);
		Bukkit.getPluginManager().registerEvents(new onPlayerTeleport(), this);
		getCommand("hat").setExecutor(new Hat());
		getCommand("servermsg").setExecutor(new Servermsg());
		GlobalVariables.SitFlag();
		
		if(GlobalVariables.GriefPreventionApi() != null && GlobalVariables.WorldGuardApi() != null){
			System.out.println("-----> GriefPrevention & WorldGuard were found!");
			getCommand("chair").setExecutor(new Chair());
		}else{System.out.println("-----> GriefPrevention or WorldGuard were not found! /chair will be disabled.");}
		
		if(GlobalVariables.LuckPermsApi() != null){
			System.out.println("-----> LuckPerms was found!");
			getCommand("cooldown").setExecutor(new Cooldown());
		}else{System.out.println("-----> LuckPerms was not found! /cooldown will be disabled.");}
	}
}

	
