package com.brinvex.brokercon.adapter.amnd.api.service;

import com.brinvex.brokercon.core.api.domain.Account;
import com.brinvex.brokercon.core.api.domain.PtfActivity;
import com.brinvex.brokercon.core.api.provider.PtfActivityProvider;

import java.time.LocalDate;

public interface AmndPtfActivityProvider extends PtfActivityProvider {

    PtfActivity getPtfProgress(
            Account account,
            LocalDate fromDateIncl,
            LocalDate toDateIncl
    );

}
