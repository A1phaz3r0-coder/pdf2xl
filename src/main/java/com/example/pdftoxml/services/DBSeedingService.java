package com.example.pdftoxml.services;

import com.example.pdftoxml.entities.PDFRecord;
import com.example.pdftoxml.utils.PDFUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.PageIterator;
import technology.tabula.Table;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DBSeedingService {
    private static final Logger LOG = LoggerFactory.getLogger(DBSeedingService.class);

    private final PdfRecordService pdfRecordService;

    public DBSeedingService(PdfRecordService pdfRecordService) {
        this.pdfRecordService = pdfRecordService;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        LOG.info("Got the prepared event logged! will start seeding now!");
        doSeedingForDB();
    }


    public void doSeedingForDB() {
        if (pdfRecordService.findAllCount() > 0) {
            LOG.info("Found all pdf records! No need to seed!");
            return;
        }
        try {
            this.readAndSaveToDB();
            LOG.info("All records saved!");
            LOG.info("Done seeding now!");
        } catch (final Exception e) {
            LOG.error("following error occurred during saving records: {}", e.getMessage(), e);
        }
    }

    public void readAndSaveToDB() throws IOException {
        final File pdfFile = ResourceUtils.getFile("classpath:pdf/202602271490813983.pdf");

        try (PDDocument document = PDDocument.load(pdfFile)) {
            final ObjectExtractor extractor = new ObjectExtractor(document);
            final SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();

            final PageIterator pageIterator = extractor.extract();
            int i = 0;
            boolean isLimited = false;
            while (pageIterator.hasNext()) {
                final List<PDFRecord> pdfRecords = new ArrayList<>();
                if (i == 2 && isLimited) {
                    break;
                }
                i++;
                LOG.info("!!Processing {} page!!", i);
                final Page page = pageIterator.next();
                final List<Table> tables = sea.extract(page);
                System.out.println("table size: " + tables.size());
                PDFUtils.readTables(tables, pdfRecords);
                pdfRecordService.saveAll(pdfRecords);
            }

        }
    }
}
