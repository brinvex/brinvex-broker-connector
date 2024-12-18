package com.brinvex.brokercon.adapter.ibkr.api.service;

import com.brinvex.brokercon.core.api.domain.FinTransaction;
import com.brinvex.brokercon.adapter.ibkr.api.model.statement.CashTransaction;
import com.brinvex.brokercon.adapter.ibkr.api.model.statement.CorporateAction;
import com.brinvex.brokercon.adapter.ibkr.api.model.statement.Trade;
import com.brinvex.brokercon.adapter.ibkr.api.model.statement.TradeConfirm;

import java.util.List;

public interface IbkrFinTransactionMapper {

    List<FinTransaction> mapCashTransactions(List<CashTransaction> cashTrans);

    List<FinTransaction> mapTrades(List<Trade> trades);

    List<FinTransaction> mapCorporateAction(List<CorporateAction> corpActions);

    List<FinTransaction> mapTradeConfirms(List<TradeConfirm> tradeConfirms);

}
