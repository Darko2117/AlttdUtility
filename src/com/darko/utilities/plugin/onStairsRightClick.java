package com.darko.utilities.plugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Stairs;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class onStairsRightClick implements Listener {
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent e){
	try{
	Player player = e.getPlayer();
	if(e.getClickedBlock().getType().toString().contains("STAIRS") && e.getAction() == Action.RIGHT_CLICK_BLOCK){
	if(GlobalVariables.chairEnabled.get(player.getUniqueId().toString()) == true){
	Location location = e.getClickedBlock().getLocation();
	location.setX(location.getX() + 0.5);
	location.setZ(location.getZ() + 0.5);
	location.setY(location.getY() - 0.5);
	Stairs stairs = (Stairs) e.getClickedBlock().getState().getData();
	BlockFace facing = stairs.getFacing();
	Vector direction = facing.getDirection();
	location.setDirection(direction);
	Pig chair = (Pig) Bukkit.getWorld(player.getWorld().getName()).spawnEntity(location, EntityType.PIG);
	chair.setInvulnerable(true);
	chair.setCustomName(GlobalVariables.PigName);
	chair.setGravity(false);
	chair.setAI(false);
	chair.setSilent(true);
	chair.setSaddle(true);
	chair.addPassenger(player);
	chair.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999999, 1, false, false));
	}}}catch(Exception e1){}}}
