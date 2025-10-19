package com.brinvex.brokercon.core.api;

import com.brinvex.brokercon.core.api.provider.Provider;

import java.util.SequencedCollection;
import java.util.function.Function;

public interface Module {

    SequencedCollection<Provider<?, ?>> providers(Class<? extends Provider<?, ?>> providerType);

    static String compactName(Class<? extends Module> moduleType) {
        return moduleType.getSimpleName().replace("Module", "").toLowerCase();
    }

    interface PropKey {
        Function<Class<?>, String> customService = serviceType -> "customService.%s".formatted(serviceType.getSimpleName());
        String dmsWorkspace = "dmsWorkspace";
    }
}
