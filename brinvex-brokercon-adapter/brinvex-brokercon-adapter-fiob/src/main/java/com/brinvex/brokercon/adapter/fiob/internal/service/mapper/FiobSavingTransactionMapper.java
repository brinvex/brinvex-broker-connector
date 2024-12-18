package com.brinvex.brokercon.adapter.fiob.internal.service.mapper;

import com.brinvex.brokercon.adapter.fiob.api.model.statement.SavingTransaction;
import com.brinvex.brokercon.core.api.domain.FinTransaction;
import com.brinvex.finance.types.enu.PtfTransactionType;
import com.brinvex.java.validation.Assert;

import java.util.ArrayList;
import java.util.List;

import static java.math.BigDecimal.ZERO;

public class FiobSavingTransactionMapper {

    @SuppressWarnings("SpellCheckingInspection")
    public List<FinTransaction> mapTransactions(List<SavingTransaction> rawTrans) {
        List<FinTransaction> trans = new ArrayList<>();

        for (int i = 0, rawTransSize = rawTrans.size(); i < rawTransSize; i++) {
            SavingTransaction rawTran = rawTrans.get(i);
            try {
                FinTransaction.FinTransactionBuilder newTran = FinTransaction.builder();
                newTran.type(detectTranType(rawTran));
                newTran.externalId(rawTran.id());
                newTran.date(rawTran.date());
                newTran.ccy(rawTran.ccy());
                newTran.grossValue(rawTran.volume());
                newTran.netValue(newTran.grossValue());
                newTran.qty(ZERO);
                newTran.fee(ZERO);
                newTran.tax(ZERO);
                newTran.settleDate(rawTran.date());

                if (PtfTransactionType.INTEREST.equals(newTran.type()) && i < rawTransSize - 1) {
                    SavingTransaction nextRawTran = rawTrans.get(i + 1);
                    if (rawTran.date().isEqual(nextRawTran.date())) {
                        String nextRawType = nextRawTran.type();
                        PtfTransactionType nextTranType = detectTranType(nextRawTran);
                        if (PtfTransactionType.TAX.equals(nextTranType) && "Odvod daně z úroků".equals(nextRawType)) {
                            Assert.equal(newTran.ccy(), nextRawTran.ccy());
                            newTran.tax(nextRawTran.volume());
                            newTran.reconcileNetValue();
                            i++;
                        }
                    }
                }
                trans.add(newTran.build());
            } catch (Exception e) {
                throw new IllegalStateException("%s - rawTran=%s".formatted(i + 1, rawTran), e);
            }
        }

        return trans;
    }

    @SuppressWarnings("SpellCheckingInspection")
    private PtfTransactionType detectTranType(SavingTransaction tran) {
        String rawType = tran.type();
        if ("Bezhotovostní příjem".equals(rawType)) {
            return PtfTransactionType.DEPOSIT;
        }
        if ("Příjem převodem uvnitř banky".equals(rawType)) {
            return PtfTransactionType.DEPOSIT;
        }
        if ("Platba kartou".equals(rawType)) {
            return PtfTransactionType.WITHDRAWAL;
        }
        if ("Bezhotovostní platba".equals(rawType)) {
            return PtfTransactionType.WITHDRAWAL;
        }
        if ("Platba převodem uvnitř banky".equals(rawType)) {
            return PtfTransactionType.WITHDRAWAL;
        }
        if ("Připsaný úrok".equals(rawType)) {
            return PtfTransactionType.INTEREST;
        }
        if ("Odvod daně z úroků".equals(rawType)) {
            return PtfTransactionType.TAX;
        }
        throw new IllegalArgumentException(String.valueOf(tran));
    }

}