package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class MyPetItemPickupLog extends Log {

    public MyPetItemPickupLog() {

        super(new LinkedHashMap<>() {
            {
                put("Time", "");
                put("Pet", "");
                put("Owner", "");
                put("Item", "");
                put("Location", "");
            }
        });

    }

}
