package com.brinvex.brokercon.core.api;

import com.brinvex.brokercon.core.api.provider.Provider;

import java.util.Set;

public interface ModuleFactory<CONNECTOR extends Module> {

    Class<CONNECTOR> connectorType();

    Set<Class<? extends Provider<?, ?>>> providerTypes();

    CONNECTOR createConnector(ModuleContext moduleCtx);

}
