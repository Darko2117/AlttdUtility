package com.darko.main.darko.logging.logs;

import com.darko.main.darko.logging.Logging;

public class PlayerLocationLog extends Log {

    public PlayerLocationLog() {

        super();
        super.setName("PlayerLocationLog");
        super.setEnabled(true);
        super.setDaysOfLogsToKeep(Logging.defaultDaysOfLogsToKeep);
        super.addArgument("Time", "");
        super.addArgument("Player", "");
        super.addArgument("Location", "");

    }

}
