package com.darko.main.cosmetics.chair;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GlobalVariables {

    public static List<Player> chairEnabled = new ArrayList<>();
    public static HashMap<UUID, Location> occupiedSeats = new HashMap<>();
    public static HashMap<Location, Entity> aliveSeats = new HashMap<>();

    public static String ChairName = "There is a 16 character limit on a name tag";
}
