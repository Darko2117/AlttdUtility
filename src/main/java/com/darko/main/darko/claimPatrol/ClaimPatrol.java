package com.darko.main.darko.claimPatrol;

import com.darko.main.AlttdUtility;
import com.darko.main.common.Methods;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClaimPatrol implements CommandExecutor, TabCompleter {

    static List<PatrolObject> cachedPatrolObjects = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        new BukkitRunnable() {
            @Override
            public void run() {

                if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.ClaimPatrolCommand"))
                    return;

                if (!(sender instanceof Player)) {
                    new Methods().sendConfigMessage(sender, "Messages.PlayerOnlyCommandMessage");
                    return;
                }

                Player searchingPlayer = (Player) sender;

                if (args.length < 2) {
                    new Methods().sendConfigMessage(sender, "Messages.InvalidUsageClaimPatrolCommand");
                    return;
                }

                PatrolMode mode = null;
                if (args[0].equals("owner")) mode = PatrolMode.OWNER;
                else if (args[0].equals("trust")) mode = PatrolMode.TRUST;
                if (mode == null) {
                    new Methods().sendConfigMessage(sender, "Messages.InvalidUsageClaimPatrolCommand");
                    return;
                }

                String playerName = args[1];
                OfflinePlayer searchedPlayer = Bukkit.getOfflinePlayer(playerName);
                if (!searchedPlayer.isOnline() && !searchedPlayer.hasPlayedBefore()) {
                    new Methods().sendConfigMessage(sender, "Messages.PlayerHasNotJoinedBefore");
                    return;
                }

                Integer claimIndex = -1;
                try {
                    claimIndex = Integer.parseInt(args[2]);
                } catch (Throwable ignored) {
                }

                PatrolMode finalMode = mode;
                Integer finalClaimIndex = claimIndex;

                PatrolObject patrolObject = getNextClaimToPatrol(searchingPlayer, searchedPlayer, finalMode, finalClaimIndex);

                if (patrolObject.getClaims().isEmpty()) {
                    new Methods().sendConfigMessage(sender, "Messages.NoClaimsToPatrol");
                    return;
                }

                YamlConfiguration claim = patrolObject.getClaims().get(patrolObject.getLastVisitedClaimIndex());

                String[] lesserCornerValues = claim.getString("Lesser Boundary Corner").split(";");
                Location lesserCorner = new Location(Bukkit.getWorld(lesserCornerValues[0]), Double.parseDouble(lesserCornerValues[1]), Double.parseDouble(lesserCornerValues[2]), Double.parseDouble(lesserCornerValues[3]));

                String[] greaterCornerValues = claim.getString("Greater Boundary Corner").split(";");
                Location greaterCorner = new Location(Bukkit.getWorld(greaterCornerValues[0]), Double.parseDouble(greaterCornerValues[1]), Double.parseDouble(greaterCornerValues[2]), Double.parseDouble(greaterCornerValues[3]));

                Double middleX, middleY, middleZ;

                middleX = (lesserCorner.getX() + greaterCorner.getX()) / 2;
                middleY = (lesserCorner.getY() + greaterCorner.getY()) / 2;
                middleZ = (lesserCorner.getZ() + greaterCorner.getZ()) / 2;

                Location centerOfClaim = new Location(lesserCorner.getWorld(), middleX, middleY, middleZ);

                if (centerOfClaim.getY() <= 0) centerOfClaim.setY(1);
                while (!Bukkit.getWorld(centerOfClaim.getWorld().getName()).getBlockAt(centerOfClaim).getBlockData().getMaterial().isAir()) {
                    centerOfClaim.setY(centerOfClaim.getY() + 1);
                }

                String firstPartOfMessage = null;
                if (patrolObject.getMode().equals(PatrolMode.OWNER))
                    firstPartOfMessage = ChatColor.GOLD + "Patrolling " + ChatColor.AQUA + searchedPlayer.getName() + ChatColor.GOLD + "'s claims.";
                if (patrolObject.getMode().equals(PatrolMode.TRUST))
                    firstPartOfMessage = ChatColor.GOLD + "Patrolling claims in which " + ChatColor.AQUA + searchedPlayer.getName() + ChatColor.GOLD + " is trusted.";
                searchingPlayer.sendMessage(firstPartOfMessage + ChatColor.GOLD + " Teleported to " + (patrolObject.getLastVisitedClaimIndex() + 1) + "/" + patrolObject.getClaims().size() + ChatColor.GOLD + ".");

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        searchingPlayer.teleport(centerOfClaim);
                    }
                }.runTask(AlttdUtility.getInstance());

            }
        }.runTaskAsynchronously(AlttdUtility.getInstance());

        return true;

    }

    private PatrolObject getNextClaimToPatrol(Player searchingPlayer, OfflinePlayer searchedPlayer, PatrolMode mode, Integer optionalClaimIndex) {

        if (!AlttdUtility.getInstance().getConfig().getBoolean("FeatureToggles.ClaimPatrolCommand")) return null;

        List<YamlConfiguration> claimsYamlConfigurationList = new ArrayList<>();

        for (File file : new File(AlttdUtility.getInstance().getConfig().getString("NumberOfClaimsFlag.ClaimDataDirectory")).listFiles()) {

            if (!file.getName().contains(".yml")) continue;

            YamlConfiguration claimYamlConfiguration = YamlConfiguration.loadConfiguration(file);

            if (mode.equals(PatrolMode.OWNER)) {

                String ownerUUID = claimYamlConfiguration.getString("Owner");

                if (ownerUUID.isEmpty()) continue;

                if (ownerUUID.equals(searchedPlayer.getUniqueId().toString()))
                    claimsYamlConfigurationList.add(claimYamlConfiguration);

            } else if (mode.equals(PatrolMode.TRUST)) {

                List<String> trustedUUIDs = new ArrayList<>();

                trustedUUIDs.addAll(claimYamlConfiguration.getStringList("Builders"));
                trustedUUIDs.addAll(claimYamlConfiguration.getStringList("Containers"));
                trustedUUIDs.addAll(claimYamlConfiguration.getStringList("Accessors"));
                trustedUUIDs.addAll(claimYamlConfiguration.getStringList("Managers"));

                if (!trustedUUIDs.contains(searchedPlayer.getUniqueId().toString())) continue;

                claimsYamlConfigurationList.add(claimYamlConfiguration);

            }

        }

        for (PatrolObject patrolObject : cachedPatrolObjects) {

            if (!patrolObject.getMode().equals(mode)) continue;
            if (!patrolObject.getSearchingPlayer().equals(searchingPlayer)) continue;
            if (!patrolObject.getSearchedPlayer().equals(searchedPlayer)) continue;

            if (optionalClaimIndex == -1) {
                if (patrolObject.getLastVisitedClaimIndex() + 1 < patrolObject.getClaims().size()) {
                    patrolObject.setLastVisitedClaimIndex(patrolObject.getLastVisitedClaimIndex() + 1);
                } else {
                    patrolObject.setLastVisitedClaimIndex(0);
                }
            } else {
                optionalClaimIndex--;
                if (!(optionalClaimIndex > -1 && optionalClaimIndex < patrolObject.getClaims().size()))
                    patrolObject.setLastVisitedClaimIndex(0);
                else patrolObject.setLastVisitedClaimIndex(optionalClaimIndex);
            }

            patrolObject.setClaims(claimsYamlConfigurationList);

            return patrolObject;

        }

        PatrolObject patrolObject = new PatrolObject(searchingPlayer, searchedPlayer, mode, claimsYamlConfigurationList, 0);

        cachedPatrolObjects.add(patrolObject);

        return patrolObject;

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        List<String> results = new ArrayList<>();
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            results.add("owner");
            results.add("trust");
            for (String result : results) {
                if (result.contains(args[0].toLowerCase())) {
                    completions.add(result);
                }
            }
        }

        if (args.length == 2) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                results.add(player.getName());
            }
            for (String result : results) {
                if (result.toLowerCase().contains(args[1].toLowerCase())) {
                    completions.add(result);
                }
            }
        }

        if (args.length == 3) {
            for (Integer i = 1; i <= 9; i++) {
                completions.add(i.toString());
            }
            if (!args[2].isEmpty()) completions.clear();
        }

        return completions;

    }

    private class PatrolObject {

        Player searchingPlayer;
        OfflinePlayer searchedPlayer;
        PatrolMode mode;
        List<YamlConfiguration> claims;
        Integer lastVisitedClaimIndex;

        public PatrolObject(Player searchingPlayer, OfflinePlayer searchedPlayer, PatrolMode mode, List<YamlConfiguration> claims, Integer lastVisitedClaimIndex) {
            this.searchingPlayer = searchingPlayer;
            this.searchedPlayer = searchedPlayer;
            this.mode = mode;
            this.claims = claims;
            this.lastVisitedClaimIndex = lastVisitedClaimIndex;
        }

        public Player getSearchingPlayer() {
            return searchingPlayer;
        }

        public void setSearchingPlayer(Player searchingPlayer) {
            this.searchingPlayer = searchingPlayer;
        }

        public OfflinePlayer getSearchedPlayer() {
            return searchedPlayer;
        }

        public void setSearchedPlayer(OfflinePlayer searchedPlayer) {
            this.searchedPlayer = searchedPlayer;
        }

        public PatrolMode getMode() {
            return mode;
        }

        public void setMode(PatrolMode mode) {
            this.mode = mode;
        }

        public List<YamlConfiguration> getClaims() {
            return claims;
        }

        public void setClaims(List<YamlConfiguration> claims) {
            this.claims = claims;
        }

        public Integer getLastVisitedClaimIndex() {
            return lastVisitedClaimIndex;
        }

        public void setLastVisitedClaimIndex(Integer lastVisitedClaimIndex) {
            this.lastVisitedClaimIndex = lastVisitedClaimIndex;
        }

    }

    enum PatrolMode {

        OWNER,
        TRUST

    }

}
