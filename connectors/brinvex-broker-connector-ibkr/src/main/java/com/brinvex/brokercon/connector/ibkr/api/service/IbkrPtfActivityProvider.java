package com.brinvex.brokercon.connector.ibkr.api.service;

import com.brinvex.brokercon.connector.ibkr.api.model.IbkrAccount;
import com.brinvex.brokercon.core.api.domain.PtfActivity;
import com.brinvex.brokercon.core.api.provider.PtfActivityProvider;

import java.time.Duration;
import java.time.LocalDate;

public interface IbkrPtfActivityProvider extends PtfActivityProvider {

    PtfActivity getPtfProgress(
            IbkrAccount ibkrAccount,
            LocalDate fromDateIncl,
            LocalDate toDateIncl,
            Duration staleTolerance
    );

    PtfActivity getPtfProgressOffline(
            IbkrAccount ibkrAccount,
            LocalDate fromDateIncl,
            LocalDate toDateIncl
    );
}
