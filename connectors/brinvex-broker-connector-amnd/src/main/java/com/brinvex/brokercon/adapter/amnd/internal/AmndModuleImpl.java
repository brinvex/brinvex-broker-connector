package com.brinvex.brokercon.adapter.amnd.internal;

import com.brinvex.brokercon.adapter.amnd.api.AmndModule;
import com.brinvex.brokercon.adapter.amnd.api.service.AmndDms;
import com.brinvex.brokercon.adapter.amnd.api.service.AmndFinTransactionMapper;
import com.brinvex.brokercon.adapter.amnd.api.service.AmndPtfActivityProvider;
import com.brinvex.brokercon.adapter.amnd.api.service.AmndStatementParser;
import com.brinvex.brokercon.adapter.amnd.internal.service.AmndDmsImpl;
import com.brinvex.brokercon.adapter.amnd.internal.service.AmndFinTransactionMapperImpl;
import com.brinvex.brokercon.adapter.amnd.internal.service.AmndPtfActivityProviderImpl;
import com.brinvex.brokercon.adapter.amnd.internal.service.AmndStatementParserImpl;
import com.brinvex.brokercon.core.api.Module;
import com.brinvex.brokercon.core.api.ModuleContext;
import com.brinvex.brokercon.core.api.ModuleFactory;
import com.brinvex.brokercon.core.api.provider.Provider;
import com.brinvex.brokercon.core.api.provider.PtfActivityProvider;

import java.util.List;
import java.util.SequencedCollection;
import java.util.Set;

import static java.util.Collections.emptyList;

public class AmndModuleImpl implements AmndModule, Module {

    public static class AmndModuleFactory implements ModuleFactory<AmndModule> {

        @Override
        public Class<AmndModule> connectorType() {
            return AmndModule.class;
        }

        @Override
        public Set<Class<? extends Provider<?, ?>>> providerTypes() {
            return Set.of(PtfActivityProvider.class);
        }

        @Override
        public AmndModule createConnector(ModuleContext moduleCtx) {
            return new AmndModuleImpl(moduleCtx);
        }
    }

    private final ModuleContext moduleCtx;

    public AmndModuleImpl(ModuleContext moduleCtx) {
        this.moduleCtx = moduleCtx;
    }

    @Override
    public SequencedCollection<Provider<?, ?>> providers(Class<? extends Provider<?, ?>> providerType) {
        if (providerType == PtfActivityProvider.class) {
            return List.of(ptfProgressProvider());
        }
        return emptyList();
    }

    @Override
    public AmndStatementParser statementParser() {
        return moduleCtx.singletonService(AmndStatementParser.class, () -> new AmndStatementParserImpl(moduleCtx.toolbox().pdfReader()));
    }

    @Override
    public AmndDms dms() {
        return moduleCtx.singletonService(AmndDms.class, () -> new AmndDmsImpl(moduleCtx.dms()));
    }

    @Override
    public AmndFinTransactionMapper finTransactionMapper() {
        return moduleCtx.singletonService(AmndFinTransactionMapper.class, () -> new AmndFinTransactionMapperImpl(moduleCtx));
    }

    @Override
    public AmndPtfActivityProvider ptfProgressProvider() {
        return moduleCtx.singletonService(AmndPtfActivityProvider.class, () -> new AmndPtfActivityProviderImpl(
                dms(),
                statementParser(),
                finTransactionMapper()
        ));
    }
}
