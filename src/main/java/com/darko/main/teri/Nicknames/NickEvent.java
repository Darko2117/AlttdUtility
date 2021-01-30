package com.darko.main.teri.Nicknames;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NickEvent extends Event{

    private String senderName;
    private String targetName;
    private String nickName;
    private NickEventType nickEventType;

    public NickEvent(String senderName, String targetName, String nickName, NickEventType nickEventType) {
        this.senderName = senderName;
        this.targetName = targetName;
        this.nickName = nickName;
        this.nickEventType = nickEventType;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getTargetName() {
        return targetName;
    }

    public String getNickName() {
        return nickName;
    }

    public NickEventType getNickEventType() {
        return nickEventType;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }

    public enum NickEventType {
        ACCEPTED,
        DENIED,
        SET,
        RESET
    }
}
