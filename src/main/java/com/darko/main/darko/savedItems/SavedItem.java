package com.darko.main.darko.savedItems;

import com.darko.main.AlttdUtility;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Data
public class SavedItem {

    private int ID;
    private ItemStack itemStack;

    public SavedItem(int ID, ItemStack itemStack) {
        this.ID = ID;
        this.itemStack = itemStack;
    }

    private static final List<SavedItem> savedItems = new ArrayList<>();

    public static void loadSavedItems() {

        savedItems.clear();

        for (String string : AlttdUtility.getInstance().getConfig().getKeys(true)) {

            if (!string.startsWith("SavedItems.")) continue;

            int ID;
            try {
                ID = Integer.parseInt(string.substring(11));
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                continue;
            }

            ItemStack itemStack = AlttdUtility.getInstance().getConfig().getItemStack(string);
            if (itemStack == null) continue;

            savedItems.add(new SavedItem(ID, itemStack));

        }
    }

    public static List<SavedItem> getSavedItems() {
        return savedItems;
    }

    public static SavedItem getSavedItemByID(int ID) {
        for (SavedItem savedItem : savedItems) {
            if (savedItem.getID() == ID) return savedItem;
        }
        return null;
    }

}
