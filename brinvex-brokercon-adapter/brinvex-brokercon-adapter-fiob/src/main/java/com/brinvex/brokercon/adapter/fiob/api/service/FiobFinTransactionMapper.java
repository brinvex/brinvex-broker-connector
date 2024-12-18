package com.brinvex.brokercon.adapter.fiob.api.service;

import com.brinvex.brokercon.adapter.fiob.api.model.statement.Lang;
import com.brinvex.brokercon.adapter.fiob.api.model.statement.SavingTransaction;
import com.brinvex.brokercon.adapter.fiob.api.model.statement.Statement.SavingTransStatement;
import com.brinvex.brokercon.adapter.fiob.api.model.statement.Statement.TradingTransStatement;
import com.brinvex.brokercon.adapter.fiob.api.model.statement.Statement.TransStatement;
import com.brinvex.brokercon.adapter.fiob.api.model.statement.TradingTransaction;
import com.brinvex.brokercon.core.api.domain.FinTransaction;

import java.util.List;

public interface FiobFinTransactionMapper {

    default List<FinTransaction> mapTransactions(TransStatement transStatement) {
        return switch (transStatement) {
            case SavingTransStatement savingTransStatement -> mapSavingTransactions(savingTransStatement.transactions());
            case TradingTransStatement tradingTransStatement -> mapTradingTransactions(tradingTransStatement.transactions(), tradingTransStatement.lang());
        };
    }

    List<FinTransaction> mapTradingTransactions(List<TradingTransaction> tradingTrans, Lang lang);

    List<FinTransaction> mapSavingTransactions(List<SavingTransaction> tradingTrans);
}
