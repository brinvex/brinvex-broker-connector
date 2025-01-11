package com.brinvex.brokercon.adapter.fiob.internal;

import com.brinvex.brokercon.adapter.fiob.api.FiobModule;
import com.brinvex.brokercon.adapter.fiob.api.service.FiobDms;
import com.brinvex.brokercon.adapter.fiob.api.service.FiobFetcher;
import com.brinvex.brokercon.adapter.fiob.api.service.FiobFinTransactionMapper;
import com.brinvex.brokercon.adapter.fiob.api.service.FiobPtfActivityProvider;
import com.brinvex.brokercon.adapter.fiob.api.service.FiobStatementMerger;
import com.brinvex.brokercon.adapter.fiob.api.service.FiobStatementParser;
import com.brinvex.brokercon.adapter.fiob.internal.service.FiobDmsImpl;
import com.brinvex.brokercon.adapter.fiob.internal.service.FiobFetcherImpl;
import com.brinvex.brokercon.adapter.fiob.internal.service.FiobPtfActivityProviderImpl;
import com.brinvex.brokercon.adapter.fiob.internal.service.FiobStatementMergerImpl;
import com.brinvex.brokercon.adapter.fiob.internal.service.mapper.FiobFinTransactionMapperImpl;
import com.brinvex.brokercon.adapter.fiob.internal.service.parser.FiobStatementParserImpl;
import com.brinvex.brokercon.core.api.Module;
import com.brinvex.brokercon.core.api.ModuleContext;
import com.brinvex.brokercon.core.api.ModuleFactory;
import com.brinvex.brokercon.core.api.provider.Provider;
import com.brinvex.brokercon.core.api.provider.PtfActivityProvider;

import java.util.List;
import java.util.SequencedCollection;
import java.util.Set;

import static java.util.Collections.emptyList;

public class FiobModuleImpl implements FiobModule, Module {

    public static class FiobModuleFactory implements ModuleFactory<FiobModule> {

        @Override
        public Class<FiobModule> connectorType() {
            return FiobModule.class;
        }

        @Override
        public Set<Class<? extends Provider<?, ?>>> providerTypes() {
            return Set.of(PtfActivityProvider.class);
        }

        @Override
        public FiobModule createConnector(ModuleContext moduleCtx) {
            return new FiobModuleImpl(moduleCtx);
        }
    }

    private final ModuleContext moduleCtx;

    public FiobModuleImpl(ModuleContext moduleCtx) {
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
    public FiobFetcher fetcher() {
        return moduleCtx.singletonService(FiobFetcher.class, FiobFetcherImpl::new);
    }

    @Override
    public FiobDms dms() {
        return moduleCtx.singletonService(FiobDms.class, () -> new FiobDmsImpl(moduleCtx.dms()));
    }

    @Override
    public FiobStatementParser statementParser() {
        return moduleCtx.singletonService(FiobStatementParser.class, FiobStatementParserImpl::new);
    }

    @Override
    public FiobStatementMerger statementMerger() {
        return moduleCtx.singletonService(FiobStatementMerger.class, FiobStatementMergerImpl::new);
    }

    @Override
    public FiobFinTransactionMapper finTransactionMapper() {
        return moduleCtx.singletonService(FiobFinTransactionMapper.class, FiobFinTransactionMapperImpl::new);
    }

    @Override
    public FiobPtfActivityProvider ptfProgressProvider() {
        return moduleCtx.singletonService(FiobPtfActivityProvider.class, () -> new FiobPtfActivityProviderImpl(
                dms(),
                statementParser(),
                fetcher(),
                statementMerger(),
                finTransactionMapper()
        ));
    }
}
