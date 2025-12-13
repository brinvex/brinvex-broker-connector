package com.brinvex.brokercon.connector.amnd.api.service;

import com.brinvex.brokercon.connector.amnd.api.model.AmndTransStatementDocKey;

public interface AmndDms {

    AmndTransStatementDocKey getTradingAccountStatementDocKey(String accountNumber);

    byte[] getStatementContent(AmndTransStatementDocKey docKey);

}
