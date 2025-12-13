package com.brinvex.brokercon.connector.amnd.api.service;

import com.brinvex.brokercon.connector.amnd.api.model.statement.Trade;
import com.brinvex.brokercon.core.api.domain.FinTransaction;

import java.util.List;

public interface AmndFinTransactionMapper {

    List<FinTransaction.FinTransactionBuilder> mapTradeToFinTransactionPair(Trade trade);

}
