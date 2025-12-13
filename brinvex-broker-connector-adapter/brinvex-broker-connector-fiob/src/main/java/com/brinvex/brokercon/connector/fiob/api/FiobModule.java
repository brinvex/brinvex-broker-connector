package com.brinvex.brokercon.connector.fiob.api;

import com.brinvex.brokercon.connector.fiob.api.service.FiobDms;
import com.brinvex.brokercon.connector.fiob.api.service.FiobFetcher;
import com.brinvex.brokercon.connector.fiob.api.service.FiobFinTransactionMapper;
import com.brinvex.brokercon.connector.fiob.api.service.FiobPtfActivityProvider;
import com.brinvex.brokercon.connector.fiob.api.service.FiobStatementMerger;
import com.brinvex.brokercon.connector.fiob.api.service.FiobStatementParser;
import com.brinvex.brokercon.core.api.Module;

public interface FiobModule extends Module {

    FiobFetcher fetcher();

    FiobStatementParser statementParser();

    FiobStatementMerger statementMerger();

    FiobDms dms();

    FiobFinTransactionMapper finTransactionMapper();

    FiobPtfActivityProvider ptfProgressProvider();


}
