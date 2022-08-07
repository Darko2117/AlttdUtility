package com.darko.main.darko.balanceStats.balStats.objects;

import lombok.Data;
import org.bukkit.Material;

@Data
public class VillagerShopLogObject{

    private Material itemMaterial;
    private int itemAmount;
    private double balanceChange;
    private long time;

    public VillagerShopLogObject(Material itemMaterial, int itemAmount, double balanceChange, long time) {
        this.itemMaterial = itemMaterial;
        this.itemAmount = itemAmount;
        this.balanceChange = balanceChange;
        this.time = time;
    }

    public int getDaysAgo() {
        return (int) ((System.currentTimeMillis() - time) / 86400000L);
    }

}
