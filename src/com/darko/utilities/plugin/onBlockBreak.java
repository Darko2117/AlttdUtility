package com.darko.utilities.plugin;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class onBlockBreak implements Listener {
	
	@EventHandler
	public void BlockBreak(BlockBreakEvent e){
		if(GlobalVariables.occupiedSeats.containsValue(e.getBlock().getLocation())){
			if(e.getPlayer().hasPermission("utility.forcedismount")){
				Entity ent = GlobalVariables.aliveSeats.get(e.getBlock().getLocation());
				ent.remove();
			}else{
				e.setCancelled(true);
			}
		}
	}
}
