package com.darko.main.utilities.permissionStuff;

import com.sk89q.worldedit.entity.Player;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.List;

public class ItemPickup implements Listener {

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent e){
        if(e.getEntity().getType().equals(EntityType.PLAYER)){
            if(e.getEntity().hasPermission("utility.no-pickup") && !e.getEntity().hasPermission("utility.no-pickup1")){
                e.setCancelled(true);
            }
        }
    }
}
