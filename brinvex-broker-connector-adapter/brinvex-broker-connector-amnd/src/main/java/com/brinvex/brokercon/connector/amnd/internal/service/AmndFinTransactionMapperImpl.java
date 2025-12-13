package com.brinvex.brokercon.connector.amnd.internal.service;

import com.brinvex.brokercon.connector.amnd.api.model.statement.Trade;
import com.brinvex.brokercon.connector.amnd.api.service.AmndFinTransactionMapper;
import com.brinvex.brokercon.core.api.ModuleContext;
import com.brinvex.brokercon.core.api.facade.JsonMapperFacade;
import com.brinvex.brokercon.core.api.domain.Asset;
import com.brinvex.fintypes.enu.Country;
import com.brinvex.fintypes.enu.InstrumentType;
import com.brinvex.brokercon.core.api.domain.FinTransaction;
import com.brinvex.fintypes.enu.FinTransactionType;
import com.brinvex.java.DateUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static com.brinvex.java.collection.Collectors.toLinkedMap;

public class AmndFinTransactionMapperImpl implements AmndFinTransactionMapper {

    private final Map<String, Asset> isinToAssetMapping;

    public AmndFinTransactionMapperImpl(ModuleContext moduleCtx) {
        String isinToSymbolMapProp = moduleCtx.getProperty("isinToSymbolMap");
        if (isinToSymbolMapProp == null || isinToSymbolMapProp.isEmpty()) {
            throw new IllegalArgumentException("isinToSymbolMap is required");
        }
        JsonMapperFacade jsonMapper = moduleCtx.toolbox().jsonMapper();
        @SuppressWarnings("unchecked")
        Map<String, String> rawMap = jsonMapper.readFromJson(isinToSymbolMapProp, Map.class);
        this.isinToAssetMapping = rawMap
                .entrySet()
                .stream()
                .collect(toLinkedMap(Map.Entry::getKey, e -> {
                    String[] symbolAndCountry = e.getValue().split("\\.");
                    return Asset.builder()
                            .isin(e.getKey())
                            .type(InstrumentType.FUND)
                            .symbol(symbolAndCountry[0])
                            .country(symbolAndCountry.length == 1 ? Country.US : Country.valueOf(symbolAndCountry[1]))
                            .build();
                }));
    }

    @Override
    public List<FinTransaction.FinTransactionBuilder> mapTradeToFinTransactionPair(Trade trade) {

        BigDecimal qty = trade.qty();
        BigDecimal fees = trade.fee();
        BigDecimal netValue = trade.netValue();
        BigDecimal grossValue = netValue.subtract(fees);

        String extraId = "%s/%s/%s/%s/%s".formatted(
                Lazy.ID_DF8.format(trade.orderDate()),
                Lazy.ID_DF6.format(trade.tradeDate()),
                Lazy.ID_DF6.format(trade.settleDate()),
                netValue.setScale(2, RoundingMode.HALF_UP),
                qty.setScale(2, RoundingMode.HALF_UP));

        return switch (trade.type()) {
            case BUY -> List.of(
                    FinTransaction.builder()
                            .type(FinTransactionType.DEPOSIT)
                            .externalId(extraId + "/1/DEPOSIT")
                            .date(trade.orderDate())
                            .qty(BigDecimal.ZERO)
                            .ccy(trade.ccy())
                            .grossValue(netValue.negate())
                            .netValue(netValue.negate())
                            .fee(BigDecimal.ZERO)
                            .tax(BigDecimal.ZERO)
                            .settleDate(trade.settleDate()),
                    FinTransaction.builder()
                            .type(FinTransactionType.BUY)
                            .externalId(extraId + "/2/BUY")
                            .date(trade.orderDate())
                            .asset(isinToAssetMapping.get(trade.isin()))
                            .qty(qty)
                            .price(trade.price())
                            .ccy(trade.ccy())
                            .grossValue(grossValue)
                            .netValue(netValue)
                            .fee(fees)
                            .tax(BigDecimal.ZERO)
                            .settleDate(trade.settleDate())
            );
            case SELL -> {
                LocalDate withdrawalDate = DateUtil.plusWorkDays(trade.orderDate(), 5);
                yield List.of(
                        FinTransaction.builder()
                                .type(FinTransactionType.SELL)
                                .externalId(extraId + "/3/SELL")
                                .date(trade.orderDate())
                                .asset(isinToAssetMapping.get(trade.isin()))
                                .qty(qty)
                                .price(trade.price())
                                .ccy(trade.ccy())
                                .grossValue(grossValue)
                                .netValue(netValue)
                                .fee(fees)
                                .tax(BigDecimal.ZERO)
                                .settleDate(trade.settleDate()),
                        FinTransaction.builder()
                                .type(FinTransactionType.WITHDRAWAL)
                                .externalId(extraId + "/4/WITHDRAWAL")
                                .date(withdrawalDate)
                                .qty(BigDecimal.ZERO)
                                .ccy(trade.ccy())
                                .grossValue(netValue.negate())
                                .netValue(netValue.negate())
                                .fee(BigDecimal.ZERO)
                                .tax(BigDecimal.ZERO)
                                .settleDate(withdrawalDate)
                );
            }
        };
    }

    private static class Lazy {
        private static final DateTimeFormatter ID_DF8 = DateTimeFormatter.ofPattern("yyyyMMdd");
        private static final DateTimeFormatter ID_DF6 = DateTimeFormatter.ofPattern("yyMMdd");
    }

}
