package com.brinvex.brokercon.connector.amnd.api.model;

import java.time.LocalDate;

public record AmndTransStatementDocKey(
        String accountId,
        LocalDate fromDateIncl,
        LocalDate toDateIncl
) {
}


