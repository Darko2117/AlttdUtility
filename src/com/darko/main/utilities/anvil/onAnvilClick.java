package com.darko.main.utilities.anvil;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.darko.main.utilities.other.GlobalVariables;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

public class onAnvilClick implements Listener{

	@EventHandler
	public void onAnvilUseUp(PlayerInteractEvent e){
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && 
		   e.getClickedBlock().getType() == Material.ANVIL){
			
			Player player = e.getPlayer();
			
			com.sk89q.worldedit.util.Location location = BukkitAdapter.adapt(e.getClickedBlock().getLocation());
			LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
			RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
			RegionQuery query = container.createQuery();
			if(query.testState(location, localPlayer, GlobalVariables.ANVIL_REPAIR)){
			
			String dataString = e.getClickedBlock().getBlockData().getAsString();
			player.sendMessage(dataString);
			if(dataString.contains("chipped_")){dataString = dataString.replace("chipped_", "");}
			else if(dataString.contains("damaged_")){dataString = dataString.replace("damaged_", "");}
			player.sendMessage(dataString);
			e.getClickedBlock().setBlockData(Bukkit.createBlockData(dataString));
			
			}}}}
