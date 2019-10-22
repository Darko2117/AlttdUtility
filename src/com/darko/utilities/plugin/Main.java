package com.darko.utilities.plugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.permission.Permission;

public class Main extends JavaPlugin{
	@Override
	public void onEnable(){
		System.out.println("Altitude Utilities Enabled!");
		getCommand("cooldown").setExecutor(new Cooldown(this));
		
		if(getApi() != null){
			System.out.println("-----> Vault was found!");
		}else{
			System.out.println("-----> Vault was not found! Plugin disabled.");
			Bukkit.getPluginManager().disablePlugin(this);
		}
	}	
	public Permission getApi(){
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Vault");
		if(plugin instanceof Permission){
			return (Permission) plugin;
		}else{
			return null;
		}}
}
