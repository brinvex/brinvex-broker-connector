package com.brinvex.brokercon.adapter.rvlt.api.service;

import com.brinvex.brokercon.adapter.rvlt.api.model.statement.Transaction;

import java.util.List;

public interface RvltFinTransactionMerger {

    List<Transaction> mergeTransactions(List<Transaction> taTrans, List<Transaction> pnlTransactions);

}
