package com.darko.utilities.plugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class onPlayerQuit implements Listener {
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e){
	try{if(e.getPlayer().isInsideVehicle()){
		if(e.getPlayer().getVehicle().getCustomName().equals(GlobalVariables.ChairName)){
		   e.getPlayer().getVehicle().eject();
		   }}}catch(Exception ex){}
	GlobalVariables.chairEnabled.put(e.getPlayer().getUniqueId(), false);
	}
}
