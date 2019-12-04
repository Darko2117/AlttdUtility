package com.darko.main.utilities.flags;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.darko.main.utilities.other.Flags;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

public class onBoneMeal implements Listener{

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBoneMealUse(PlayerInteractEvent e){
	
	Player player = e.getPlayer();
	
	if(e.getAction() == Action.RIGHT_CLICK_BLOCK && player.getInventory().getItemInMainHand().getType() == Material.BONE_MEAL){
	
		com.sk89q.worldedit.util.Location location = BukkitAdapter.adapt(e.getClickedBlock().getLocation());
		
		RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		RegionQuery query = container.createQuery();
		ApplicableRegionSet set = query.getApplicableRegions(location);
		
		if(set.size() != 0){
		LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
		RegionContainer container1 = WorldGuard.getInstance().getPlatform().getRegionContainer();
		RegionQuery query1 = container1.createQuery();
		
		if(!query1.testState(location, localPlayer, Flags.BONE_MEAL_USE)){
		if(!player.hasPermission("utility.bonemeal.bypass")){
		e.setCancelled(true);
		}}}}}}