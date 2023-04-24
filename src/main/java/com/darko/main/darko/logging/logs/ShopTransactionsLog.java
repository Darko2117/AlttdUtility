package com.darko.main.darko.logging.logs;

import java.util.LinkedHashMap;

public class ShopTransactionsLog extends Log {

    public ShopTransactionsLog() {

        super(new LinkedHashMap<>() {
            {
                put("Time", "");
                put("OwnerName", "");
                put("CustomerName", "");
                put("ShopType", "");
                put("Location", "");
                put("Amount", "");
                put("Price", "");
                put("Item", "");
                put("ShopBalance", "");
            }
        });

    }

}
