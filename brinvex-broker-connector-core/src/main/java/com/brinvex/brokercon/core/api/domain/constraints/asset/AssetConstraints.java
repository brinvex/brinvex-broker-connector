package com.brinvex.brokercon.core.api.domain.constraints.asset;

import com.brinvex.fintypes.enu.Country;
import com.brinvex.fintypes.enu.InstrumentType;
import com.brinvex.brokercon.core.api.domain.Asset;
import com.brinvex.brokercon.core.api.general.Validatable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public abstract class AssetConstraints implements Validatable {

    public static AssetConstraints of(Asset asset) {
        return switch (asset.type()) {
            case CASH -> new CashConstraints(asset);
            case STK, ETF, FUND, BOND, DERIV -> new InstrumentConstraints(asset);
            case IND -> throw new IllegalStateException("Unexpected value: " + asset.type());
        };
    }

    private final Asset asset;

    AssetConstraints(Asset asset) {
        this.asset = asset;
    }

    @NotNull
    public InstrumentType getType() {
        return asset.type();
    }

    public Country getCountry() {
        return asset.country();
    }

    @NotBlank
    public String getSymbol() {
        return asset.symbol();
    }

    public String getName() {
        return asset.name();
    }

    public String getCountryFigi() {
        return asset.countryFigi();
    }

    public String getIsin() {
        return asset.isin();
    }

    public String getExternalType() {
        return asset.externalType();
    }

    public String getExternalDetail() {
        return asset.externalDetail();
    }
}
