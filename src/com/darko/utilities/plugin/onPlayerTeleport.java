package com.darko.utilities.plugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class onPlayerTeleport implements Listener {

	@EventHandler
	public static void onTeleport(PlayerTeleportEvent e){
		String Ystring = String.valueOf(e.getTo().getY());
		Ystring = Ystring.substring(Ystring.indexOf("."), Ystring.length());
		e.getPlayer().sendMessage(Ystring);
		if(Ystring == ".9375"){
			e.getTo().setY(e.getTo().getY() + 0.0625);
			e.getPlayer().sendMessage(e.getTo().toString());
		}
	}
}
