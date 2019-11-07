package com.darko.utilities.plugin;


import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	
	@Override
	public void onEnable(){
		System.out.println("Utility plugin started... Teri bring back sugar cane please thank you :)");
		Bukkit.getPluginManager().registerEvents(new onJoin(), this);
		Bukkit.getPluginManager().registerEvents(new onStairsRightClick(), this);
		Bukkit.getPluginManager().registerEvents(new onPlayerDismount(), this);
		getCommand("hat").setExecutor(new Hat());
		getCommand("servermsg").setExecutor(new Servermsg());
		getCommand("chair").setExecutor(new Chair());
		}}
