package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class ClaimsResizedLog extends Log {

    public ClaimsResizedLog() {

        super(new LinkedHashMap<>() {
            {
                put("Time", "");
                put("Player", "");
                put("LowestY", "");
                put("Area", "");
            }
        });

    }

}
