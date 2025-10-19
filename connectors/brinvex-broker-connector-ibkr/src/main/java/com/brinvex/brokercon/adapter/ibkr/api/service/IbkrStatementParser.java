package com.brinvex.brokercon.adapter.ibkr.api.service;

import com.brinvex.brokercon.adapter.ibkr.api.model.statement.FlexStatement.ActivityStatement;
import com.brinvex.brokercon.adapter.ibkr.api.model.statement.FlexStatement.TradeConfirmStatement;

import java.time.LocalDateTime;
import java.util.List;

public interface IbkrStatementParser {

    LocalDateTime parseStatementCreatedOn(List<String> statementLines);

    ActivityStatement parseActivityStatement(String statementXmlContent);

    TradeConfirmStatement parseTradeConfirmStatement(String statementXmlContent);

}
