package test.com.brinvex.brokercon.adapter.rvlt.impl.service;

import com.brinvex.brokercon.adapter.rvlt.api.RvltModule;
import com.brinvex.brokercon.core.api.domain.Account;
import com.brinvex.brokercon.testsupport.TestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class RvltBaseTest {

    protected final Logger LOG = LoggerFactory.getLogger(getClass());

    protected final TestContext testCtx = new TestContext(RvltModule.class);

    protected final Account account1 = Account.of(testCtx.subProperties("account1"));

    public boolean account1IsNotNull() {
        return account1 != null;
    }
}
