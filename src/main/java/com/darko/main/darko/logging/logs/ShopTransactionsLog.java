package com.darko.main.darko.logging.logs;

import com.darko.main.darko.logging.Logging;

public class ShopTransactionsLog extends Log{

    public ShopTransactionsLog() {

        super();
        super.setName("ShopTransactionsLog");
        super.setEnabled(true);
        super.setDaysOfLogsToKeep(Logging.defaultDaysOfLogsToKeep);
        super.addArgument("Time", "");
        super.addArgument("OwnerName", "");
        super.addArgument("CustomerName", "");
        super.addArgument("ShopType", "");
        super.addArgument("Location", "");
        super.addArgument("Amount", "");
        super.addArgument("Price", "");
        super.addArgument("Item", "");
        super.addArgument("ShopBalance", "");

    }

}
