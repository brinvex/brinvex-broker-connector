package com.brinvex.brokercon.core.api.domain.constraints.fintransaction;

import com.brinvex.brokercon.core.api.domain.Asset;
import com.brinvex.brokercon.core.api.domain.FinTransaction;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class SymbolChangeConstraints extends FinTransactionConstraints {

    SymbolChangeConstraints(FinTransaction finTransaction) {
        super(finTransaction);
    }

    @NotNull
    @Override
    public Asset getAsset() {
        return super.getAsset();
    }

    @Min(0)
    @Max(0)
    @Override
    public BigDecimal getGrossValueCalcDeviation() {
        return super.getGrossValueCalcDeviation();
    }

    @Min(0)
    @Max(0)
    @Override
    public BigDecimal getNetValue() {
        return super.getNetValue();
    }

    @Min(0)
    @Max(0)
    @NotNull
    @Override
    public BigDecimal getTax() {
        return super.getTax();
    }

    @Min(0)
    @Max(0)
    @Override
    public BigDecimal getFee() {
        return super.getFee();
    }

}
