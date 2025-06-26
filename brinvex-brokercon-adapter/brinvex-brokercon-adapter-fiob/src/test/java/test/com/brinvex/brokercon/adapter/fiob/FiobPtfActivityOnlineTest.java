package test.com.brinvex.brokercon.adapter.fiob;


import com.brinvex.brokercon.adapter.fiob.api.FiobModule;
import com.brinvex.brokercon.adapter.fiob.api.service.FiobPtfActivityProvider;
import com.brinvex.brokercon.core.api.domain.PtfActivity;
import com.brinvex.brokercon.core.api.facade.ValidatorFacade;
import com.brinvex.brokercon.core.api.domain.FinTransaction;
import com.brinvex.brokercon.core.api.domain.constraints.fintransaction.FinTransactionConstraints;
import com.brinvex.brokercon.testsupport.ObfuscationUtil;
import com.brinvex.brokercon.testsupport.SimplePtf;
import com.brinvex.brokercon.testsupport.TestContext;
import com.brinvex.fintypes.enu.Country;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.SequencedCollection;
import java.util.Set;

import static com.brinvex.fintypes.enu.Currency.CZK;
import static com.brinvex.fintypes.enu.Currency.EUR;
import static com.brinvex.brokercon.testsupport.AssertionUtil.assertPtfSnapshotEqual;
import static java.math.BigDecimal.ZERO;
import static java.time.Duration.ofMinutes;
import static java.time.LocalDate.parse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class FiobPtfActivityOnlineTest extends FiobBaseTest {

    @EnabledIf("account1CredentialsIsNotNull")
    @EnabledIfSystemProperty(named = "enableConfidentialTests", matches = "true")
    @Test
    void ptfProgressOnline_account1() {
        assert account1 != null;

        String dmsWorkspace = "fiob-dms-online1";
        TestContext testCtx = this.testCtx.withDmsWorkspace(dmsWorkspace);
        FiobModule fiobModule = testCtx.get(FiobModule.class);
        FiobPtfActivityProvider ptfProgressProvider = fiobModule.ptfProgressProvider();
        ValidatorFacade validator = testCtx.validator();

        {
            LocalDate fromDateIncl = parse("2019-01-05");
            LocalDate toDateIncl = parse("2024-11-19");

            SimplePtf expectedPtf = loadExpectedPtfSnapshot(testCtx, account1, toDateIncl);

            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgress(account1, fromDateIncl, toDateIncl, ofMinutes(15));

            SequencedCollection<FinTransaction> trans = ptfActivity.transactions();
            assertFalse(trans.isEmpty());
            validator.validateAndThrow(trans, FinTransactionConstraints::of);

            SimplePtf ptf = new SimplePtf(trans);
            assertPtfSnapshotEqual(expectedPtf, ptf);
        }
    }

    @EnabledIf("account1CredentialsIsNotNull")
    @EnabledIfSystemProperty(named = "enableConfidentialTests", matches = "true")
    @Test
    void ptfProgressOnline_account1_2() {
        assert account1 != null;

        String dmsWorkspace = "fiob-dms-online1";
        TestContext testCtx = this.testCtx.withDmsWorkspace(dmsWorkspace);
        FiobModule fiobModule = testCtx.get(FiobModule.class);
        FiobPtfActivityProvider ptfProgressProvider = fiobModule.ptfProgressProvider();
        ValidatorFacade validator = testCtx.validator();

        {
            LocalDate fromDateIncl = parse("2019-01-05");
            LocalDate toDateIncl = parse("2025-05-16");

            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgress(account1, fromDateIncl, toDateIncl, ofMinutes(15));

            SequencedCollection<FinTransaction> trans = ptfActivity.transactions();
            assertFalse(trans.isEmpty());
            validator.validateAndThrow(trans, FinTransactionConstraints::of);

            SimplePtf ptf = new SimplePtf(trans);
            assertEquals(0, ptf.getHoldingQty(Country.US, "GOLD").compareTo(ZERO));
        }
    }

    @EnabledIf("account1CredentialsIsNotNull")
    @EnabledIfSystemProperty(named = "enableConfidentialTests", matches = "true")
    @Test
    void ptfProgressOnline_account1_3() {
        assert account1 != null;

        String dmsWorkspace = "fiob-dms-online1";
        TestContext testCtx = this.testCtx.withDmsWorkspace(dmsWorkspace);
        FiobModule fiobModule = testCtx.get(FiobModule.class);
        FiobPtfActivityProvider ptfProgressProvider = fiobModule.ptfProgressProvider();
        ValidatorFacade validator = testCtx.validator();

        {
            LocalDate fromDateIncl = parse("2019-01-05");
            LocalDate toDateIncl = parse("2025-06-26");

            SimplePtf expectedPtf = loadExpectedPtfSnapshot(testCtx, account1, toDateIncl);

            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgress(account1, fromDateIncl, toDateIncl, ofMinutes(15));

            SequencedCollection<FinTransaction> trans = ptfActivity.transactions();
            assertFalse(trans.isEmpty());
            validator.validateAndThrow(trans, FinTransactionConstraints::of);

            SimplePtf ptf = new SimplePtf(trans);
            assertPtfSnapshotEqual(expectedPtf, ptf);
        }
    }

    @EnabledIf("account4CredentialsIsNotNull")
    @EnabledIfSystemProperty(named = "enableConfidentialTests", matches = "true")
    @Test
    void ptfProgressOnline_account3() {
        assert account3 != null;

        String dmsWorkspace = "fiob-dms-online1";
        TestContext testCtx = this.testCtx.withDmsWorkspace(dmsWorkspace);
        FiobModule fiobModule = testCtx.get(FiobModule.class);
        FiobPtfActivityProvider ptfProgressProvider = fiobModule.ptfProgressProvider();
        ValidatorFacade validator = testCtx.validator();

        {
            LocalDate fromDateIncl = parse("2022-01-01");
            LocalDate toDateIncl = parse("2024-11-18");

            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgress(account3, fromDateIncl, toDateIncl, ofMinutes(5));

            SequencedCollection<FinTransaction> trans = ptfActivity.transactions();
            assertFalse(trans.isEmpty());
            validator.validateAndThrow(trans, FinTransactionConstraints::of);

            SimplePtf ptf = new SimplePtf(trans);
            assertEquals(Set.of(CZK), ptf.getCurrencies());
            assertEquals("400.33", ObfuscationUtil.remainder1000.apply(ptf.getCash(CZK)).toPlainString());
        }
    }

    @EnabledIf("account4CredentialsIsNotNull")
    @EnabledIfSystemProperty(named = "enableConfidentialTests", matches = "true")
    @Test
    void ptfProgressOnline_account4() {
        assert account4 != null;

        String dmsWorkspace = "fiob-dms-online1";
        TestContext testCtx = this.testCtx.withDmsWorkspace(dmsWorkspace);
        FiobModule fiobModule = testCtx.get(FiobModule.class);
        FiobPtfActivityProvider ptfProgressProvider = fiobModule.ptfProgressProvider();
        ValidatorFacade validator = testCtx.validator();

        {
            LocalDate fromDateIncl = parse("2022-01-01");
            LocalDate toDateIncl = parse("2024-11-18");

            PtfActivity ptfActivity = ptfProgressProvider.getPtfProgress(account4, fromDateIncl, toDateIncl, ofMinutes(5));

            SequencedCollection<FinTransaction> trans = ptfActivity.transactions();
            assertFalse(trans.isEmpty());
            validator.validateAndThrow(trans, FinTransactionConstraints::of);

            SimplePtf ptf = new SimplePtf(trans);
            assertEquals(Set.of(EUR), ptf.getCurrencies());
            assertEquals("7.44", ObfuscationUtil.remainder10.apply(ptf.getCash(EUR)).toPlainString());
        }
    }

}

