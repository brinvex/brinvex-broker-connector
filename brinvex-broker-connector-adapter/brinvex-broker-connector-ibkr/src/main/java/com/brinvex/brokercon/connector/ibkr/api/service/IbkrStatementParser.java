package com.brinvex.brokercon.connector.ibkr.api.service;

import com.brinvex.brokercon.connector.ibkr.api.model.statement.FlexStatement.ActivityStatement;
import com.brinvex.brokercon.connector.ibkr.api.model.statement.FlexStatement.TradeConfirmStatement;

import java.time.LocalDateTime;
import java.util.List;

public interface IbkrStatementParser {

    LocalDateTime parseStatementCreatedOn(List<String> statementLines);

    ActivityStatement parseActivityStatement(String statementXmlContent);

    TradeConfirmStatement parseTradeConfirmStatement(String statementXmlContent);

}
