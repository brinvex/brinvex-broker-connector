package com.brinvex.brokercon.adapter.amnd.api.service;

import com.brinvex.brokercon.adapter.amnd.api.model.AmndTransStatementDocKey;

public interface AmndDms {

    AmndTransStatementDocKey getTradingAccountStatementDocKey(String accountNumber);

    byte[] getStatementContent(AmndTransStatementDocKey docKey);

}
