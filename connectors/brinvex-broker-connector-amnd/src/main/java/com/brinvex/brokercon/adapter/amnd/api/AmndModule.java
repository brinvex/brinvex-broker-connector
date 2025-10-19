package com.brinvex.brokercon.adapter.amnd.api;

import com.brinvex.brokercon.adapter.amnd.api.service.AmndDms;
import com.brinvex.brokercon.adapter.amnd.api.service.AmndFinTransactionMapper;
import com.brinvex.brokercon.adapter.amnd.api.service.AmndPtfActivityProvider;
import com.brinvex.brokercon.adapter.amnd.api.service.AmndStatementParser;
import com.brinvex.brokercon.core.api.Module;

public interface AmndModule extends Module {

    AmndStatementParser statementParser();

    AmndDms dms();

    AmndFinTransactionMapper finTransactionMapper();

    AmndPtfActivityProvider ptfProgressProvider();

}
