package com.brinvex.brokercon.connector.ibkr.api.service;


import com.brinvex.brokercon.connector.ibkr.api.model.statement.FlexStatement.ActivityStatement;

import java.util.Collection;
import java.util.Optional;

public interface IbkrStatementMerger {

    Optional<ActivityStatement> mergeActivityStatements(Collection<ActivityStatement> flexStatements);

}
