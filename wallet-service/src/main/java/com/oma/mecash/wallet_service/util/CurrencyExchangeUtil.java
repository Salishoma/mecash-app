package com.oma.mecash.wallet_service.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class CurrencyExchangeUtil {

    public static BigDecimal getDefaultRate(String from, String to) {
        Map<String, BigDecimal> defaultRates = Map.of(
                "USD_NGN", new BigDecimal("1515.00"),
                "EUR_NGN", new BigDecimal("1585.90"),
                "GBP_NGN", new BigDecimal("1908.45"),
                "NGN_USD", new BigDecimal("1.00").divide(new BigDecimal("1515.00"), 10, RoundingMode.HALF_UP),
                "NGN_EURO", new BigDecimal("1.00").divide(new BigDecimal("1585.90"), 10, RoundingMode.HALF_UP),
                "NGN_GBP", new BigDecimal("1.00").divide(new BigDecimal("1908.45"), 10, RoundingMode.HALF_UP)
        );

        return defaultRates.getOrDefault(from + "_" + to, new BigDecimal("1.00"));
    }

}

