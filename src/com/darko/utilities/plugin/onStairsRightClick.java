package com.darko.utilities.plugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Stairs;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;


public class onStairsRightClick implements Listener {
	Integer OneClick = 0;
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onRightClick(PlayerInteractEvent e){
	if(OneClick == 0){
	
	Player player = e.getPlayer();
	if(e.getAction() == Action.RIGHT_CLICK_BLOCK && player.getInventory().getItemInMainHand().getType() == Material.AIR){
	
	if(!GlobalVariables.chairEnabled.containsKey(player.getUniqueId())){
		GlobalVariables.chairEnabled.put(player.getUniqueId(), false);}
	
	Location blockloc = e.getClickedBlock().getLocation();
	Material[] bannedMaterialsBelow = {Material.AIR, Material.LAVA, Material.FIRE};
	Material[] allowedMaterialsAbove = {Material.AIR, Material.WALL_SIGN, Material.ITEM_FRAME};
	Boolean bannedMaterialFound = false;
	Boolean allowedMaterialFould = false;
	
	
	if(	e.getClickedBlock().getType().toString().contains("STAIRS") &&
		GlobalVariables.chairEnabled.get(player.getUniqueId()) &&
		!e.getClickedBlock().getState().getData().toString().contains("inverted") &&
		!player.isGliding() &&
		!GlobalVariables.occupiedSeats.containsValue(e.getClickedBlock().getLocation())){
		
	for(Integer i = 0; i < bannedMaterialsBelow.length; i++){
		if(bannedMaterialsBelow[i].equals(Bukkit.getWorld(player.getWorld().getName()).getBlockAt(blockloc.clone().subtract(0, 1, 0)).getType())){
		bannedMaterialFound = true;}}
	for(Integer i = 0; i < allowedMaterialsAbove.length; i++){
		if(allowedMaterialsAbove[i].equals(Bukkit.getWorld(player.getWorld().getName()).getBlockAt(blockloc.clone().add(0, 1, 0)).getType())){
			allowedMaterialFould = true;}}
	
	if(!bannedMaterialFound && allowedMaterialFould){
	
	Boolean ClaimPerms = false;
	Boolean ClaimExists = false;
	Boolean RegionPerms = false;
	Claim claim = null;
	try{claim = GriefPrevention.instance.dataStore.getClaimAt(e.getClickedBlock().getLocation(), true, null);}catch(Exception ex){}
	
	
	
	com.sk89q.worldedit.util.Location blockloc1 = BukkitAdapter.adapt(e.getClickedBlock().getLocation());
	LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
	RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
	RegionQuery query = container.createQuery();
	
	
	if(query.testState(blockloc1, localPlayer, GlobalVariables.SIT)){RegionPerms = true;}
	
	try{
	if(claim.allowAccess(player) == null){
	ClaimPerms = true; 
	ClaimExists = true;
	}else if(claim.allowAccess(player) != null){
	ClaimPerms = false;
	ClaimExists = true;
	}else{ClaimExists = false;}
	}catch(Exception ex){}
	
	
	if((RegionPerms && !ClaimExists) || (RegionPerms && ClaimPerms)){
	Location location = e.getClickedBlock().getLocation();
	Stairs stairs = (Stairs) e.getClickedBlock().getState().getData();
	BlockFace facing = stairs.getFacing();
	Vector direction = facing.getDirection();
	location.setDirection(direction);
	ArmorStand chair = (ArmorStand) Bukkit.getWorld(player.getWorld().getName()).spawnEntity(location.clone().add(0.5, -1.25, 0.5), EntityType.ARMOR_STAND);
	chair.setInvulnerable(true);
	chair.setCustomName(GlobalVariables.ChairName);
	chair.setGravity(false);
	chair.setVisible(false);
	chair.setBasePlate(false);
	chair.setArms(true);
	chair.setLeftArmPose(new EulerAngle(Math.toRadians(0), Math.toRadians(0), Math.toRadians(236)));
	chair.setRightArmPose(new EulerAngle(Math.toRadians(0), Math.toRadians(0), Math.toRadians(220)));
	chair.setLeftLegPose(new EulerAngle(Math.toRadians(0), Math.toRadians(0), Math.toRadians(340)));
	chair.setRightLegPose(new EulerAngle(Math.toRadians(0), Math.toRadians(0), Math.toRadians(20)));
	chair.addPassenger(player);
	
	GlobalVariables.occupiedSeats.put(player.getUniqueId(), e.getClickedBlock().getLocation());
	GlobalVariables.aliveSeats.put(e.getClickedBlock().getLocation(), chair);
	
	}else if(!ClaimPerms && ClaimExists){player.sendMessage(ChatColor.RED + "You don't have " + claim.getOwnerName() + "'s permission to use that.");}
	else if(!RegionPerms && !ClaimPerms){player.sendMessage(ChatColor.RED + "You can't sit in this region.");}
	}else{player.sendMessage(ChatColor.RED + "Invalid block found above/below the stairs.");}}
	OneClick++;
	
	}}else{OneClick = 0;}
	}}
