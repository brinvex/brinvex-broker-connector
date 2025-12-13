package com.brinvex.brokercon.connector.ibkr.internal.builder;

import com.brinvex.brokercon.connector.ibkr.api.model.statement.AssetCategory;
import com.brinvex.brokercon.connector.ibkr.api.model.statement.AssetSubCategory;
import com.brinvex.brokercon.connector.ibkr.api.model.statement.PriorPeriodPosition;
import com.brinvex.fintypes.enu.Currency;
import com.brinvex.java.validation.Assert;

import java.time.LocalDate;

@SuppressWarnings("DuplicatedCode")
public class PriorPeriodPositionBuilder {
    private Currency currency;
    private AssetCategory assetCategory;
    private AssetSubCategory assetSubCategory;
    private String symbol;
    private String description;
    private String isin;
    private String figi;
    private String underlyingSymbol;
    private LocalDate date;
    private String listingExchange;

    public PriorPeriodPosition build() {
        Assert.notNull(currency);
        Assert.notNull(assetCategory);
        Assert.notNull(assetSubCategory);
        Assert.notNullNotBlank(symbol);
        Assert.notNullNotBlank(description);
        Assert.notNullNotBlank(isin);
        Assert.notNull(underlyingSymbol);
        Assert.notNull(date);

        return new PriorPeriodPosition(
                currency,
                assetCategory,
                assetSubCategory,
                symbol,
                description,
                isin,
                figi,
                underlyingSymbol,
                date,
                listingExchange
        );
    }

    public PriorPeriodPositionBuilder currency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public PriorPeriodPositionBuilder assetCategory(AssetCategory assetCategory) {
        this.assetCategory = assetCategory;
        return this;
    }

    public PriorPeriodPositionBuilder assetSubCategory(AssetSubCategory assetSubCategory) {
        this.assetSubCategory = assetSubCategory;
        return this;
    }

    public PriorPeriodPositionBuilder symbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public PriorPeriodPositionBuilder description(String description) {
        this.description = description;
        return this;
    }

    public PriorPeriodPositionBuilder isin(String isin) {
        this.isin = isin;
        return this;
    }

    public PriorPeriodPositionBuilder figi(String figi) {
        this.figi = figi;
        return this;
    }

    public PriorPeriodPositionBuilder underlyingSymbol(String underlyingSymbol) {
        this.underlyingSymbol = underlyingSymbol;
        return this;
    }

    public PriorPeriodPositionBuilder date(LocalDate date) {
        this.date = date;
        return this;
    }

    public PriorPeriodPositionBuilder listingExchange(String listingExchange) {
        this.listingExchange = listingExchange;
        return this;
    }
}
