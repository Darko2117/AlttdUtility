package com.darko.main.darko.logging.logs;

import com.darko.main.darko.logging.Logging;

public class CratePrizesLog extends Log {

    public CratePrizesLog() {

        super();
        super.setName("CratePrizesLog");
        super.setEnabled(true);
        super.setDaysOfLogsToKeep(Logging.defaultDaysOfLogsToKeep);
        super.addArgument("Time", "");
        super.addArgument("User", "");
        super.addArgument("Items", "");
        super.addArgument("Commands", "");
        super.addArgument("Crate", "");

    }

}
