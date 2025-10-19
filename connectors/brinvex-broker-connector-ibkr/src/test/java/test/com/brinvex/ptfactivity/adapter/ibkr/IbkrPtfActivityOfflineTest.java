package test.com.brinvex.ptfactivity.adapter.ibkr;


import com.brinvex.brokercon.adapter.ibkr.api.IbkrModule;
import com.brinvex.brokercon.adapter.ibkr.api.model.IbkrDocKey.ActivityDocKey;
import com.brinvex.brokercon.adapter.ibkr.api.service.IbkrDms;
import com.brinvex.brokercon.adapter.ibkr.api.service.IbkrPtfActivityProvider;
import com.brinvex.brokercon.core.api.domain.FinTransaction;
import com.brinvex.brokercon.core.api.domain.PtfActivity;
import com.brinvex.brokercon.core.api.domain.constraints.fintransaction.FinTransactionConstraints;
import com.brinvex.brokercon.core.api.facade.ValidatorFacade;
import com.brinvex.brokercon.testsupport.SimplePtf;
import com.brinvex.brokercon.testsupport.TestContext;
import com.brinvex.fintypes.enu.Country;
import com.brinvex.fintypes.enu.FinTransactionType;
import com.brinvex.fintypes.vo.DateAmount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static com.brinvex.fintypes.enu.Country.DE;
import static com.brinvex.fintypes.enu.Country.US;
import static com.brinvex.fintypes.enu.Currency.EUR;
import static com.brinvex.fintypes.enu.Currency.USD;
import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;
import static java.time.LocalDate.now;
import static java.time.LocalDate.parse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class IbkrPtfActivityOfflineTest extends IbkrBaseTest {

    @EnabledIf("account1IsNotNull")
    @Test
    void portfolioProgress_iterative() {
        String workspace = "ibkr-dms-stable";
        TestContext testCtx = this.testCtx.withDmsWorkspace(workspace);
        IbkrModule ibkrModule = testCtx.get(IbkrModule.class);
        IbkrDms ibkrDms = ibkrModule.dms();
        IbkrPtfActivityProvider ptfProgressProvider = ibkrModule.ptfProgressProvider();
        ValidatorFacade validator = testCtx.validator();
        LocalDate today = now();

        assert account1 != null;
        List<ActivityDocKey> docKeys = ibkrDms.getActivityDocKeys(account1.externalId(), LocalDate.MIN, today);
        assertFalse(docKeys.isEmpty());
        for (LocalDate d = docKeys.getFirst().fromDateIncl(); d.isBefore(docKeys.getLast().toDateIncl()); d = d.plusMonths(1)) {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgressOffline(account2, d, today);
            assertNotNull(ptfActivity, "d=%s".formatted(d));
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
        }
    }

    @EnabledIf("account2IsNotNull")
    @Test
    void portfolioProgress_spinOff() {
        String workspace = "ibkr-dms-stable";
        TestContext testCtx = this.testCtx.withDmsWorkspace(workspace);
        IbkrModule ibkrModule = testCtx.get(IbkrModule.class);
        IbkrPtfActivityProvider ptfProgressProvider = ibkrModule.ptfProgressProvider();
        ValidatorFacade validator = testCtx.validator();

        PtfActivity ptfActivity = ptfProgressProvider.getPtfProgressOffline(
                account2, parse("2023-01-23"), parse("2024-04-02"));
        validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);

        SimplePtf ptf = new SimplePtf(ptfActivity.transactions());

        assertEquals(0, ptf.getHoldingQty(US, "GE").compareTo(new BigDecimal(6)));
        assertEquals(0, ptf.getHoldingQty(US, "GEV").compareTo(new BigDecimal(1)));
        assertEquals(0, ptf.getCash(EUR).compareTo(new BigDecimal("234.561374405")));
        assertEquals(0, ptf.getCash(USD).compareTo(new BigDecimal("153.48807417")));

        FinTransaction transformationTran = ptf.getTransactions().get(225);
        assertEquals(FinTransactionType.TRANSFORMATION, transformationTran.type());
        assertEquals("GEV", transformationTran.asset().symbol());
        FinTransaction sellTran = ptf.getTransactions().get(226);
        assertEquals(FinTransactionType.SELL, sellTran.type());
        assertEquals("GEV", sellTran.asset().symbol());
    }

    @EnabledIf("account2IsNotNull")
    @Test
    void portfolioProgress_paymentOfLieuOfDividends() {
        String workspace = "ibkr-dms-stable";
        TestContext testCtx = this.testCtx.withDmsWorkspace(workspace);
        IbkrModule ibkrModule = testCtx.get(IbkrModule.class);
        IbkrPtfActivityProvider ptfProgressProvider = ibkrModule.ptfProgressProvider();
        ValidatorFacade validator = testCtx.validator();

        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgressOffline(
                    account2, parse("2023-01-23"), parse("2024-06-28"));
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            SimplePtf ptf = new SimplePtf(ptfActivity.transactions());

            assertEquals(314, ptf.getTransactions().size());
            FinTransaction tran = ptf.getTransactions().get(310);
            assertEquals(FinTransactionType.DIVIDEND, tran.type());
            assertEquals("PAYMENT_IN_LIEU_OF_DIVIDENDS", tran.externalType());
            assertEquals("ARCC", tran.asset().symbol());
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgressOffline(
                    account2, parse("2023-01-23"), parse("2024-07-15"));
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            SimplePtf ptf = new SimplePtf(ptfActivity.transactions());

            assertEquals(326, ptf.getTransactions().size());
            FinTransaction tran = ptf.getTransactions().get(310);
            assertEquals(FinTransactionType.DIVIDEND, tran.type());
            assertEquals("PAYMENT_IN_LIEU_OF_DIVIDENDS", tran.externalType());
            assertEquals("ARCC", tran.asset().symbol());
        }
    }

    @EnabledIf("account2IsNotNull")
    @Test
    void portfolioProgress_symbolChange() {
        String workspace = "ibkr-dms-stable-20250917";
        TestContext testCtx = this.testCtx.withDmsWorkspace(workspace);
        IbkrModule ibkrModule = testCtx.get(IbkrModule.class);
        IbkrPtfActivityProvider ptfProgressProvider = ibkrModule.ptfProgressProvider();
        ValidatorFacade validator = testCtx.validator();

        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgressOffline(
                    account2, parse("2023-01-23"), parse("2025-09-17"));
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            SimplePtf ptf = new SimplePtf(ptfActivity.transactions());

            assertEquals(new BigDecimal("0"), ptf.getHoldingQty(US, "SQ"));
            assertEquals(new BigDecimal("6"), ptf.getHoldingQty(US, "XYZ"));

            assertEquals(new BigDecimal("0"), ptf.getHoldingQty(US, "IGT"));
            assertEquals(new BigDecimal("40"), ptf.getHoldingQty(US, "BRSL"));
        }
    }

    @EnabledIf("account2IsNotNull")
    @Test
    void portfolioProgress_tradeConfirm() {

        List<FinTransaction> actAndTcTrans;

        {
            TestContext testCtx = this.testCtx.withDmsWorkspace("ibkr-dms-stable-20250918");
            IbkrModule ibkrModule = testCtx.get(IbkrModule.class);
            IbkrPtfActivityProvider ptfProgressProvider = ibkrModule.ptfProgressProvider();
            ValidatorFacade validator = testCtx.validator();

            List<FinTransaction> actTrans1;
            {
                PtfActivity ptfActivity = ptfProgressProvider.getPtfProgressOffline(
                        account2, parse("2023-01-23"), parse("2025-09-16"));
                validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
                SimplePtf ptf = new SimplePtf(ptfActivity.transactions());
                actTrans1 = ptf.getTransactions();
                assertEquals(0, ptf.getHoldingQty(DE, "SXR8").compareTo(new BigDecimal("81")));
            }
            {
                PtfActivity ptfActivity = ptfProgressProvider.getPtfProgressOffline(
                        account2, parse("2023-01-23"), parse("2025-09-17"));
                validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
                SimplePtf ptf = new SimplePtf(ptfActivity.transactions());
                assertEquals(actTrans1, ptf.getTransactions());
            }
            {
                PtfActivity ptfActivity = ptfProgressProvider.getPtfProgressOffline(
                        account2, parse("2023-01-23"), parse("2025-09-18"));
                validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
                SimplePtf ptf = new SimplePtf(ptfActivity.transactions());

                actAndTcTrans = ptf.getTransactions();
                assertEquals(actTrans1.size() + 1, actAndTcTrans.size());

                FinTransaction newestTran = actAndTcTrans.getLast();
                assertEquals(FinTransactionType.BUY, newestTran.type());
                assertEquals(parse("2025-09-18"), newestTran.date());
                assertEquals("SXR8", newestTran.asset().symbol());
                assertEquals(0, ptf.getHoldingQty(DE, "SXR8").compareTo(new BigDecimal("83")));
            }
        }
    }

    @EnabledIf("account2IsMigrated")
    @Test
    void ptfProgress_accountMigration_newNavSameAsOld() {
        TestContext testCtx = this.testCtx.withDmsWorkspace("ibkr-dms-stable");
        IbkrModule ibkrModule = testCtx.get(IbkrModule.class);
        IbkrPtfActivityProvider ptfProgressProvider = ibkrModule.ptfProgressProvider();
        ValidatorFacade validator = testCtx.validator();

        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgressOffline(
                    account2, parse("2023-01-23"), parse("2024-09-02"));
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            for (DateAmount nav : ptfActivity.netAssetValues()) {
                assertTrue(nav.amount().compareTo(ZERO) > 0, nav::toString);
            }

            SimplePtf ptf = new SimplePtf(ptfActivity.transactions());

            assertEquals(2, ptf.getCurrencies().size());
            assertEquals("49.10", ptf.getCash(EUR).setScale(2, HALF_UP).toString());
            assertEquals("105.82", ptf.getCash(USD).setScale(2, HALF_UP).toString());

            assertEquals(39, ptf.getHoldingsCount());
        }
    }

    @EnabledIf("account2IsMigrated")
    @Test
    void ptfProgress_accountMigration_navDate() {
        TestContext testCtx = this.testCtx.withDmsWorkspace("ibkr-dms-stable");
        IbkrModule ibkrModule = testCtx.get(IbkrModule.class);
        IbkrPtfActivityProvider ptfProgressProvider = ibkrModule.ptfProgressProvider();
        ValidatorFacade validator = testCtx.validator();
        PtfActivity ptfActivity = ptfProgressProvider.getPtfProgressOffline(
                account2, parse("2023-01-23"), parse("2024-09-11"));
        validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
        assertEquals(parse("2024-09-11"), ptfActivity.netAssetValues().getLast().date());
    }

    @EnabledIf("account2IsNotNull")
    @Test
    void ptfProgress_corpActions() {
        TestContext testCtx = this.testCtx.withDmsWorkspace("ibkr-dms-stable");
        IbkrModule ibkrModule = testCtx.get(IbkrModule.class);
        IbkrPtfActivityProvider ptfProgressProvider = ibkrModule.ptfProgressProvider();
        ValidatorFacade validator = testCtx.validator();
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgressOffline(
                    account2, parse("2023-01-23"), parse("2023-08-02"));
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            SimplePtf ptf = new SimplePtf(ptfActivity.transactions());

            assertEquals(2, ptf.getCurrencies().size());
            assertEquals(0, ptf.getCash(EUR).compareTo(new BigDecimal("43.659223735")));
            assertEquals(0, ptf.getCash(USD).compareTo(new BigDecimal("0.402378700")));

            assertEquals(15, ptf.getHoldingsCount());
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgressOffline(
                    account2, parse("2023-01-23"), parse("2023-11-17"));
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            SimplePtf ptf = new SimplePtf(ptfActivity.transactions());

            assertEquals(2, ptf.getCurrencies().size());
            assertEquals(0, ptf.getCash(EUR).compareTo(new BigDecimal("722.811854405")));
            assertEquals(0, ptf.getCash(USD).compareTo(new BigDecimal("183.601774170")));

            assertEquals(23, ptf.getHoldingsCount());
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgressOffline(
                    account2, parse("2023-01-23"), parse("2023-11-29"));
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            SimplePtf ptf = new SimplePtf(ptfActivity.transactions());

            assertEquals(2, ptf.getCurrencies().size());
            assertEquals(0, ptf.getCash(EUR).compareTo(new BigDecimal("722.811854405")));
            assertEquals(0, ptf.getCash(USD).compareTo(new BigDecimal("1071.101774170")));

            assertEquals(23, ptf.getHoldingsCount());
            assertEquals(0, ptf.getHoldingQty(US, "VMW").compareTo(ZERO));
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgressOffline(
                    account2, parse("2023-01-23"), parse("2024-04-30"));
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            SimplePtf ptf = new SimplePtf(ptfActivity.transactions());

            assertEquals(2, ptf.getCurrencies().size());
            assertEquals(0, ptf.getCash(EUR).setScale(2, HALF_UP).compareTo(new BigDecimal("284.92")));
            assertEquals(0, ptf.getCash(USD).setScale(2, HALF_UP).compareTo(new BigDecimal("164.64")));
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgressOffline(
                    account2, parse("2023-01-23"), parse("2024-05-31"));
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            SimplePtf ptf = new SimplePtf(ptfActivity.transactions());

            assertEquals(2, ptf.getCurrencies().size());
            assertEquals(0, ptf.getCash(EUR).setScale(2, HALF_UP).compareTo(new BigDecimal("130.13")));
            assertEquals(0, ptf.getCash(USD).setScale(2, HALF_UP).compareTo(new BigDecimal("54.07")));
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgressOffline(
                    account2, parse("2023-01-23"), parse("2024-06-05"));
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            SimplePtf ptf = new SimplePtf(ptfActivity.transactions());

            assertEquals(2, ptf.getCurrencies().size());
            assertEquals(0, ptf.getCash(EUR).setScale(2, HALF_UP).compareTo(new BigDecimal("1090.13")));
            assertEquals(0, ptf.getCash(USD).setScale(2, HALF_UP).compareTo(new BigDecimal("64.89")));
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgressOffline(
                    account2, parse("2023-01-23"), parse("2024-06-10"));
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            SimplePtf ptf = new SimplePtf(ptfActivity.transactions());

            assertEquals(2, ptf.getCurrencies().size());
            assertEquals(0, ptf.getHoldingQty(US, "NVDA").compareTo(new BigDecimal("60")));
        }
    }

    @EnabledIf("account2IsNotNull")
    @Test
    void ptfProgress_balanceCorrectness() {
        TestContext brokercon = this.testCtx.withDmsWorkspace("ibkr-dms-stable-20250918");
        IbkrPtfActivityProvider ptfProgressProvider = brokercon.get(IbkrModule.class).ptfProgressProvider();
        ValidatorFacade validator = brokercon.validator();

        SimplePtf ptf;
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgressOffline(
                    account2, parse("2023-01-23"), parse("2024-06-10")
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            ptf = new SimplePtf(ptfActivity.transactions());

            assertEquals(2, ptf.getCurrencies().size());
            assertEquals(0, ptf.getHoldingQty(US, "NVDA").compareTo(new BigDecimal("60")));
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgressOffline(
                    account2, parse("2023-01-23"), now()
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            assertTrue(ptf.getTransactions().size() <= ptfActivity.transactions().size());

            ptf = new SimplePtf(ptfActivity.transactions());
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgressOffline(
                    account2, parse("2023-01-23"), now()
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            assertTrue(ptf.getTransactions().size() <= ptfActivity.transactions().size());

            ptf = new SimplePtf(ptfActivity.transactions());
            assertTrue(ptf.getCurrencies().size() >= 2);
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgressOffline(
                    account2, parse("2023-01-23"), parse("2024-09-05")
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            ptf = new SimplePtf(ptfActivity.transactions());
            assertEquals(2, ptf.getCurrencies().size());
            assertEquals("6", ptf.getHoldingQty(Country.US, "ILMN").toString());
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgressOffline(
                    account2, parse("2023-01-23"), parse("2024-09-11")
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            ptf = new SimplePtf(ptfActivity.transactions());
            assertEquals(2, ptf.getCurrencies().size());
            assertEquals("35", ptf.getHoldingQty(DE, "SXR8").toString());
            assertEquals("59.64", ptf.getCash(EUR).setScale(2, HALF_UP).toPlainString());
            assertEquals("517.29", ptf.getCash(USD).setScale(2, HALF_UP).toPlainString());
            assertEquals("5108.86", ptfActivity.netAssetValues().getLast().amount().remainder(new BigDecimal("10000")).setScale(2, HALF_UP).toPlainString());
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgressOffline(
                    account2, parse("2023-01-23"), parse("2024-10-10")
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            ptf = new SimplePtf(ptfActivity.transactions());
            assertEquals(2, ptf.getCurrencies().size());
            assertEquals("0.02", ptf.getCash(EUR).setScale(2, HALF_UP).toPlainString());
            assertEquals("811.39", ptf.getCash(USD).setScale(2, HALF_UP).toPlainString());
            assertEquals("35", ptf.getHoldingQty(DE, "SXR8").toString());
            assertEquals("1653.08", ptfActivity.netAssetValues().getLast().amount().remainder(new BigDecimal("10000")).setScale(2, HALF_UP).toPlainString());
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgressOffline(
                    account2, parse("2023-01-23"), parse("2024-10-21")
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            ptf = new SimplePtf(ptfActivity.transactions());
            assertEquals(2, ptf.getCurrencies().size());
            assertEquals("0.02", ptf.getCash(EUR).setScale(2, HALF_UP).toPlainString());
            assertEquals("595.01", ptf.getCash(USD).setScale(2, HALF_UP).toPlainString());
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgressOffline(
                    account2, parse("2023-01-23"), parse("2024-10-31")
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            ptf = new SimplePtf(ptfActivity.transactions());
            assertEquals(2, ptf.getCurrencies().size());
            assertEquals("206.48", ptf.getCash(EUR).setScale(2, HALF_UP).toPlainString());
            assertEquals("693.38", ptf.getCash(USD).setScale(2, HALF_UP).toPlainString());
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgressOffline(
                    account2, parse("2023-01-23"), parse("2024-11-22")
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            ptf = new SimplePtf(ptfActivity.transactions());
            assertEquals(2, ptf.getCurrencies().size());
            assertEquals("1206.48", ptf.getCash(EUR).setScale(2, HALF_UP).toPlainString());
            assertEquals("564.24", ptf.getCash(USD).setScale(2, HALF_UP).toPlainString());
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgressOffline(
                    account2, parse("2023-01-23"), parse("2024-11-29")
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            ptf = new SimplePtf(ptfActivity.transactions());
            assertEquals(2, ptf.getCurrencies().size());
            assertEquals("477.44", ptf.getCash(EUR).setScale(2, HALF_UP).toPlainString());
            assertEquals("587.03", ptf.getCash(USD).setScale(2, HALF_UP).toPlainString());
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgressOffline(
                    account2, parse("2023-01-23"), parse("2024-12-31")
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            ptf = new SimplePtf(ptfActivity.transactions());
            assertEquals(2, ptf.getCurrencies().size());
            assertEquals("65.66", ptf.getCash(EUR).setScale(2, HALF_UP).toPlainString());
            assertEquals("426.40", ptf.getCash(USD).setScale(2, HALF_UP).toPlainString());
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgressOffline(
                    account2, parse("2023-01-23"), parse("2025-01-15")
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            ptf = new SimplePtf(ptfActivity.transactions());
            assertEquals(2, ptf.getCurrencies().size());
            assertEquals("43.96", ptf.getCash(EUR).setScale(2, HALF_UP).toPlainString());
            assertEquals("8.52", ptf.getCash(USD).setScale(2, HALF_UP).toPlainString());
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgressOffline(
                    account2, parse("2023-01-23"), parse("2025-01-22")
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            ptf = new SimplePtf(ptfActivity.transactions());
            assertEquals(2, ptf.getCurrencies().size());
            assertEquals("24.40", ptf.getCash(EUR).setScale(2, HALF_UP).toPlainString());
            assertEquals("18.34", ptf.getCash(USD).setScale(2, HALF_UP).toPlainString());
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgressOffline(
                    account2, parse("2023-01-23"), parse("2025-01-27")
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            ptf = new SimplePtf(ptfActivity.transactions());
            assertEquals(2, ptf.getCurrencies().size());
            assertEquals("114.42", ptf.getCash(EUR).setScale(2, HALF_UP).toPlainString());
            assertEquals("19.78", ptf.getCash(USD).setScale(2, HALF_UP).toPlainString());
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgressOffline(
                    account2, parse("2023-01-23"), parse("2025-01-28")
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            ptf = new SimplePtf(ptfActivity.transactions());
            assertEquals(2, ptf.getCurrencies().size());
            assertEquals("114.42", ptf.getCash(EUR).setScale(2, HALF_UP).toPlainString());
            assertEquals("20.01", ptf.getCash(USD).setScale(2, HALF_UP).toPlainString());
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgressOffline(
                    account2, parse("2023-01-23"), parse("2025-01-29")
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            ptf = new SimplePtf(ptfActivity.transactions());
            assertEquals(2, ptf.getCurrencies().size());
            assertEquals("114.42", ptf.getCash(EUR).setScale(2, HALF_UP).toPlainString());
            assertEquals("20.01", ptf.getCash(USD).setScale(2, HALF_UP).toPlainString());
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgressOffline(
                    account2, parse("2023-01-23"), parse("2025-01-31")
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            ptf = new SimplePtf(ptfActivity.transactions());
            assertEquals(2, ptf.getCurrencies().size());
            assertEquals("114.42", ptf.getCash(EUR).setScale(2, HALF_UP).toPlainString());
            assertEquals("31.89", ptf.getCash(USD).setScale(2, HALF_UP).toPlainString());
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgressOffline(
                    account2, parse("2023-01-23"), parse("2025-06-04")
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            ptf = new SimplePtf(ptfActivity.transactions());
            assertEquals(2, ptf.getCurrencies().size());
            assertEquals("0.00", ptf.getCash(USD).setScale(2, HALF_UP).toPlainString());
            assertEquals("68", ptf.getHoldingQty(DE, "SXR8").toString());
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgressOffline(
                    account2, parse("2023-01-23"), parse("2025-09-18")
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            ptf = new SimplePtf(ptfActivity.transactions());
            assertEquals(2, ptf.getCurrencies().size());
            assertEquals("83", ptf.getHoldingQty(DE, "SXR8").toString());
            assertEquals("24", ptf.getHoldingQty(DE, "SIE").toString());
            List<FinTransaction> dividendTrans1 = ptf.getTransactions().stream()
                    .filter(t -> t.type() == FinTransactionType.DIVIDEND && t.asset().symbol().equals("SIE"))
                    .toList();
            assertEquals(1, dividendTrans1.size());
        }
    }
}

