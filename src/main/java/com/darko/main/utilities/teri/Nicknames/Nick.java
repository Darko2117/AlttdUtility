package com.darko.main.utilities.teri.Nicknames;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Nick {
    private String oldNick;
    private String newNick;
    private UUID uuid;
    private String lastChanged;

    public Nick(String oldNick, String newNick, UUID uuid, long lastChanged) {
        this.oldNick = oldNick;
        this.newNick = newNick;
        this.uuid = uuid;
        this.lastChanged = new SimpleDateFormat("yyyy-MM-dd").format(new Date(lastChanged));
    }

    public String getOldNick() {
        return oldNick;
    }

    public String getNewNick() {
        return newNick;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getLastChanged() {
        return lastChanged;
    }
}
