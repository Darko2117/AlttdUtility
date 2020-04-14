package com.darko.main.utilities.drops;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class onIronGolemDeath implements Listener {

    @EventHandler
    public void onGolemDeath(EntityDeathEvent e) {

        if (e.getEntity().getType().equals(EntityType.IRON_GOLEM)) {

            e.getDrops().clear();

            List<ItemStack> drops = new ArrayList<>();

            ItemStack iron = new ItemStack(Material.IRON_INGOT);
            iron.setAmount(1);

            ItemStack poppy = new ItemStack(Material.POPPY);
            iron.setAmount(1);

            Random r = new Random();
            float chance = r.nextFloat();

            if (chance <= 0.25f)
                drops.add(iron);
            if (chance <= 0.15f)
                drops.add(poppy);

            e.getDrops().addAll(drops);
        }
    }
}
