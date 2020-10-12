//package com.darko.main.cosmetics.chair;
//
//import com.darko.main.API.APIs;
//import com.darko.main.other.Methods;
//import com.darko.main.database.Database;
//import com.sk89q.worldedit.bukkit.BukkitAdapter;
//import com.sk89q.worldguard.LocalPlayer;
//import com.sk89q.worldguard.WorldGuard;
//import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
//import com.sk89q.worldguard.protection.regions.RegionContainer;
//import com.sk89q.worldguard.protection.regions.RegionQuery;
//import me.ryanhamshire.GriefPrevention.Claim;
//import me.ryanhamshire.GriefPrevention.GriefPrevention;
//import org.bukkit.Bukkit;
//import org.bukkit.ChatColor;
//import org.bukkit.Location;
//import org.bukkit.Material;
//import org.bukkit.block.BlockFace;
//import org.bukkit.block.data.type.Stairs;
//import org.bukkit.command.Command;
//import org.bukkit.command.CommandExecutor;
//import org.bukkit.command.CommandSender;
//import org.bukkit.entity.ArmorStand;
//import org.bukkit.entity.Entity;
//import org.bukkit.entity.EntityType;
//import org.bukkit.entity.Player;
//
//import com.darko.main.Main;
//import org.bukkit.event.Event;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.EventPriority;
//import org.bukkit.event.Listener;
//import org.bukkit.event.block.Action;
//import org.bukkit.event.block.BlockBreakEvent;
//import org.bukkit.event.player.PlayerCommandPreprocessEvent;
//import org.bukkit.event.player.PlayerInteractEvent;
//import org.bukkit.event.player.PlayerQuitEvent;
//import org.bukkit.event.world.ChunkLoadEvent;
//import org.bukkit.inventory.EquipmentSlot;
//import org.bukkit.scheduler.BukkitRunnable;
//import org.bukkit.util.Vector;
//import org.spigotmc.event.entity.EntityDismountEvent;
//
//import java.sql.ResultSet;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class Chair implements CommandExecutor, Listener {
//
//    static HashMap<Location, Entity> aliveSeats = new HashMap<>();
//    static String chairName = "There is a 35 character limit on a name tag.";
//
//    @Override
//    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
//
//        if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.Chair")) return true;
//
//        if (!(sender instanceof Player)) {
//            new Methods().sendConfigMessage(sender, "Messages.PlayerOnlyCommandMessage");
//            return true;
//        }
//
//        if (Database.connection == null) {
//            new Methods().sendConfigMessage(sender, "Messages.NoDatabaseConnectionMessage");
//            return true;
//        }
//
//        new BukkitRunnable() {
//            @Override
//            public void run() {
//                try {
//
//                    Player player = (Player) sender;
//                    String uuid = player.getUniqueId().toString();
//
//                    String statement = "SELECT * FROM users WHERE UUID = '" + uuid + "';";
//
//                    ResultSet rs = Database.connection.prepareStatement(statement).executeQuery();
//                    rs.next();
//
//                    Boolean chairEnabled = rs.getBoolean("chair_enabled");
//
//                    if (!chairEnabled) {
//
//                        statement = "UPDATE users SET chair_enabled = true WHERE UUID = '" + uuid + "';";
//                        Database.connection.prepareStatement(statement).executeUpdate();
//                        new Methods().sendConfigMessage(player, "Messages.ChairEnabled");
//
//                    } else {
//
//                        statement = "UPDATE users SET chair_enabled = false WHERE UUID = '" + uuid + "';";
//                        Database.connection.prepareStatement(statement).executeUpdate();
//                        new Methods().sendConfigMessage(player, "Messages.ChairDisabled");
//
//                    }
//
//                    Database.reloadLoadedValues();
//
//                } catch (Throwable throwable) {
//                    throwable.printStackTrace();
//                }
//            }
//        }.runTaskAsynchronously(Main.getInstance());
//
//        return true;
//
//    }
//
//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void onRightClick(PlayerInteractEvent event) {
//
//        if (event.useInteractedBlock().equals(Event.Result.DENY)) return;
//        if (event.useItemInHand().equals(Event.Result.DENY)) return;
//        if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.Chair")) return;
//
//        Player player = event.getPlayer();
//
//        if (Database.connection == null) return;
//
//        new BukkitRunnable() {
//            @Override
//            public void run() {
//
//                if (!handCheck(player, event)) return;
//                if (!chairEnabledCheck(player)) return;
//                if (!glidingCheck(player)) return;
//                if (!sneakingCheck(player)) return;
//                if (!stairsCheck(event)) return;
//                if (!occupiedCheck(event)) return;
//                if (!blocksCheck(player, event)) return;
//                if (!claimCheck(player, event)) return;
//                if (!regionCheck(player, event)) return;
//
//                new BukkitRunnable() {
//                    @Override
//                    public void run() {
//
//                        Location location = event.getClickedBlock().getLocation();
//                        Stairs stairs = (Stairs) event.getClickedBlock().getBlockData();
//                        BlockFace facing = stairs.getFacing();
//                        Vector direction = null;
//                        if (stairs.getFacing().equals(BlockFace.EAST)
//                                || stairs.getFacing().equals(BlockFace.WEST)) {
//                            direction = facing.getDirection().rotateAroundZ(180);
//                        } else if (stairs.getFacing().equals(BlockFace.NORTH)
//                                || stairs.getFacing().equals(BlockFace.SOUTH)) {
//                            direction = facing.getDirection().rotateAroundX(180);
//                        }
//                        location.setDirection(direction);
//                        ArmorStand chair = (ArmorStand) Bukkit.getWorld(player.getWorld().getName())
//                                .spawnEntity(location.clone().add(0.5, -1.25, 0.5), EntityType.ARMOR_STAND);
//                        chair.setVisible(false);
//                        chair.setInvulnerable(true);
//                        chair.setCustomName(chairName);
//                        chair.setGravity(false);
//                        chair.addPassenger(player);
//
//                        aliveSeats.put(event.getClickedBlock().getLocation(), chair);
//
//                    }
//                }.runTask(Main.getInstance());
//
//            }
//        }.runTaskAsynchronously(Main.getInstance());
//
//    }
//
//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void onBlockBreak(BlockBreakEvent event) {
//
//        if (event.isCancelled()) return;
//        if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.Chair")) return;
//
//        if (event.isCancelled()) return;
//
//        if (aliveSeats.containsKey(event.getBlock().getLocation())) {
//            if (event.getPlayer().hasPermission("utility.forcedismount")) {
//                aliveSeats.get(event.getBlock().getLocation()).eject();
//            } else {
//                event.setCancelled(true);
//            }
//        }
//
//    }
//
//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void onChunkLoad(ChunkLoadEvent event) {
//
//        if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.Chair")) return;
//
//        Entity[] entities = event.getChunk().getEntities();
//
//        for (Entity entity : entities) {
//
//            if (entity.getCustomName() == null) continue;
//            if (!entity.getCustomName().equals(chairName)) continue;
//            if (!entity.getPassengers().isEmpty()) continue;
//
//            for (Map.Entry<Location, Entity> entry : aliveSeats.entrySet()) {
//                if (entry.getValue().equals(entity)) {
//                    aliveSeats.remove(entry.getKey());
//                }
//            }
//            entity.remove();
//
//            Main.getInstance().getLogger().info(entity.getType() + " deleted at " + entity.getLocation() + " with onChunkLoad");
//
//        }
//
//    }
//
//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void onDismount(EntityDismountEvent event) {
//
//        if (event.isCancelled()) return;
//        if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.Chair")) return;
//
//        Entity dismounted = event.getDismounted();
//        Entity entity = event.getEntity();
//
//        if (dismounted.getCustomName() == null) return;
//        if (!dismounted.getCustomName().equals(chairName)) return;
//
//        for (Map.Entry<Location, Entity> entry : aliveSeats.entrySet()) {
//            if (entry.getValue().equals(dismounted)) {
//                aliveSeats.remove(entry.getKey());
//                break;
//            }
//        }
//        dismounted.remove();
//
//        new BukkitRunnable() {
//            public void run() {
//                if (entity.getVehicle() == null) {
//                    Location dismountLocation = entity.getLocation();
//                    dismountLocation.setY(dismountLocation.getY() + (1 - dismountLocation.getY() % 1));
//                    entity.teleport(dismountLocation);
//                }
//            }
//        }.runTaskLater(Main.getInstance(), 0);
//
//    }
//
//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void onPlayerLeave(PlayerQuitEvent event) {
//
//        if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.Chair")) return;
//
//        Player player = event.getPlayer();
//
//        if (!player.isInsideVehicle()) return;
//
//        if (player.getVehicle().getCustomName() == null) return;
//
//        if (player.getVehicle().getCustomName().equals(chairName)) {
//            player.getVehicle().eject();
//        }
//
//    }
//
//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void onTeleportCommand(PlayerCommandPreprocessEvent event) {
//
//        if (event.isCancelled()) return;
//        if (!Main.getInstance().getConfig().getBoolean("FeatureToggles.Chair")) return;
//
//        Player player = event.getPlayer();
//
//        if (!player.isInsideVehicle()) return;
//        if (player.getVehicle().getCustomName() == null) return;
//        if (!player.getVehicle().getCustomName().equals(chairName)) return;
//
//        String command = event.getMessage();
//        String[] tpCommands = {"/tp", "/ptp", "/spawn", "/warp", "/home", "/back"};
//
//        for (String tpCommand : tpCommands) {
//            if (command.startsWith(tpCommand)) {
//                event.setCancelled(true);
//                player.sendMessage(ChatColor.WHITE + "You " + ChatColor.RED + "can't" + ChatColor.WHITE + " teleport while sitting.");
//            }
//        }
//
//    }
//
//    Boolean handCheck(Player player, PlayerInteractEvent event) {
//
//        return event.getAction() == Action.RIGHT_CLICK_BLOCK
//                && player.getInventory().getItemInMainHand().getType() == Material.AIR && event.getHand().equals(EquipmentSlot.HAND);
//
//    }
//
//    Boolean chairEnabledCheck(Player player) {
//
//        return Database.chairEnabledPlayers.contains(player);
//
//    }
//
//    Boolean glidingCheck(Player player) {
//
//        return !player.isGliding();
//
//    }
//
//    Boolean sneakingCheck(Player player) {
//
//        return !player.isSneaking();
//
//    }
//
//    Boolean stairsCheck(PlayerInteractEvent event) {
//
//        String dataString = event.getClickedBlock().getBlockData().toString();
//        return dataString.contains("stairs") && !dataString.contains("half=top");
//
//    }
//
//    Boolean occupiedCheck(PlayerInteractEvent event) {
//
//        if (aliveSeats.containsKey(event.getClickedBlock().getLocation())) {
//            new Methods().sendConfigMessage(event.getPlayer(), "Messages.SeatOccupiedMessage");
//            return false;
//        }
//
//        return true;
//
//    }
//
//    Boolean blocksCheck(Player player, PlayerInteractEvent event) {
//
//        Material blockBelowMaterial = Bukkit.getWorld(player.getWorld().getName())
//                .getBlockAt(event.getClickedBlock().getLocation().clone().subtract(0, 1, 0)).getType();
//        Material blockAboveMaterial = Bukkit.getWorld(player.getWorld().getName())
//                .getBlockAt(event.getClickedBlock().getLocation().clone().add(0, 1, 0)).getType();
//
//        List<Material> bannedMaterialsBelow = new ArrayList<>();
//        bannedMaterialsBelow.add(Material.LAVA);
//        bannedMaterialsBelow.add(Material.FIRE);
//
//        List<Material> allowedMaterialsAbove = new ArrayList<>();
//        allowedMaterialsAbove.add(Material.AIR);
//        allowedMaterialsAbove.add(Material.ACACIA_WALL_SIGN);
//        allowedMaterialsAbove.add(Material.BIRCH_WALL_SIGN);
//        allowedMaterialsAbove.add(Material.DARK_OAK_WALL_SIGN);
//        allowedMaterialsAbove.add(Material.JUNGLE_WALL_SIGN);
//        allowedMaterialsAbove.add(Material.OAK_WALL_SIGN);
//        allowedMaterialsAbove.add(Material.SPRUCE_WALL_SIGN);
//        allowedMaterialsAbove.add(Material.WALL_TORCH);
//        allowedMaterialsAbove.add(Material.WATER);
//
//        for (Material material : bannedMaterialsBelow) {
//            if (material.equals(blockBelowMaterial)) {
//                new Methods().sendConfigMessage(player, "Messages.InvalidChairBlock");
//                return false;
//            }
//        }
//        for (Material material : allowedMaterialsAbove) {
//            if (material.equals(blockAboveMaterial)) {
//                return true;
//            }
//        }
//
//        new Methods().sendConfigMessage(player, "Messages.InvalidChairBlock");
//        return false;
//
//    }
//
//    Boolean claimCheck(Player player, PlayerInteractEvent event) {
//
//        if (!APIs.GriefPreventionFound) {
//            return true;
//        }
//
//        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(event.getClickedBlock().getLocation(), true, null);
//
//        if (claim == null) return true;
//
//        if (claim.allowAccess(player) == null) {
//            return true;
//        } else {
//            player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig()
//                    .getString("Messages.SeatNoClaimPerm").replace("%player%", claim.getOwnerName())));
//            return false;
//        }
//
//    }
//
//    Boolean regionCheck(Player player, PlayerInteractEvent event) {
//
//        if (!APIs.WorldGuardFound) {
//            return true;
//        }
//
//        com.sk89q.worldedit.util.Location blockLocation = BukkitAdapter.adapt(event.getClickedBlock().getLocation());
//        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
//        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
//        RegionQuery query = container.createQuery();
//        if (query.testState(blockLocation, localPlayer, com.darko.main.utilities.flags.Flags.SIT)) {
//            return true;
//        } else {
//            new Methods().sendConfigMessage(player, "Messages.SeatNoRegionPerm");
//            return false;
//        }
//
//    }
//
//}
