package com.darko.main.utilities.other;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;

import me.lucko.luckperms.api.LuckPermsApi;
import me.ryanhamshire.GriefPrevention.GriefPrevention;


public class GlobalVariables {
	
	public static HashMap<UUID, Boolean> 		chairEnabled = new HashMap<>();
	public static HashMap<UUID, Location>		occupiedSeats = new HashMap<>();
	public static HashMap<Location, Entity> 	aliveSeats = new HashMap<>();
	
	public static String ChairName = "There is a 16 character limit on a name tag";
	public static String OldChairName = "There is a 16 character limit on a name tag so someone naming a pig this is impossible";
	
	public static StateFlag SIT, ANVIL_REPAIR;
	
	public static void SitFlag(){
		FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
	    try {
	     StateFlag flag = new StateFlag("sit", true);
	     registry.register(flag);
	     SIT = flag;
	     }catch (FlagConflictException e) {
	     Flag<?> existing = registry.get("sit");
	     if (existing instanceof StateFlag) {
	     SIT = (StateFlag) existing;
	     }else{}}}
	
	public static void AnvilRepairFlag(){
		FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
	    try {
	     StateFlag flag = new StateFlag("anvilrepair", true);
	     registry.register(flag);
	     ANVIL_REPAIR = flag;
	     }catch (FlagConflictException e) {
	     Flag<?> existing = registry.get("anvilrepair");
	     if (existing instanceof StateFlag) {
	     ANVIL_REPAIR = (StateFlag) existing;
	     }else{}}}
	
	public static GriefPrevention GriefPreventionApi(){
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("GriefPrevention");
		if(plugin instanceof GriefPrevention){
		return (GriefPrevention) plugin;
		}else{return null;}}
	
	public static WorldGuardPlugin WorldGuardApi(){
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		if(plugin instanceof WorldGuardPlugin){
		return (WorldGuardPlugin) plugin;
		}else{return null;}}
	
	public static LuckPermsApi LuckPermsApi(){
	RegisteredServiceProvider<LuckPermsApi> provider = null;
	provider = Bukkit.getServicesManager().getRegistration(LuckPermsApi.class);
	if (provider != null) {
	    LuckPermsApi api = provider.getProvider();
	    return api;}else{return null;}}
	
	
}
