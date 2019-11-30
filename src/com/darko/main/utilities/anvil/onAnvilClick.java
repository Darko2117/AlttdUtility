package com.darko.main.utilities.anvil;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.darko.main.utilities.other.GlobalVariables;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

public class onAnvilClick implements Listener{

	@EventHandler
	public void onDamagedAnvilClick(PlayerInteractEvent e){
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && 
		   e.getClickedBlock().getType() == Material.ANVIL){
			
			Player player = e.getPlayer();
			com.sk89q.worldedit.util.Location location = BukkitAdapter.adapt(e.getClickedBlock().getLocation());
			
			RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
			RegionQuery query = container.createQuery();
			ApplicableRegionSet set = query.getApplicableRegions(location);
			
			if(set.size() != 0){
			LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
			RegionContainer container1 = WorldGuard.getInstance().getPlatform().getRegionContainer();
			RegionQuery query1 = container1.createQuery();
			
			if(query1.testState(location, localPlayer, GlobalVariables.ANVIL_REPAIR)){
			
			String dataString = e.getClickedBlock().getBlockData().getAsString();
			if(dataString.contains("chipped_")){dataString = dataString.replace("chipped_", "");}
			else if(dataString.contains("damaged_")){dataString = dataString.replace("damaged_", "");}
			e.getClickedBlock().setBlockData(Bukkit.createBlockData(dataString));
			
			}}}}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void AnvilClickPriority(PlayerInteractEvent e){
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && 
		   e.getClickedBlock().getType() == Material.ANVIL){
			
			Player player = e.getPlayer();
			com.sk89q.worldedit.util.Location location = BukkitAdapter.adapt(e.getClickedBlock().getLocation());
			
			RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
			RegionQuery query = container.createQuery();
			ApplicableRegionSet set = query.getApplicableRegions(location);
			
			if(set.size() != 0){
			LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
			RegionContainer container1 = WorldGuard.getInstance().getPlatform().getRegionContainer();
			RegionQuery query1 = container1.createQuery();
			
			if(query1.testState(location, localPlayer, GlobalVariables.ANVIL_USE)){

			if(e.isCancelled()){e.setCancelled(false);}
			
			}}}}}
