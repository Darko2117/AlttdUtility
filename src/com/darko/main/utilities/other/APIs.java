package com.darko.main.utilities.other;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import me.lucko.luckperms.api.LuckPermsApi;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

public class APIs {

	public static GriefPrevention GriefPreventionApi() {
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("GriefPrevention");
		if (plugin instanceof GriefPrevention) {
			return (GriefPrevention) plugin;
		} else {
			return null;
		}
	}

	public static WorldGuardPlugin WorldGuardApi() {
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		if (plugin instanceof WorldGuardPlugin) {
			return (WorldGuardPlugin) plugin;
		} else {
			return null;
		}
	}

	public static LuckPermsApi LuckPermsApi() {
		RegisteredServiceProvider<LuckPermsApi> provider = null;
		provider = Bukkit.getServicesManager().getRegistration(LuckPermsApi.class);
		if (provider != null) {
			LuckPermsApi api = provider.getProvider();
			return api;
		} else {
			return null;
		}
	}

}
