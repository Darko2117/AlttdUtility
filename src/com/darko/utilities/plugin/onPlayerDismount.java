package com.darko.utilities.plugin;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.entity.EntityDismountEvent;

public class onPlayerDismount implements Listener {
	private Main main;
	public onPlayerDismount(Main main){
		this.main = main;
	}
	@EventHandler
	public void onDismount(EntityDismountEvent e){
	try{Entity ent = e.getDismounted();
		if(ent.getCustomName().equals(GlobalVariables.ChairName)){
		new BukkitRunnable(){
		public void run(){
		ent.remove();
		GlobalVariables.aliveSeats.remove(e.getEntity().getLocation());
        GlobalVariables.occupiedSeats.remove(e.getEntity().getUniqueId());
		}}.runTaskAsynchronously(this.main);}}catch(Exception ex){}}}
