package com.darko.main.darko.logging.logs;

import com.darko.main.darko.logging.Logging;

public class PickedUpItemsLog extends Log {

    public PickedUpItemsLog() {

        super();
        super.setName("PickedUpItemsLog");
        super.setEnabled(true);
        super.setDaysOfLogsToKeep(Logging.defaultDaysOfLogsToKeep);
        super.addArgument("Time", "");
        super.addArgument("User", "");
        super.addArgument("Item", "");
        super.addArgument("Location", "");

    }

}
