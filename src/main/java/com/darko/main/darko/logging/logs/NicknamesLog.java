package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class NicknamesLog extends Log {

    public NicknamesLog() {

        super(new LinkedHashMap<>() {
            {
                put("Time", "");
                put("Player", "");
                put("Nickname", "");
                put("WhoResponded", "");
                put("Action", "");
            }
        });

    }

}
