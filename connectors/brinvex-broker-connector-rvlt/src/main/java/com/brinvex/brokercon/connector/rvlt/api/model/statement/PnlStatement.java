package com.brinvex.brokercon.connector.rvlt.api.model.statement;

import com.brinvex.fintypes.enu.Currency;

import java.time.LocalDate;
import java.util.List;

public record PnlStatement(
        String accountName,
        String accountNumber,
        LocalDate periodStartIncl,
        LocalDate periodEndIncl,
        Currency ccy,
        List<Transaction> transactions
) {
}
