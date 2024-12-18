package test.com.brinvex.ptfactivity.adapter.ibkr;


import com.brinvex.finance.types.enu.Country;
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

import static com.brinvex.finance.types.enu.Currency.EUR;
import static com.brinvex.finance.types.enu.Currency.USD;
import static com.brinvex.finance.types.enu.Country.DE;
import static com.brinvex.finance.types.enu.Country.US;
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
    void portfolioProgress() {
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
    }

}

