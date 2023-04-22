package com.darko.main.darko.balanceStats.balStats;

import com.darko.main.AlttdUtility;
import com.darko.main.common.database.Database;
import com.darko.main.darko.balanceStats.balStats.objects.VillagerShopLogObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BalStatsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        new BukkitRunnable() {
            @Override
            public void run() {
                try {

                    if (!(commandSender instanceof Player player))
                        return;

                    if (Database.connection == null)
                        return;

                    int numberOfDays = 7;
                    if (strings.length != 0)
                        numberOfDays = Integer.parseInt(strings[0]);
                    if (numberOfDays < 0)
                        numberOfDays = 0;
                    else if (numberOfDays > 60)
                        numberOfDays = 60;

                    // 1 day = 86400000 ms
                    long earliestAllowedTimestamp = System.currentTimeMillis() - ((numberOfDays + 1) * 86400000L);

                    String statement = "SELECT * FROM villager_shop_log WHERE user_UUID = '" + player.getUniqueId() + "' AND time >= " + earliestAllowedTimestamp;
                    ResultSet resultSet = Database.connection.prepareStatement(statement).executeQuery();

                    List<VillagerShopLogObject> villagerShopLogObjectList = new ArrayList<>();

                    while (resultSet.next()) {
                        Material itemMaterial = Material.valueOf(resultSet.getString(4));
                        int itemAmount = resultSet.getInt(5);
                        double balanceChange = resultSet.getDouble(6);
                        long time = resultSet.getLong(7);

                        villagerShopLogObjectList.add(new VillagerShopLogObject(itemMaterial, itemAmount, balanceChange, time));
                    }

                    String text = ChatColor.GOLD + "Spawn merchants transaction total in the last " + ChatColor.YELLOW + numberOfDays + ChatColor.GOLD + " days:" + "\n";
                    text += ChatColor.GOLD + "Total Sell: " + ChatColor.GREEN + "$" + String.format("%,.2f", getTotalSell(villagerShopLogObjectList)) + "\n";
                    text += ChatColor.GOLD + "Total Buy: " + ChatColor.RED + "$" + String.format("%,.2f", getTotalBuy(villagerShopLogObjectList)) + "\n";
                    text += ChatColor.GOLD + "" + ChatColor.BOLD + "HOVER DAYS FOR MORE DETAILS: ";

                    TextComponent textComponent = Component.text(text);

                    for (int i = -1; i < numberOfDays + 1; i++) {

                        List<VillagerShopLogObject> dayVillagerShopLogObjectList;
                        if (i == -1) {
                            dayVillagerShopLogObjectList = villagerShopLogObjectList;
                        } else {
                            dayVillagerShopLogObjectList = getVillagerShopLogObjectListForDay(villagerShopLogObjectList, i);
                        }

                        TextComponent dayTextComponent;
                        if (i == -1) {
                            dayTextComponent = Component.text(ChatColor.YELLOW + "" + ChatColor.BOLD + "TOTAL");
                        } else {
                            dayTextComponent = Component.text(ChatColor.YELLOW + "" + ChatColor.BOLD + i);
                        }

                        String hoverText = "";
                        if (i == -1) {
                            hoverText += ChatColor.GOLD + "" + ChatColor.BOLD + "TOTAL\n";
                        } else if (i == 0) {
                            hoverText += ChatColor.GOLD + "" + ChatColor.BOLD + "TODAY\n";
                        } else if (i == 1) {
                            hoverText += ChatColor.GOLD + "" + ChatColor.BOLD + "1 DAY AGO\n";
                        } else {
                            hoverText += ChatColor.GOLD + "" + ChatColor.BOLD + i + " DAYS AGO\n";
                        }
                        hoverText += ChatColor.GOLD + "Total Sell: " + ChatColor.GREEN + "$" + String.format("%,.2f", getTotalSell(dayVillagerShopLogObjectList)) + "   ";
                        hoverText += ChatColor.GOLD + "Total Buy: " + ChatColor.RED + "$" + String.format("%,.2f", getTotalBuy(dayVillagerShopLogObjectList)) + "\n\n";

                        hoverText += ChatColor.GOLD + "" + ChatColor.BOLD + "SELLING\n";

                        int indexOnLine = 0;
                        HashMap<Material, Double> sellingHashMap = getSellHashMap(dayVillagerShopLogObjectList);
                        for (Map.Entry<Material, Double> entry : sellingHashMap.entrySet()) {

                            if (indexOnLine == 3) {
                                hoverText += "\n";
                                indexOnLine = 0;
                            } else if (indexOnLine != 0) {
                                hoverText += "   ";
                            }

                            hoverText += ChatColor.WHITE + entry.getKey().toString() + ChatColor.GOLD + "x" + ChatColor.WHITE + getSellCountForMaterial(dayVillagerShopLogObjectList, entry.getKey()) + ChatColor.GOLD + "=" + ChatColor.GREEN + "$" + String.format("%,.2f", entry.getValue());
                            indexOnLine++;

                        }

                        hoverText += "\n\n";
                        hoverText += ChatColor.GOLD + "" + ChatColor.BOLD + "BUYING\n";

                        indexOnLine = 0;
                        HashMap<Material, Double> buyingHashMap = getBuyHashMap(dayVillagerShopLogObjectList);
                        for (Map.Entry<Material, Double> entry : buyingHashMap.entrySet()) {

                            if (indexOnLine == 3) {
                                hoverText += "\n";
                                indexOnLine = 0;
                            } else if (indexOnLine != 0) {
                                hoverText += "   ";
                            }

                            hoverText += ChatColor.WHITE + entry.getKey().toString() + ChatColor.GOLD + "x" + ChatColor.WHITE + getBuyCountForMaterial(dayVillagerShopLogObjectList, entry.getKey()) + ChatColor.GOLD + "=" + ChatColor.RED + "$" + String.format("%,.2f", entry.getValue());
                            indexOnLine++;

                        }

                        dayTextComponent = dayTextComponent.hoverEvent(Component.text(hoverText));

                        textComponent = textComponent.append(dayTextComponent);
                        textComponent = textComponent.append(Component.text(" "));

                    }

                    player.sendMessage(textComponent);

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }.runTaskAsynchronously(AlttdUtility.getInstance());

        return true;

    }

    HashMap<Material, Double> getSellHashMap(List<VillagerShopLogObject> villagerShopLogObjectList) {

        HashMap<Material, Double> hashMap = new HashMap<>();

        for (VillagerShopLogObject villagerShopLogObject : villagerShopLogObjectList) {

            if (villagerShopLogObject.getBalanceChange() < 0)
                continue;

            Material material = villagerShopLogObject.getItemMaterial();
            Double balanceChange = villagerShopLogObject.getBalanceChange();

            if (hashMap.containsKey(material)) {
                hashMap.put(material, hashMap.get(material) + balanceChange);
            } else {
                hashMap.put(material, balanceChange);
            }

        }

        // Create a list from elements of HashMap
        List<Map.Entry<Material, Double>> list = new LinkedList<>(hashMap.entrySet());

        // Sort the list
        list.sort(Map.Entry.comparingByValue());
        Collections.reverse(list);

        // put data from sorted list to hashmap
        HashMap<Material, Double> temp = new LinkedHashMap<>();
        for (Map.Entry<Material, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }

        return temp;

    }

    HashMap<Material, Double> getBuyHashMap(List<VillagerShopLogObject> villagerShopLogObjectList) {

        HashMap<Material, Double> hashMap = new HashMap<>();

        for (VillagerShopLogObject villagerShopLogObject : villagerShopLogObjectList) {

            if (villagerShopLogObject.getBalanceChange() > 0)
                continue;

            Material material = villagerShopLogObject.getItemMaterial();
            Double balanceChange = villagerShopLogObject.getBalanceChange() * -1;

            if (hashMap.containsKey(material)) {
                hashMap.put(material, hashMap.get(material) + balanceChange);
            } else {
                hashMap.put(material, balanceChange);
            }

        }

        // Create a list from elements of HashMap
        List<Map.Entry<Material, Double>> list = new LinkedList<>(hashMap.entrySet());

        // Sort the list
        list.sort(Map.Entry.comparingByValue());
        Collections.reverse(list);

        // put data from sorted list to hashmap
        HashMap<Material, Double> temp = new LinkedHashMap<>();
        for (Map.Entry<Material, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }

        return temp;

    }

    int getSellCountForMaterial(List<VillagerShopLogObject> villagerShopLogObjectList, Material material) {

        int count = 0;

        for (VillagerShopLogObject villagerShopLogObject : villagerShopLogObjectList) {
            if (villagerShopLogObject.getBalanceChange() > 0 && villagerShopLogObject.getItemMaterial().equals(material)) {
                count += villagerShopLogObject.getItemAmount();
            }
        }

        return count;

    }

    int getBuyCountForMaterial(List<VillagerShopLogObject> villagerShopLogObjectList, Material material) {

        int count = 0;

        for (VillagerShopLogObject villagerShopLogObject : villagerShopLogObjectList) {
            if (villagerShopLogObject.getBalanceChange() < 0 && villagerShopLogObject.getItemMaterial().equals(material)) {
                count += villagerShopLogObject.getItemAmount();
            }
        }

        return count;

    }

    double getTotalSell(List<VillagerShopLogObject> villagerShopLogObjectList) {

        double totalSell = 0;

        for (VillagerShopLogObject villagerShopLogObject : villagerShopLogObjectList) {
            if (villagerShopLogObject.getBalanceChange() > 0) {
                totalSell += villagerShopLogObject.getBalanceChange();
            }
        }

        return totalSell;

    }

    double getTotalBuy(List<VillagerShopLogObject> villagerShopLogObjectList) {

        double totalBuy = 0;

        for (VillagerShopLogObject villagerShopLogObject : villagerShopLogObjectList) {
            if (villagerShopLogObject.getBalanceChange() < 0) {
                totalBuy += villagerShopLogObject.getBalanceChange();
            }
        }

        if (totalBuy == 0)
            return totalBuy;
        else
            return totalBuy * -1;

    }

    List<VillagerShopLogObject> getVillagerShopLogObjectListForDay(List<VillagerShopLogObject> villagerShopLogObjectList, int daysAgo) {

        List<VillagerShopLogObject> villagerShopLogObjectList1 = new ArrayList<>();

        for (VillagerShopLogObject villagerShopLogObject : villagerShopLogObjectList)
            if (villagerShopLogObject.getDaysAgo() == daysAgo)
                villagerShopLogObjectList1.add(villagerShopLogObject);

        return villagerShopLogObjectList1;

    }

}
