package com.darko.main.cosmetics.chair;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class GlobalVariables {

	public static HashMap<UUID, Boolean> chairEnabled = new HashMap<>();
	public static HashMap<UUID, Location> occupiedSeats = new HashMap<>();
	public static HashMap<Location, Entity> aliveSeats = new HashMap<>();

	public static String ChairName = "There is a 16 character limit on a name tag";
	public static String OldChairName = "There is a 16 character limit on a name tag so someone naming a pig this is impossible";
}
