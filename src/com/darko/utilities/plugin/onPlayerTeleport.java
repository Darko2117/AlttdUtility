package com.darko.utilities.plugin;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class onPlayerTeleport implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void PlayerTeleport(PlayerTeleportEvent e){
		try{Player player = e.getPlayer();
		Entity ent = player.getVehicle();
		if(player.isInsideVehicle()){
			if(ent.getCustomName().equals(GlobalVariables.ChairName)){
				e.setCancelled(true);
				player.sendMessage(ChatColor.WHITE + "You" + ChatColor.RED + " can't " + ChatColor.WHITE + "teleport while sitting!");
				}
			}
		}catch(Exception ex){}
	}
}
