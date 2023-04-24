package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class EggsThrownLog extends Log {

    public EggsThrownLog() {

        super(new LinkedHashMap<>() {
            {
                put("Time", "");
                put("User", "");
                put("Location", "");
                put("ClaimOwner", "");
            }
        });

    }

}
