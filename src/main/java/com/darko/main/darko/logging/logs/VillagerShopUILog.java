package com.darko.main.darko.logging.logs;

import com.darko.main.darko.logging.Logging;

public class VillagerShopUILog extends Log {

    public VillagerShopUILog() {

        super();
        super.setName("VillagerShopUILog");
        super.setEnabled(true);
        super.setDaysOfLogsToKeep(Logging.defaultDaysOfLogsToKeep);
        super.addArgument("Time", "");
        super.addArgument("User", "");
        super.addArgument("Amount", "");
        super.addArgument("Price", "");
        super.addArgument("Item", "");
        super.addArgument("PointsBefore", "");
        super.addArgument("PointsAfter", "");
        super.addArgument("Type", "");

    }

}
