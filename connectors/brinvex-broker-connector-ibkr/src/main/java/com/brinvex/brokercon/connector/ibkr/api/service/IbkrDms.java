package com.brinvex.brokercon.connector.ibkr.api.service;


import com.brinvex.brokercon.connector.ibkr.api.model.IbkrDocKey;
import com.brinvex.brokercon.connector.ibkr.api.model.IbkrDocKey.ActivityDocKey;
import com.brinvex.brokercon.connector.ibkr.api.model.IbkrDocKey.TradeConfirmDocKey;

import java.time.LocalDate;
import java.util.List;

public interface IbkrDms {

    List<ActivityDocKey> getActivityDocKeys(String accountId, LocalDate fromDateIncl, LocalDate toDateIncl);

    List<TradeConfirmDocKey> getTradeConfirmDocKeys(String accountId, LocalDate fromDateIncl, LocalDate toDateIncl);

    String getStatementContent(IbkrDocKey docKey);

    List<String> getStatementContentLines(IbkrDocKey docKey, int limit);

    boolean putActivityStatement(ActivityDocKey docKey, String content);

    boolean putTradeConfirmStatement(TradeConfirmDocKey docKey, String content);

    void delete(IbkrDocKey docKey);

}
