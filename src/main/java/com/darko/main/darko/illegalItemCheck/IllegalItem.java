package com.darko.main.darko.illegalItemCheck;

import lombok.Data;

@Data
public class IllegalItem {

    private String name;
    private String itemMaterial;
    private String itemName;
    private String itemLore;
    private String itemEnchant;

    public IllegalItem(String name, String itemMaterial, String itemName, String itemLore, String itemEnchant) {
        this.name = name;
        this.itemMaterial = itemMaterial;
        this.itemName = itemName;
        this.itemLore = itemLore;
        this.itemEnchant = itemEnchant;
    }

}
