package com.darko.main.utilities.flags;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.darko.main.utilities.other.Flags;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

public class onEntityRename implements Listener{

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onNameTagUse(PlayerInteractEntityEvent e){

		Player player = e.getPlayer();
		if(e.getPlayer().getInventory().getItemInMainHand().getType() == Material.NAME_TAG ||
		   e.getPlayer().getInventory().getItemInOffHand().getType() == Material.NAME_TAG){
		
		com.sk89q.worldedit.util.Location location = BukkitAdapter.adapt(e.getRightClicked().getLocation());
		
		RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		RegionQuery query = container.createQuery();
		ApplicableRegionSet set = query.getApplicableRegions(location);
		if(set.size() != 0){
		LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
		RegionContainer container1 = WorldGuard.getInstance().getPlatform().getRegionContainer();
		RegionQuery query1 = container1.createQuery();
		
		if(!query1.testState(location, localPlayer, Flags.NAME_TAG_USE)){

		if(!player.hasPermission("utility.nametag.bypass")){
		e.setCancelled(true);
		}
		}}}}}
