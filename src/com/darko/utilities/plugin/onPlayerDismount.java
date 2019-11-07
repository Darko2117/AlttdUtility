package com.darko.utilities.plugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityDismountEvent;

public class onPlayerDismount implements Listener {
	
	@EventHandler
	public void onDismount(EntityDismountEvent e){
	if(e.getDismounted().getCustomName().equals(GlobalVariables.PigName)){
	e.getDismounted().remove();
	}}}
