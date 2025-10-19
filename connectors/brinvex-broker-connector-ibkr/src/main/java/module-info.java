import com.brinvex.brokercon.adapter.ibkr.internal.IbkrModuleImpl;

module com.brinvex.brokercon.adapter.ibkr {
    exports com.brinvex.brokercon.adapter.ibkr.api;
    exports com.brinvex.brokercon.adapter.ibkr.api.model;
    exports com.brinvex.brokercon.adapter.ibkr.api.model.statement;
    exports com.brinvex.brokercon.adapter.ibkr.api.service;
    requires transitive com.brinvex.brokercon.core;
    requires transitive com.brinvex.fintypes;
    requires transitive com.brinvex.java;
    requires transitive com.brinvex.dms;
    requires java.net.http;
    requires java.xml;
    requires transitive org.slf4j;
    provides com.brinvex.brokercon.core.api.ModuleFactory with IbkrModuleImpl.IbkrModuleFactory;
}