package com.brinvex.brokercon.adapter.ibkr.api.model.statement;

import com.brinvex.fintypes.enu.Currency;

import java.time.LocalDate;

public record PriorPeriodPosition(
        Currency currency,
        AssetCategory assetCategory,
        AssetSubCategory assetSubCategory,
        String symbol,
        String description,
        String isin,
        String figi,
        String underlyingSymbol,
        LocalDate date,
        String listingExchange
) {
}
