package com.brinvex.brokercon.connector.amnd.api.service;

import com.brinvex.brokercon.connector.amnd.api.model.statement.TransactionStatement;

public interface AmndStatementParser {

    TransactionStatement parseTrades(byte[] statementPdfContent);
}
