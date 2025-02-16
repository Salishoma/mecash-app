package com.oma.mecash.wallet_service.util;

import java.util.Map;

public class CurrencyExchangeUtil {

    public static double getDefaultRate(String from, String to) {
        Map<String, String> defaultRates = Map.of(
                "USD_NGN", String.format("%.2f", 1515.00),
                "EUR_NGN", String.format("%.2f", 1585.90),
                "GBP_NGN", String.format("%.2f", 1908.45),
                "NGN_USD", String.format("%.2f", 1 / 1515.00),
                "NGN_EURO", String.format("%.2f", 1 / 1585.90),
                "NGN_GBP", String.format("%.2f", 1 / 1908.45)
        );

        return Double.parseDouble(defaultRates.getOrDefault(from + "_" + to, String.format("%.2f", 1.00)));
    }
}

