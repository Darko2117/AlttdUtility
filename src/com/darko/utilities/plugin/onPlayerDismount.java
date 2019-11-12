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
	try{if(e.getDismounted().getCustomName().equals(GlobalVariables.ChairName)){
		Entity ent = e.getDismounted();
		new BukkitRunnable() {
		public void run() {
		ent.remove();
		GlobalVariables.aliveSeats.remove(e.getEntity().getLocation());
		GlobalVariables.occupiedSeats.remove(e.getEntity().getUniqueId());
		}}.runTask(this.main);
		}}catch(Exception ex){}}}
