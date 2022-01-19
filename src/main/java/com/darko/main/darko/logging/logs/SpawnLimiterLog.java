package com.darko.main.darko.logging.logs;

import com.darko.main.darko.logging.Logging;

public class SpawnLimiterLog extends Log {

    public SpawnLimiterLog() {

        super();
        super.setName("SpawnLimiterLog");
        super.setEnabled(true);
        super.setDaysOfLogsToKeep(Logging.defaultDaysOfLogsToKeep);
        super.addArgument("Time", "");
        super.addArgument("EntityType", "");
        super.addArgument("Location", "");
        super.addArgument("ClaimOwner", "");

    }



}
