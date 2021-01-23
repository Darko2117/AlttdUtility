package com.darko.main.utilities.teri.Nicknames;

import com.darko.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.darko.main.utilities.teri.Nicknames.Nicknames.format;

public class NicknamesGui implements Listener {
    private Inventory inv;
    private String inventoryOwnerName;
    private int currentPage;

    public NicknamesGui(String inventoryOwnerName) {
        this.inventoryOwnerName = inventoryOwnerName;
        // Create a new inventory, with no owner (as this isn't a real inventory)
        inv = Bukkit.createInventory(null, 36, "Nicknames Page ");

        // Put the items into the inventory
        currentPage = 1;
        setItems(currentPage);
    }

    public void setItems(int currentPage) {
        inv.clear();
        ArrayList<Nick> nicks = DatabaseQueries.getNicknamesList(currentPage*9-9, currentPage*9-1);

        if (currentPage != 1){
            inv.setItem(28, createGuiItem(Material.PAPER, "§bPrevious page",
                    "§aCurrent page: %page%".replace("%page%", String.valueOf(currentPage)),
                    "§aPrevious page: %previousPage%".replace("%previousPage%", String.valueOf(currentPage - 1))));
        }

        if (DatabaseQueries.hasNextPage(currentPage)) {
            inv.setItem(36, createGuiItem(Material.PAPER, "§bNext page",
                    "§aCurrent page: %page%".replace("%page%", String.valueOf(currentPage)),
                    "§aNext page: §b%nextPage%".replace("%nextPage%", String.valueOf(currentPage + 1))));
        }

        if (nicks != null) {
            for (int i = 0; i < nicks.size(); i++) {
                inv.setItem(i, createPlayerSkull(nicks.get(i), Main.getInstance().getConfig().getStringList("Nicknames.Lore")));
            }
        }
    }

    private ItemStack createPlayerSkull(Nick nick, List<String> lore){
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(nick.getUuid());

        meta.setOwningPlayer(offlinePlayer);
        meta.setDisplayName(offlinePlayer.getName());

        for (int ii = 0; ii < lore.size(); ii++){
            lore.set(ii, format(lore.get(ii)
                    .replace("%newNick%", nick.getNewNick())
                    .replace("%oldNick%", nick.getOldNick())
                    .replace("%lastChanged%", nick.getLastChanged())));
        }

        meta.setLore(lore);
        playerHead.setItemMeta(meta);

        return playerHead;
    }

    // Nice little method to create a gui item with a custom name, and description
    private ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(name);

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    // You can open the inventory with this
    public void openInventory(final HumanEntity ent) {
        setItems(1);
        ent.openInventory(inv);
    }

    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (e.getInventory() != inv) return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        final Player p = (Player) e.getWhoClicked();

        if (clickedItem.getType().equals(Material.PAPER)){
            if (clickedItem.getItemMeta().getDisplayName().equals("Next Page")){
                setItems(currentPage + 1);
            }
        } else {
            SkullMeta meta = (SkullMeta) clickedItem.getItemMeta();
            OfflinePlayer owningPlayer = meta.getOwningPlayer();

            if (owningPlayer == null){
                e.getWhoClicked().sendMessage(format(Main.getInstance().getConfig().getString("Messages.NickUserNotFound")));
                return;
            }
            Nick nick = DatabaseQueries.getNickChange(owningPlayer.getUniqueId());

            if (nick == null){
                e.getWhoClicked().sendMessage(format(Main.getInstance().getConfig().getString("Messages.NickAlreadyHandled")));
                return;
            }

            if (e.isLeftClick() && clickedItem.getType().equals(Material.PLAYER_HEAD)){
                Nicknames.getInstance().setNick(owningPlayer.getPlayer(), nick.getNewNick());
                if (owningPlayer.isOnline()){
                    owningPlayer.getPlayer().sendMessage(format(Main.getInstance().getConfig().getString("Messages.NickChanged")
                            .replace("%nickname%", nick.getNewNick())));
                    e.getWhoClicked().sendMessage(format(Main.getInstance().getConfig().getString("Messages.NickAccepted")
                            .replace("%targetPlayer%", owningPlayer.getName())
                            .replace("%newNick%", nick.getNewNick())
                            .replace("oldNick", nick.getOldNick())));
                }
            } else if (e.isRightClick() && clickedItem.getType().equals(Material.PLAYER_HEAD)){
                DatabaseQueries.denyNewNickname(owningPlayer.getUniqueId());
                if (owningPlayer.hasPlayedBefore()) {
                    e.getWhoClicked().sendMessage(format(Main.getInstance().getConfig().getString("Messages.NickDenied")
                            .replace("%targetPlayer%", owningPlayer.getName())
                            .replace("%newNick%", nick.getNewNick())
                            .replace("oldNick", nick.getOldNick())));
                }
            }
        }

        // Using slots click is a best option for your inventory click's
        p.sendMessage("You clicked at slot " + e.getRawSlot());
    }

    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory() == inv) {
            e.setCancelled(true);
        }
    }
}
