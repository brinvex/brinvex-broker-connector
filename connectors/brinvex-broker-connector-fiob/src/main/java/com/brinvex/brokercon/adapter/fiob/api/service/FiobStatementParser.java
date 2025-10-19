package com.brinvex.brokercon.adapter.fiob.api.service;

import com.brinvex.brokercon.adapter.fiob.api.model.statement.Statement.SavingTransStatement;
import com.brinvex.brokercon.adapter.fiob.api.model.statement.Statement.TradingSnapshotStatement;
import com.brinvex.brokercon.adapter.fiob.api.model.statement.Statement.TradingTransStatement;

import java.time.LocalDateTime;
import java.util.List;

public interface FiobStatementParser {

    LocalDateTime parseTradingStatementCreatedOn(List<String> statementHeaderLines);

    TradingTransStatement parseTradingTransStatement(String statementContent);

    SavingTransStatement parseSavingTransStatement(String statementContent);

    TradingSnapshotStatement parseSnapshotStatement(String statementContent);

}
