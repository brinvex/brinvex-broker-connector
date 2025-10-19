package com.brinvex.brokercon.core.api;

import com.brinvex.brokercon.core.api.domain.PtfActivity;
import com.brinvex.brokercon.core.api.domain.PtfActivityReq;
import com.brinvex.brokercon.core.internal.BrokerConnectorImpl;

public interface BrokerConnector {

    PtfActivity process(PtfActivityReq request);

    <MODULE extends Module> MODULE getModule(Class<MODULE> moduleType);

    static BrokerConnector newBrokerConnector(BrokerConnectorConfig config) {
        return new BrokerConnectorImpl(config);
    }


}
