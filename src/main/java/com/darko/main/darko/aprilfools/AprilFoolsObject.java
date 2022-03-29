package com.darko.main.darko.aprilfools;

import org.bukkit.entity.Player;

public class AprilFoolsObject {

    private Player player;
    private long lastSoundTime;
    private long nextSoundDelay;

    public AprilFoolsObject(Player player, long lastSoundTime, long nextSoundDelay) {
        this.player = player;
        this.lastSoundTime = lastSoundTime;
        this.nextSoundDelay = nextSoundDelay;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public long getLastSoundTime() {
        return lastSoundTime;
    }

    public void setLastSoundTime(long lastSoundTime) {
        this.lastSoundTime = lastSoundTime;
    }

    public long getNextSoundDelay() {
        return nextSoundDelay;
    }

    public void setNextSoundDelay(long nextSoundDelay) {
        this.nextSoundDelay = nextSoundDelay;
    }

}
