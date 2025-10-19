package com.brinvex.brokercon.adapter.fiob.api.model.statement;


import com.brinvex.fintypes.vo.Money;

import java.time.LocalDate;
import java.util.List;

public sealed interface Statement {

    sealed interface TransStatement extends Statement {
        String accountId();

        LocalDate periodFrom();

        LocalDate periodTo();
    }

    record TradingTransStatement(
            @Override
            String accountId,
            @Override
            LocalDate periodFrom,
            @Override
            LocalDate periodTo,
            List<TradingTransaction> transactions,
            Lang lang
    ) implements TransStatement {
    }

    record SavingTransStatement(
            @Override
            String accountId,
            @Override
            LocalDate periodFrom,
            @Override
            LocalDate periodTo,
            List<SavingTransaction> transactions
    ) implements TransStatement {
    }

    record TradingSnapshotStatement(
            @Override
            LocalDate date,
            @Override
            Money nav
    ) implements Statement {
    }

}
