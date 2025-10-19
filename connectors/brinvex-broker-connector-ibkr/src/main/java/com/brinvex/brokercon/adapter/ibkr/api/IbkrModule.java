package com.brinvex.brokercon.adapter.ibkr.api;

import com.brinvex.brokercon.adapter.ibkr.api.service.IbkrDms;
import com.brinvex.brokercon.adapter.ibkr.api.service.IbkrFetcher;
import com.brinvex.brokercon.adapter.ibkr.api.service.IbkrFinTransactionMapper;
import com.brinvex.brokercon.adapter.ibkr.api.service.IbkrPtfActivityProvider;
import com.brinvex.brokercon.adapter.ibkr.api.service.IbkrStatementMerger;
import com.brinvex.brokercon.adapter.ibkr.api.service.IbkrStatementParser;
import com.brinvex.brokercon.core.api.Module;

public interface IbkrModule extends Module {

    IbkrFetcher fetcher();

    IbkrStatementParser statementParser();

    IbkrStatementMerger statementMerger();

    IbkrDms dms();

    IbkrFinTransactionMapper finTransactionMapper();

    IbkrPtfActivityProvider ptfProgressProvider();
}
