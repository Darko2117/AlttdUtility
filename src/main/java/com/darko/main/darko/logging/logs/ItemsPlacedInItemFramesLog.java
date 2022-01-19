package com.darko.main.darko.logging.logs;

import com.darko.main.darko.logging.Logging;

public class ItemsPlacedInItemFramesLog extends Log {

    public ItemsPlacedInItemFramesLog() {

        super();
        super.setName("ItemsPlacedInItemFramesLog");
        super.setEnabled(true);
        super.setDaysOfLogsToKeep(Logging.defaultDaysOfLogsToKeep);
        super.addArgument("Time", "");
        super.addArgument("User", "");
        super.addArgument("Item", "");
        super.addArgument("Location", "");

    }

}
