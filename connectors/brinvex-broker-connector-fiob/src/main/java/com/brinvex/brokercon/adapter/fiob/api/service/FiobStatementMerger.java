package com.brinvex.brokercon.adapter.fiob.api.service;


import com.brinvex.brokercon.adapter.fiob.api.model.statement.Statement.SavingTransStatement;
import com.brinvex.brokercon.adapter.fiob.api.model.statement.Statement.TradingTransStatement;

import java.util.Collection;
import java.util.Optional;

public interface FiobStatementMerger {

    Optional<TradingTransStatement> mergeTradingTransStatements(Collection<TradingTransStatement> tradingTransStatements);

    Optional<SavingTransStatement> mergeSavingTransStatements(Collection<SavingTransStatement> savingTransStatements);
}
