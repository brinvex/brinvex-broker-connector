package com.brinvex.brokercon.adapter.fiob.api.service;

import com.brinvex.brokercon.core.api.domain.Account;

import java.time.LocalDate;

public interface FiobFetcher {

    String fetchTransStatement(
            Account account,
            LocalDate fromDateIncl,
            LocalDate toDateIncl
    );

    String fetchSnapshotStatement(
            Account account,
            LocalDate date
    );

    FiobFetcher newSessionReusingFetcher();
}
