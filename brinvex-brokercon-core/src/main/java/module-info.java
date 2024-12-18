module com.brinvex.brokercon.core {
    exports com.brinvex.brokercon.core.api;
    exports com.brinvex.brokercon.core.api.domain;
    exports com.brinvex.brokercon.core.api.domain.constraints.asset;
    exports com.brinvex.brokercon.core.api.domain.constraints.fintransaction;
    exports com.brinvex.brokercon.core.api.facade;
    exports com.brinvex.brokercon.core.api.provider;
    exports com.brinvex.brokercon.core.api.exception;
    exports com.brinvex.brokercon.core.api.general;
    requires com.brinvex.csv;
    requires com.brinvex.dms;
    requires com.brinvex.finance.types;
    requires com.brinvex.java;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.databind;
    requires jakarta.validation;
    requires org.apache.pdfbox;
    requires org.apache.pdfbox.io;
    requires org.slf4j;

    uses com.brinvex.brokercon.core.api.ModuleFactory;
}