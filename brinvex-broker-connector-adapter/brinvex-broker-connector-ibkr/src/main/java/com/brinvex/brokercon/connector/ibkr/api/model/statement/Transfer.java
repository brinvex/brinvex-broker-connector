package com.brinvex.brokercon.connector.ibkr.api.model.statement;

import com.brinvex.fintypes.enu.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Transfer(
        TransferType type,
        LocalDate reportDate,
        Currency currency,
        AssetCategory assetCategory,
        String symbol,
        String description,
        String isin,
        String figi,
        BigDecimal quantity,
        LocalDate date,
        LocalDate settleDate,
        BigDecimal cashTransfer,
        String transactionID
) {
}
