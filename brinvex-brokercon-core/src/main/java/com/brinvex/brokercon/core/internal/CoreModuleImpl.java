package com.brinvex.brokercon.core.internal;

import com.brinvex.brokercon.core.api.CoreModule;
import com.brinvex.brokercon.core.api.ModuleContext;
import com.brinvex.brokercon.core.api.Toolbox;
import com.brinvex.brokercon.core.api.provider.Provider;
import com.brinvex.brokercon.core.api.provider.PtfActivityProvider;
import com.brinvex.brokercon.core.internal.ptfprogress.PtfActivityProviderImpl;

import java.util.List;
import java.util.SequencedCollection;

import static java.util.Collections.emptyList;

public class CoreModuleImpl implements CoreModule {

    private final ModuleContext moduleCtx;

    public CoreModuleImpl(ModuleContext moduleCtx) {
        this.moduleCtx = moduleCtx;
    }

    @Override
    public SequencedCollection<Provider<?, ?>> providers(Class<? extends Provider<?, ?>> providerType) {
        if (PtfActivityProvider.class == providerType) {
            return List.of(ptfProgressProvider());
        }
        return emptyList();
    }

    private PtfActivityProvider ptfProgressProvider() {
        return moduleCtx.singletonService(PtfActivityProvider.class, () -> new PtfActivityProviderImpl(moduleCtx.dms()));
    }

    @Override
    public Toolbox toolbox() {
        return moduleCtx.toolbox();
    }
}
