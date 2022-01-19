package com.darko.main.darko.logging.logs;

import com.darko.main.darko.logging.Logging;

public class FarmLimiterLog extends Log {

    public FarmLimiterLog() {

        super();
        super.setName("FarmLimiterLog");
        super.setEnabled(true);
        super.setDaysOfLogsToKeep(Logging.defaultDaysOfLogsToKeep);
        super.addArgument("Time", "");
        super.addArgument("Entity", "");
        super.addArgument("Location", "");
        super.addArgument("ClaimOwner", "");

    }

}
