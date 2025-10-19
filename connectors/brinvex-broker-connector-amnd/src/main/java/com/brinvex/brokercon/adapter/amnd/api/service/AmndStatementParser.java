package com.brinvex.brokercon.adapter.amnd.api.service;

import com.brinvex.brokercon.adapter.amnd.api.model.statement.TransactionStatement;

public interface AmndStatementParser {

    TransactionStatement parseTrades(byte[] statementPdfContent);
}
