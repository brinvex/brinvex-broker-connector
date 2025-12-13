package com.brinvex.brokercon.connector.rvlt.api.service;

import com.brinvex.brokercon.connector.rvlt.api.model.statement.Transaction;
import com.brinvex.brokercon.core.api.domain.FinTransaction;

import java.util.List;

public interface RvltFinTransactionMapper {

    List<FinTransaction> mapTransactions(List<Transaction> rvltTransactions);

}
