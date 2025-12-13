package com.brinvex.brokercon.connector.amnd.api.model.statement;

import java.util.List;

public record TransactionStatement(
        String accountId,
        List<Trade> trades
) {
}
