package test.com.brinvex.ptfactivity.adapter.ibkr;


import com.brinvex.fintypes.enu.Country;
import com.brinvex.brokercon.adapter.ibkr.api.IbkrModule;
import com.brinvex.brokercon.adapter.ibkr.api.service.IbkrPtfActivityProvider;
import com.brinvex.brokercon.core.api.domain.constraints.fintransaction.FinTransactionConstraints;
import com.brinvex.brokercon.core.api.domain.PtfActivity;
import com.brinvex.brokercon.core.api.facade.ValidatorFacade;
import com.brinvex.brokercon.testsupport.SimplePtf;
import com.brinvex.brokercon.testsupport.TestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.math.BigDecimal;

import static com.brinvex.fintypes.enu.Currency.EUR;
import static com.brinvex.fintypes.enu.Currency.USD;
import static com.brinvex.fintypes.enu.Country.DE;
import static com.brinvex.fintypes.enu.Country.US;
import static java.math.RoundingMode.HALF_UP;
import static java.time.Duration.ofMinutes;
import static java.time.LocalDate.now;
import static java.time.LocalDate.parse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IbkrPtfActivityOnlineTest extends IbkrBaseTest {


    // When running from IDEA, @EnableIf is ignored if @EnabledIfSystemProperty is present, but it doesn't cause problem for us.
    @EnabledIf("account2CredentialsIsNotNull")
    @EnabledIfSystemProperty(named = "enableConfidentialTests", matches = "true")
    @Test
    void portfolioProgress1() {
        TestContext brokercon = testCtx.withDmsWorkspace("ibkr-dms-online1");
        IbkrPtfActivityProvider ptfProgressProvider = brokercon.get(IbkrModule.class).ptfProgressProvider();
        ValidatorFacade validator = brokercon.validator();

        SimplePtf ptf;
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgress(
                    account2, parse("2023-01-23"), parse("2024-06-10"), ofMinutes(0)
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            ptf = new SimplePtf(ptfActivity.transactions());

            assertEquals(2, ptf.getCurrencies().size());
            assertEquals(0, ptf.getHoldingQty(US, "NVDA").compareTo(new BigDecimal("60")));
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgress(
                    account2, parse("2023-01-23"), now(), ofMinutes(1)
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            assertTrue(ptf.getTransactions().size() <= ptfActivity.transactions().size());

            ptf = new SimplePtf(ptfActivity.transactions());
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgress(
                    account2, parse("2023-01-23"), now(), ofMinutes(1)
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            assertTrue(ptf.getTransactions().size() <= ptfActivity.transactions().size());

            ptf = new SimplePtf(ptfActivity.transactions());
            assertTrue(ptf.getCurrencies().size() >= 2);
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgress(
                    account2, parse("2023-01-23"), parse("2024-09-05"), ofMinutes(1)
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            ptf = new SimplePtf(ptfActivity.transactions());
            assertEquals(2, ptf.getCurrencies().size());
            assertEquals("6", ptf.getHoldingQty(Country.US, "ILMN").toString());
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgress(
                    account2, parse("2023-01-23"), parse("2024-09-11"), ofMinutes(1)
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            ptf = new SimplePtf(ptfActivity.transactions());
            assertEquals(2, ptf.getCurrencies().size());
            assertEquals("35", ptf.getHoldingQty(DE, "CSPX").toString());
            assertEquals("59.64", ptf.getCash(EUR).setScale(2, HALF_UP).toPlainString());
            assertEquals("517.29", ptf.getCash(USD).setScale(2, HALF_UP).toPlainString());
            assertEquals("5108.86", ptfActivity.netAssetValues().getLast().amount().remainder(new BigDecimal("10000")).setScale(2, HALF_UP).toPlainString());
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgress(
                    account2, parse("2023-01-23"), parse("2024-10-10"), ofMinutes(1)
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            ptf = new SimplePtf(ptfActivity.transactions());
            assertEquals(2, ptf.getCurrencies().size());
            assertEquals("0.02", ptf.getCash(EUR).setScale(2, HALF_UP).toPlainString());
            assertEquals("811.39", ptf.getCash(USD).setScale(2, HALF_UP).toPlainString());
            assertEquals("35", ptf.getHoldingQty(DE, "CSPX").toString());
            assertEquals("1653.08", ptfActivity.netAssetValues().getLast().amount().remainder(new BigDecimal("10000")).setScale(2, HALF_UP).toPlainString());
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgress(
                    account2, parse("2023-01-23"), parse("2024-10-21"), ofMinutes(1)
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            ptf = new SimplePtf(ptfActivity.transactions());
            assertEquals(2, ptf.getCurrencies().size());
            assertEquals("0.02", ptf.getCash(EUR).setScale(2, HALF_UP).toPlainString());
            assertEquals("595.01", ptf.getCash(USD).setScale(2, HALF_UP).toPlainString());
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgress(
                    account2, parse("2023-01-23"), parse("2024-10-31"), ofMinutes(1)
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            ptf = new SimplePtf(ptfActivity.transactions());
            assertEquals(2, ptf.getCurrencies().size());
            assertEquals("206.48", ptf.getCash(EUR).setScale(2, HALF_UP).toPlainString());
            assertEquals("693.38", ptf.getCash(USD).setScale(2, HALF_UP).toPlainString());
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgress(
                    account2, parse("2023-01-23"), parse("2024-11-22"), ofMinutes(1)
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            ptf = new SimplePtf(ptfActivity.transactions());
            assertEquals(2, ptf.getCurrencies().size());
            assertEquals("1206.48", ptf.getCash(EUR).setScale(2, HALF_UP).toPlainString());
            assertEquals("564.24", ptf.getCash(USD).setScale(2, HALF_UP).toPlainString());
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgress(
                    account2, parse("2023-01-23"), parse("2024-11-29"), ofMinutes(1)
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            ptf = new SimplePtf(ptfActivity.transactions());
            assertEquals(2, ptf.getCurrencies().size());
            assertEquals("477.44", ptf.getCash(EUR).setScale(2, HALF_UP).toPlainString());
            assertEquals("587.03", ptf.getCash(USD).setScale(2, HALF_UP).toPlainString());
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgress(
                    account2, parse("2023-01-23"), parse("2024-12-31"), ofMinutes(1)
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            ptf = new SimplePtf(ptfActivity.transactions());
            assertEquals(2, ptf.getCurrencies().size());
            assertEquals("65.66", ptf.getCash(EUR).setScale(2, HALF_UP).toPlainString());
            assertEquals("426.40", ptf.getCash(USD).setScale(2, HALF_UP).toPlainString());
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgress(
                    account2, parse("2023-01-23"), parse("2025-01-15"), ofMinutes(1)
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            ptf = new SimplePtf(ptfActivity.transactions());
            assertEquals(2, ptf.getCurrencies().size());
            assertEquals("43.96", ptf.getCash(EUR).setScale(2, HALF_UP).toPlainString());
            assertEquals("8.52", ptf.getCash(USD).setScale(2, HALF_UP).toPlainString());
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgress(
                    account2, parse("2023-01-23"), parse("2025-01-22"), ofMinutes(1)
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            ptf = new SimplePtf(ptfActivity.transactions());
            assertEquals(2, ptf.getCurrencies().size());
            assertEquals("24.40", ptf.getCash(EUR).setScale(2, HALF_UP).toPlainString());
            assertEquals("18.34", ptf.getCash(USD).setScale(2, HALF_UP).toPlainString());
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgress(
                    account2, parse("2023-01-23"), parse("2025-01-27"), ofMinutes(1)
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            ptf = new SimplePtf(ptfActivity.transactions());
            assertEquals(2, ptf.getCurrencies().size());
            assertEquals("114.42", ptf.getCash(EUR).setScale(2, HALF_UP).toPlainString());
            assertEquals("19.78", ptf.getCash(USD).setScale(2, HALF_UP).toPlainString());
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgress(
                    account2, parse("2023-01-23"), parse("2025-01-28"), ofMinutes(1)
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            ptf = new SimplePtf(ptfActivity.transactions());
            assertEquals(2, ptf.getCurrencies().size());
            assertEquals("114.42", ptf.getCash(EUR).setScale(2, HALF_UP).toPlainString());
            assertEquals("20.01", ptf.getCash(USD).setScale(2, HALF_UP).toPlainString());
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgress(
                    account2, parse("2023-01-23"), parse("2025-01-29"), ofMinutes(1)
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            ptf = new SimplePtf(ptfActivity.transactions());
            assertEquals(2, ptf.getCurrencies().size());
            assertEquals("114.42", ptf.getCash(EUR).setScale(2, HALF_UP).toPlainString());
            assertEquals("20.01", ptf.getCash(USD).setScale(2, HALF_UP).toPlainString());
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgress(
                    account2, parse("2023-01-23"), parse("2025-01-31"), ofMinutes(1)
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            ptf = new SimplePtf(ptfActivity.transactions());
            assertEquals(2, ptf.getCurrencies().size());
            assertEquals("114.42", ptf.getCash(EUR).setScale(2, HALF_UP).toPlainString());
            assertEquals("31.89", ptf.getCash(USD).setScale(2, HALF_UP).toPlainString());
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgress(
                    account2, parse("2023-01-23"), parse("2025-06-04"), ofMinutes(1)
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            ptf = new SimplePtf(ptfActivity.transactions());
            assertEquals(2, ptf.getCurrencies().size());
            assertEquals("0.00", ptf.getCash(USD).setScale(2, HALF_UP).toPlainString());
            assertEquals("68", ptf.getHoldingQty(DE, "CSPX").toString());
        }
        {
            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgress(
                    account2, parse("2023-01-23"), parse("2025-09-16"), ofMinutes(1)
            );
            validator.validateAndThrow(ptfActivity.transactions(), FinTransactionConstraints::of);
            ptf = new SimplePtf(ptfActivity.transactions());
            assertEquals(2, ptf.getCurrencies().size());
            assertEquals("81", ptf.getHoldingQty(DE, "CSPX").toString());
        }
    }
}

