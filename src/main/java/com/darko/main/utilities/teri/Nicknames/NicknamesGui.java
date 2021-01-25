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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.darko.main.utilities.teri.Nicknames.Nicknames.format;

public class NicknamesGui implements Listener {

    private Inventory inv;
    private int currentPage;

    public NicknamesGui() {
        // Create a new inventory, with no owner (as this isn't a real inventory)
        inv = Bukkit.createInventory(null, 36, "Nicknames GUI");

        // Put the items into the inventory
        currentPage = 1;
        setItems(currentPage);
    }

    public void setItems(int currentPage) {
        new BukkitRunnable() {
            @Override
            public void run() {
                inv.clear();
                Utilities.updateCache();
                boolean hasNextPage = false;
                int i = (currentPage-1)*27; //TODO set to 1 or 2 to test
                int limit = i/27;

                for (Nick nick : Nicknames.getInstance().NickCache.values()){
                    if (nick.hasRequest()){
                        if (limit >= i/27) {
                            inv.setItem(i % 27, createPlayerSkull(nick, Main.getInstance().getConfig().getStringList("Nicknames.Lore")));
                            i++;
                        } else {
                            hasNextPage = true;
                            break;
                        }
                    }
                }

                if (currentPage != 1){
                    inv.setItem(28, createGuiItem(Material.PAPER, "§bPrevious page",
                            "§aCurrent page: %page%".replace("%page%", String.valueOf(currentPage)),
                            "§aPrevious page: %previousPage%".replace("%previousPage%", String.valueOf(currentPage - 1))));
                }

                if (hasNextPage) {
                    inv.setItem(36, createGuiItem(Material.PAPER, "§bNext page",
                            "§aCurrent page: %page%".replace("%page%", String.valueOf(currentPage)),
                            "§aNext page: §b%nextPage%".replace("%nextPage%", String.valueOf(currentPage + 1))));
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
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
                    .replace("%oldNick%", nick.getCurrentNick() == null ? "None" : nick.getCurrentNick())
                    .replace("%lastChanged%", nick.getLastChangedDate() == 0 ? "Not Applicable" : nick.getLastChangedDateFormatted())));
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
    public void openInventory(final HumanEntity ent) {//Possibly with a boolean to show if it should get from cache or update cache
        ent.openInventory(inv);
    }

    // Check for clicks on items

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (e.getInventory() != inv) return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        final Player p = (Player) e.getWhoClicked();

        if (clickedItem.getType().equals(Material.PAPER)){
            if (clickedItem.getItemMeta().getDisplayName().equals("Next Page")){
                setItems(currentPage + 1);
            }
        } else if (clickedItem.getType().equals(Material.PLAYER_HEAD)){
            SkullMeta meta = (SkullMeta) clickedItem.getItemMeta();
            if (meta.hasEnchants()){
                return;
            }
            OfflinePlayer owningPlayer = meta.getOwningPlayer();

            if (owningPlayer == null){
                p.sendMessage(format(Main.getInstance().getConfig().getString("Messages.NickUserNotFound")));
                return;
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    Utilities.updateCache();

                    Nick nick;
                    UUID uniqueId = owningPlayer.getUniqueId();
                    if (Nicknames.getInstance().NickCache.containsKey(uniqueId)){
                        nick = Nicknames.getInstance().NickCache.get(uniqueId);
                    } else {
                        nick = DatabaseQueries.getNick(uniqueId);
                    }

                    if (nick == null || !nick.hasRequest()){
                        p.sendMessage(format(Main.getInstance().getConfig().getString("Messages.NickAlreadyHandled")));
                        return;
                    }

                    if (e.isLeftClick()){
                        if (owningPlayer.hasPlayedBefore()) {
                            DatabaseQueries.acceptNewNickname(uniqueId, nick.getNewNick());
                            p.sendMessage(format(Main.getInstance().getConfig().getString("Messages.NickAccepted")
                                    .replace("%targetPlayer%", clickedItem.getItemMeta().getDisplayName())
                                    .replace("%newNick%", nick.getNewNick())
                                    .replace("%oldNick%", nick.getCurrentNick() == null ? clickedItem.getItemMeta().getDisplayName() : nick.getCurrentNick())));

                            if (owningPlayer.isOnline()){
                                Nicknames.getInstance().setNick(owningPlayer.getPlayer(), nick.getNewNick());
                                owningPlayer.getPlayer().sendMessage(format(Main.getInstance().getConfig().getString("Messages.NickChanged")
                                        .replace("%nickname%", nick.getNewNick())));

                            } else {
                                Utilities.bungeeMessageHandled(uniqueId, e.getWhoClicked().getServer().getPlayer(e.getWhoClicked().getName()), "Accepted");
                            }

                            nick.setCurrentNick(nick.getNewNick());
                            nick.setLastChangedDate(new Date().getTime());
                            nick.setNewNick(null);
                            nick.setRequestedDate(0);

                            Nicknames.getInstance().NickCache.put(uniqueId, nick);

                            ItemStack itemStack = new ItemStack(Material.SKELETON_SKULL);
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            itemMeta.setDisplayName(clickedItem.getItemMeta().getDisplayName());
                            itemMeta.setLore(clickedItem.getLore());
                            itemStack.setItemMeta(itemMeta);
                            e.getInventory().setItem(e.getSlot(), itemStack);
                            p.updateInventory();
                        } else {
                            p.sendMessage(format(Main.getInstance().getConfig().getString("Messages.CantFindPlayer")
                                    .replace("%playerName%", clickedItem.getItemMeta().getDisplayName())));
                        }

                    } else if (e.isRightClick()){
                        if (owningPlayer.hasPlayedBefore()) {
                            DatabaseQueries.denyNewNickname(uniqueId);
                            p.sendMessage(format(Main.getInstance().getConfig().getString("Messages.NickDenied")
                                    .replace("%targetPlayer%", owningPlayer.getName())
                                    .replace("%newNick%", nick.getNewNick())
                                    .replace("%oldNick%", nick.getCurrentNick() == null ? owningPlayer.getName() : nick.getCurrentNick())));

                            if (Nicknames.getInstance().NickCache.containsKey(uniqueId)
                                    && Nicknames.getInstance().NickCache.get(uniqueId).getCurrentNick() != null){
                                nick.setNewNick(null);
                                nick.setRequestedDate(0);
                                Nicknames.getInstance().NickCache.put(uniqueId, nick);
                            } else {
                                Nicknames.getInstance().NickCache.remove(uniqueId);
                            }

                            if (owningPlayer.isOnline()) {
                                Utilities.bungeeMessageHandled(uniqueId, e.getWhoClicked().getServer().getPlayer(e.getWhoClicked().getName()), "Denied");
                            }

                            ItemStack itemStack = new ItemStack(Material.SKELETON_SKULL);
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            itemMeta.setDisplayName(clickedItem.getItemMeta().getDisplayName());
                            itemMeta.setLore(clickedItem.getLore());
                            itemStack.setItemMeta(itemMeta);
                            e.getInventory().setItem(e.getSlot(), itemStack);
                            p.updateInventory();
                        } else {
                            p.sendMessage(format(Main.getInstance().getConfig().getString("Messages.CantFindPlayer")
                                    .replace("%playerName%", clickedItem.getItemMeta().getDisplayName())));
                        }
                    }
                }
            }.runTaskAsynchronously(Main.getInstance());
        }
    }

    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory() == inv) {
            e.setCancelled(true);
        }
    }
}
