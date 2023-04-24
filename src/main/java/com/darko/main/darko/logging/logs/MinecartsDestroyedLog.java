package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class MinecartsDestroyedLog extends Log {

    public MinecartsDestroyedLog() {

        super(new LinkedHashMap<>() {
            {
                put("Time", "");
                put("Attacker", "");
                put("Location", "");
                put("ClaimOwner", "");
            }
        });

    }

}
