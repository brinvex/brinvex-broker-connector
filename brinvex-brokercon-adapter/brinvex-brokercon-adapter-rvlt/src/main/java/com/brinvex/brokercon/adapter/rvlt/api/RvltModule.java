package com.brinvex.brokercon.adapter.rvlt.api;

import com.brinvex.brokercon.adapter.rvlt.api.service.RvltFinTransactionMapper;
import com.brinvex.brokercon.adapter.rvlt.api.service.RvltDms;
import com.brinvex.brokercon.adapter.rvlt.api.service.RvltFinTransactionMerger;
import com.brinvex.brokercon.adapter.rvlt.api.service.RvltPtfActivityProvider;
import com.brinvex.brokercon.adapter.rvlt.api.service.RvltStatementParser;
import com.brinvex.brokercon.core.api.Module;

public interface RvltModule extends Module {

    RvltStatementParser statementParser();

    RvltDms dms();

    RvltFinTransactionMerger finTransactionMerger();

    RvltFinTransactionMapper finTransactionMapper();

    RvltPtfActivityProvider ptfProgressProvider();

}
