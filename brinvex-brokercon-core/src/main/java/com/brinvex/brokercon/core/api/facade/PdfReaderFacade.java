package com.brinvex.brokercon.core.api.facade;

import java.util.List;

public interface PdfReaderFacade {

    List<String> readPdfLines(byte[] pdfContent);

}
