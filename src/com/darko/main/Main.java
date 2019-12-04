package com.darko.main;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.darko.main.cosmetics.chair.Chair;
import com.darko.main.cosmetics.chair.onBlockBreak;
import com.darko.main.cosmetics.chair.onChunkLoad;
import com.darko.main.cosmetics.chair.onPlayerDismount;
import com.darko.main.cosmetics.chair.onPlayerQuit;
import com.darko.main.cosmetics.chair.onPlayerTeleportCommand;
import com.darko.main.cosmetics.chair.onStairsRightClick;
import com.darko.main.cosmetics.hat.Hat;
import com.darko.main.utilities.flags.onAnvilClick;
import com.darko.main.utilities.flags.onEntityRename;
import com.darko.main.utilities.cooldown.Cooldown;
import com.darko.main.utilities.other.APIs;
import com.darko.main.utilities.other.ConsoleColors;
import com.darko.main.utilities.other.Flags;
import com.darko.main.utilities.servermsg.Servermsg;
import com.darko.main.utilities.tamedexpire.OnEntityInteractWithLead;
import com.darko.main.utilities.teleport.onPlayerTeleport;

public class Main extends JavaPlugin{
	
	@Override
	public void onEnable(){
		System.out.println("Utility plugin started... Teri bring back " + ConsoleColors.GREEN_BOLD + "Sugar Cane" + ConsoleColors.RESET + " please thank you :)");
		
		Bukkit.getPluginManager().registerEvents(new onStairsRightClick(), this);
		Bukkit.getPluginManager().registerEvents(new onPlayerDismount(this), this);
		Bukkit.getPluginManager().registerEvents(new onPlayerQuit(), this);
		Bukkit.getPluginManager().registerEvents(new onBlockBreak(), this);
		Bukkit.getPluginManager().registerEvents(new onChunkLoad(this), this);
		Bukkit.getPluginManager().registerEvents(new onPlayerTeleportCommand(), this);
		Bukkit.getPluginManager().registerEvents(new onAnvilClick(), this);
		Bukkit.getPluginManager().registerEvents(new onPlayerTeleport(), this);
		Bukkit.getPluginManager().registerEvents(new onEntityRename(), this);
		Bukkit.getPluginManager().registerEvents(new OnEntityInteractWithLead(), this);

		getCommand("hat").setExecutor(new Hat());
		getCommand("servermsg").setExecutor(new Servermsg());
		getCommand("chair").setExecutor(new Chair());
		
		Flags.FlagsEnable();
		
		if(APIs.GriefPreventionApi() != null && APIs.WorldGuardApi() != null){
			System.out.println("-----> GriefPrevention & WorldGuard were found!");
			getCommand("chair").setExecutor(new Chair());
		}else{System.out.println("-----> GriefPrevention or WorldGuard were not found! /chair will work in claims and worldguard regions.");}
		
		if(APIs.LuckPermsApi() != null){
			System.out.println("-----> LuckPerms was found!");
			getCommand("cooldown").setExecutor(new Cooldown());
		}else{System.out.println("-----> LuckPerms was not found! /cooldown will be disabled.");}
	}
}

	
