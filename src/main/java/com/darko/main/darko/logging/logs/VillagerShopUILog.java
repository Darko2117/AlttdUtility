package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class VillagerShopUILog extends Log {

    public VillagerShopUILog() {

        super(new LinkedHashMap<>() {
            {
                put("Time", "");
                put("User", "");
                put("Amount", "");
                put("Price", "");
                put("Item", "");
                put("PointsBefore", "");
                put("PointsAfter", "");
                put("Type", "");
            }
        });

    }

}
