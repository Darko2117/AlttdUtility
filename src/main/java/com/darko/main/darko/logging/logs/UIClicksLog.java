package com.darko.main.darko.logging.logs;

import com.darko.main.darko.logging.Logging;

public class UIClicksLog extends Log {

    public UIClicksLog() {

        super();
        super.setName("UIClicksLog");
        super.setEnabled(true);
        super.setDaysOfLogsToKeep(Logging.defaultDaysOfLogsToKeep);
        super.addArgument("Time", "");
        super.addArgument("User", "");
        super.addArgument("InventoryName", "");
        super.addArgument("ClickedItem", "");
        super.addArgument("Location", "");

    }

}
