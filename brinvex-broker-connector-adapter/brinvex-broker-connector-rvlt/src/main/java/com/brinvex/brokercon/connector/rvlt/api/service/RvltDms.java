package com.brinvex.brokercon.connector.rvlt.api.service;


import com.brinvex.brokercon.connector.rvlt.api.model.RvltDocKey.TradingAccountStatementDocKey;
import com.brinvex.brokercon.connector.rvlt.api.model.RvltDocKey.PnlStatementDocKey;

import java.time.LocalDate;
import java.util.List;

public interface RvltDms {

    List<TradingAccountStatementDocKey> getTradingAccountStatementDocKeys(String accountNumber, LocalDate fromDateIncl, LocalDate toDateIncl);

    byte[] getStatementContent(TradingAccountStatementDocKey docKey);

    List<PnlStatementDocKey> getPnlStatementDocKeys(String accountNumber, LocalDate fromDateIncl, LocalDate toDateIncl);

    byte[] getStatementContent(PnlStatementDocKey docKey);
}
