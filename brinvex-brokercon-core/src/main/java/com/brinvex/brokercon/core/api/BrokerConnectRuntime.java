package com.brinvex.brokercon.core.api;

import com.brinvex.brokercon.core.api.domain.PtfActivity;
import com.brinvex.brokercon.core.api.domain.PtfActivityReq;
import com.brinvex.brokercon.core.internal.BrokerConnectRuntimeImpl;

public interface BrokerConnectRuntime {

    PtfActivity process(PtfActivityReq request);

    <MODULE extends Module> MODULE getModule(Class<MODULE> moduleType);

    static BrokerConnectRuntime newPtfActivityRuntime(BrokerConnectRuntimeConfig config) {
        return new BrokerConnectRuntimeImpl(config);
    }


}
