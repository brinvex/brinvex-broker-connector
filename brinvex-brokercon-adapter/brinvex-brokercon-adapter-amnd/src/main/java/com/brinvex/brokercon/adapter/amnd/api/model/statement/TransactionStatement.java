package com.brinvex.brokercon.adapter.amnd.api.model.statement;

import java.util.List;

public record TransactionStatement(
        String accountId,
        List<Trade> trades
) {
}
