package com.brinvex.brokercon.core.internal;

import com.brinvex.brokercon.core.api.BrokerConnector;
import com.brinvex.brokercon.core.api.BrokerConnectorConfig;
import com.brinvex.brokercon.core.api.Module;
import com.brinvex.brokercon.core.api.ModuleContext;
import com.brinvex.brokercon.core.api.Toolbox;
import com.brinvex.dms.api.Dms;
import com.brinvex.java.validation.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ModuleContextImpl implements ModuleContext {

    private static final Logger LOG = LoggerFactory.getLogger(ModuleContextImpl.class);

    private final String moduleCompactName;

    private final BrokerConnector brokerConnector;

    private final BrokerConnectorConfig config;

    private final ToolboxImpl toolbox;

    private volatile Dms dms;

    private final Map<Class<?>, Object> singletons = new LinkedHashMap<>();

    public ModuleContextImpl(
            BrokerConnector brokerConnector,
            BrokerConnectorConfig config,
            Toolbox toolbox,
            Class<? extends Module> moduleType
    ) {
        this.moduleCompactName = Module.compactName(moduleType);
        this.brokerConnector = brokerConnector;
        this.config = config;
        this.toolbox = (ToolboxImpl) toolbox;
    }

    @Override
    public <SERVICE> SERVICE singletonService(Class<SERVICE> svcType, Supplier<SERVICE> serviceSupplier) {
        Object uncheckedSvcInstance = singletons.get(svcType);
        if (uncheckedSvcInstance == null) {
            synchronized (singletons) {
                uncheckedSvcInstance = singletons.get(svcType);
                if (uncheckedSvcInstance == null) {
                    String customSvcPropKey = Module.PropKey.customService.apply(svcType);
                    String customSvcFqn = getProperty(customSvcPropKey);
                    if (customSvcFqn != null) {
                        try {
                            uncheckedSvcInstance = newCustomService(customSvcFqn, serviceSupplier);
                        } catch (Exception e) {
                            String errMsg = "CustomService instantiation failed - %s, svcType=%s, %s"
                                    .formatted(customSvcFqn, svcType, e.getMessage());
                            throw new IllegalStateException(errMsg, e);
                        }
                        LOG.info("Instantiated customService={}", uncheckedSvcInstance);
                    } else {
                        uncheckedSvcInstance = serviceSupplier.get();
                    }
                    Assert.notNull(uncheckedSvcInstance);
                    singletons.put(svcType, uncheckedSvcInstance);
                }
            }
        }
        @SuppressWarnings("unchecked")
        SERVICE typedSvcInstance = (SERVICE) uncheckedSvcInstance;
        return typedSvcInstance;
    }

    @Override
    public String getProperty(String propKey, String defaultValue) {
        return config.getProperty(moduleCompactName, propKey, defaultValue);
    }

    @Override
    public Dms dms() {
        if (dms == null) {
            synchronized (this) {
                if (dms == null) {
                    String dmsWorkspace = getProperty(Module.PropKey.dmsWorkspace, moduleCompactName + "-dms");
                    dms = toolbox.dmsFactory().getDms(dmsWorkspace);
                }
            }
        }
        return dms;
    }

    @Override
    public Toolbox toolbox() {
        return toolbox;
    }

    private <SERVICE> SERVICE newCustomService(String customSvcFqn, Supplier<SERVICE> defaultService) {
        try {
            @SuppressWarnings("unchecked")
            Constructor<SERVICE> constructor = (Constructor<SERVICE>) Class.forName(customSvcFqn)
                    .getConstructor(ModuleContext.class, Supplier.class);
            return constructor.newInstance(this, defaultService);
        } catch (ClassNotFoundException
                 | IllegalAccessException
                 | InstantiationException
                 | NoSuchMethodException
                 | InvocationTargetException e
        ) {
            throw new IllegalStateException("Failed to instantiate %s, %s".formatted(customSvcFqn, e), e);
        }
    }
}
