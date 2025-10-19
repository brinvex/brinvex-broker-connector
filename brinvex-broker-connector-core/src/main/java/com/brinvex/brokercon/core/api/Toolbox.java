package com.brinvex.brokercon.core.api;

import com.brinvex.brokercon.core.api.facade.JsonMapperFacade;
import com.brinvex.brokercon.core.api.facade.PdfReaderFacade;
import com.brinvex.brokercon.core.api.facade.ValidatorFacade;

public interface Toolbox {

    ValidatorFacade validator();

    PdfReaderFacade pdfReader();

    JsonMapperFacade jsonMapper();
}
