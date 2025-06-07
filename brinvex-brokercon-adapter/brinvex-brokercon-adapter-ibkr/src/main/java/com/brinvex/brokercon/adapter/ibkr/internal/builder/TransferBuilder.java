package com.brinvex.brokercon.adapter.ibkr.internal.builder;

import com.brinvex.brokercon.adapter.ibkr.api.model.statement.AssetCategory;
import com.brinvex.brokercon.adapter.ibkr.api.model.statement.Transfer;
import com.brinvex.brokercon.adapter.ibkr.api.model.statement.TransferType;
import com.brinvex.fintypes.enu.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransferBuilder {
    private TransferType type;
    private LocalDate reportDate;
    private Currency currency;
    private AssetCategory assetCategory;
    private String symbol;
    private String description;
    private String isin;
    private String figi;
    private BigDecimal quantity;
    private LocalDate date;
    private LocalDate settleDate;
    private BigDecimal cashTransfer;
    private String transactionID;

    public Transfer build() {
        return new Transfer(
                type,
                reportDate,
                currency,
                assetCategory,
                symbol,
                description,
                isin,
                figi,
                quantity,
                date,
                settleDate,
                cashTransfer,
                transactionID
        );
    }

    public TransferBuilder type(TransferType type) {
        this.type = type;
        return this;
    }

    public TransferBuilder reportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
        return this;
    }

    public TransferBuilder currency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public TransferBuilder assetCategory(AssetCategory assetCategory) {
        this.assetCategory = assetCategory;
        return this;
    }

    public TransferBuilder symbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public TransferBuilder description(String description) {
        this.description = description;
        return this;
    }

    public TransferBuilder isin(String isin) {
        this.isin = isin;
        return this;
    }

    public TransferBuilder figi(String figi) {
        this.figi = figi;
        return this;
    }

    public TransferBuilder quantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public TransferBuilder date(LocalDate date) {
        this.date = date;
        return this;
    }

    public TransferBuilder settleDate(LocalDate settleDate) {
        this.settleDate = settleDate;
        return this;
    }

    public TransferBuilder cashTransfer(BigDecimal cashTransfer) {
        this.cashTransfer = cashTransfer;
        return this;
    }

    public TransferBuilder transactionID(String transactionID) {
        this.transactionID = transactionID;
        return this;
    }
}
