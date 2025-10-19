package com.brinvex.brokercon.core.api.domain.constraints.asset;

import com.brinvex.fintypes.enu.Country;
import com.brinvex.brokercon.core.api.domain.Asset;
import jakarta.validation.constraints.NotNull;

public class InstrumentConstraints extends AssetConstraints {

    InstrumentConstraints(Asset asset) {
        super(asset);
    }

    @NotNull
    @Override
    public Country getCountry() {
        return super.getCountry();
    }

    @NotNull
    @Override
    public String getSymbol() {
        return super.getSymbol();
    }
}
