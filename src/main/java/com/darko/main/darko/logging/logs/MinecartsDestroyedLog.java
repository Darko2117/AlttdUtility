package com.darko.main.darko.logging.logs;

import com.darko.main.darko.logging.Logging;

public class MinecartsDestroyedLog extends Log {

    public MinecartsDestroyedLog() {

        super();
        super.setName("MinecartsDestroyedLog");
        super.setEnabled(true);
        super.setDaysOfLogsToKeep(Logging.defaultDaysOfLogsToKeep);
        super.addArgument("Time", "");
        super.addArgument("Attacker", "");
        super.addArgument("Location", "");
        super.addArgument("ClaimOwner", "");

    }

}
