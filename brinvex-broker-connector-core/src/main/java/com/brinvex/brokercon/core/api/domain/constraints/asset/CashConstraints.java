package com.brinvex.brokercon.core.api.domain.constraints.asset;

import com.brinvex.fintypes.enu.Country;
import com.brinvex.brokercon.core.api.domain.Asset;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

public class CashConstraints extends AssetConstraints {

    CashConstraints(Asset asset) {
        super(asset);
    }

    @Null
    @Override
    public Country getCountry() {
        return super.getCountry();
    }

    @NotNull
    @Override
    public String getSymbol() {
        return super.getSymbol();
    }

    @Null
    @Override
    public String getName() {
        return super.getName();
    }

    @Null
    @Override
    public String getCountryFigi() {
        return super.getCountryFigi();
    }

    @Null
    @Override
    public String getIsin() {
        return super.getIsin();
    }

    @Null
    @Override
    public String getExternalType() {
        return super.getExternalType();
    }

    @Null
    @Override
    public String getExternalDetail() {
        return super.getExternalDetail();
    }
}
