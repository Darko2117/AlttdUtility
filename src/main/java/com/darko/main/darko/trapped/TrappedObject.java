package com.darko.main.darko.trapped;

import com.darko.main.AlttdUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

enum Status {
    AVAILABLE, AWAITING_TELEPORT, ON_COOLDOWN
}


public class TrappedObject {

    Player player;
    String commandToExecute;
    Integer secondsToTeleport;
    Integer secondsCooldown;
    Long teleportTimestamp;
    Long cooldownTimestamp;
    BossBar bossBar;
    Status status;


    TrappedObject(Player player) {

        this.player = player;
        commandToExecute = AlttdUtility.getInstance().getConfig().getString("Trapped.CommandToExecute").replace("%player%", player.getName());
        secondsToTeleport = AlttdUtility.getInstance().getConfig().getInt("Trapped.SecondsToTeleport");
        secondsCooldown = AlttdUtility.getInstance().getConfig().getInt("Trapped.Cooldown");
        teleportTimestamp = System.currentTimeMillis() + secondsToTeleport * 1000;
        cooldownTimestamp = System.currentTimeMillis() + secondsCooldown * 1000;
        bossBar = Bukkit.createBossBar(ChatColor.DARK_RED + "" + ChatColor.BOLD + "TELEPORTING IN " + getSecondsUntilTeleportString() + "!", BarColor.RED, BarStyle.SOLID);
        bossBar.setProgress(1);
        bossBar.addPlayer(player);
        status = Status.AWAITING_TELEPORT;

    }

    void update() {

        if (status.equals(Status.AWAITING_TELEPORT)) {
            if (teleportTimestamp <= System.currentTimeMillis()) {
                Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), commandToExecute);
                status = Status.ON_COOLDOWN;
            }
        }

        if (status.equals(Status.AWAITING_TELEPORT)) {
            Double totalTime = secondsToTeleport.doubleValue();
            Double passedTime = Double.parseDouble((String.valueOf((teleportTimestamp - System.currentTimeMillis()) / 1000d)));
            Double progress = passedTime / totalTime;
            if (progress < 0d)
                progress = 0d;
            if (progress > 1d)
                progress = 1d;
            bossBar.setProgress(progress);
            bossBar.setTitle(ChatColor.DARK_RED + "" + ChatColor.BOLD + "TELEPORTING IN " + getSecondsUntilTeleportString() + "!");
        }

        if (!status.equals(Status.AWAITING_TELEPORT)) {
            bossBar.removeAll();
        }

        if (status.equals(Status.ON_COOLDOWN)) {
            if (cooldownTimestamp <= System.currentTimeMillis()) {
                status = Status.AVAILABLE;
            }
        }

    }

    String getSecondsUntilTeleportString() {

        Double seconds = Double.parseDouble(String.valueOf((teleportTimestamp - System.currentTimeMillis()) / 1000d));

        if (seconds < 0d)
            seconds = 0d;

        return new DecimalFormat("0.0").format(seconds);

    }

    Integer getRemainingCooldownSeconds() {

        Integer cooldown = Integer.parseInt(String.valueOf(cooldownTimestamp - System.currentTimeMillis())) / 1000;

        if (cooldown < 0)
            cooldown = 0;

        return cooldown;

    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getCommandToExecute() {
        return commandToExecute;
    }

    public void setCommandToExecute(String commandToExecute) {
        this.commandToExecute = commandToExecute;
    }

    public Integer getSecondsToTeleport() {
        return secondsToTeleport;
    }

    public void setSecondsToTeleport(Integer secondsToTeleport) {
        this.secondsToTeleport = secondsToTeleport;
    }

    public Integer getSecondsCooldown() {
        return secondsCooldown;
    }

    public void setSecondsCooldown(Integer secondsCooldown) {
        this.secondsCooldown = secondsCooldown;
    }

    public Long getTeleportTimestamp() {
        return teleportTimestamp;
    }

    public void setTeleportTimestamp(Long teleportTimestamp) {
        this.teleportTimestamp = teleportTimestamp;
    }

    public Long getCooldownTimestamp() {
        return cooldownTimestamp;
    }

    public void setCooldownTimestamp(Long cooldownTimestamp) {
        this.cooldownTimestamp = cooldownTimestamp;
    }

    public BossBar getBossBar() {
        return bossBar;
    }

    public void setBossBar(BossBar bossBar) {
        this.bossBar = bossBar;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
