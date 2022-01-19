package com.darko.main.darko.logging.logs;

import com.darko.main.darko.logging.Logging;

public class LightningStrikesLog extends Log {

    public LightningStrikesLog() {

        super();
        super.setName("LightningStrikesLog");
        super.setEnabled(true);
        super.setDaysOfLogsToKeep(Logging.defaultDaysOfLogsToKeep);
        super.addArgument("Time", "");
        super.addArgument("Cause", "");
        super.addArgument("Location", "");

    }

}
