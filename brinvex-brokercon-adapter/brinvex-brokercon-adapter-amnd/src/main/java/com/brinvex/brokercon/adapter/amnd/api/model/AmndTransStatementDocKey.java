package com.brinvex.brokercon.adapter.amnd.api.model;

import java.time.LocalDate;

public record AmndTransStatementDocKey(
        String accountId,
        LocalDate fromDateIncl,
        LocalDate toDateIncl
) {
}


