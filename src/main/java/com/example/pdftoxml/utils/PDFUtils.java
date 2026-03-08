package com.example.pdftoxml.utils;

import com.example.pdftoxml.entities.PDFRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.PageIterator;
import technology.tabula.RectangularTextContainer;
import technology.tabula.Table;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class PDFUtils {

    private static final Logger LOG = LoggerFactory.getLogger(PDFUtils.class);

    private enum POSITION {
        S_NO, CLASS, ROLL, APPNO, CANDIDATE_NAME, GENDER, CAT, DOMICILE, TOT_MRK, RESULT, AIRANK
    }

    public static List<PDFRecord> read(final String fileName) throws FileNotFoundException {
        // Path to your PDF file
//        File pdfFile = new File("sample_data.pdf");
        final File pdfFile = ResourceUtils.getFile("classpath:pdf/" + fileName);

        try (PDDocument document = PDDocument.load(pdfFile)) {
            ObjectExtractor extractor = new ObjectExtractor(document);
            SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();

            // Extract the first page
            PageIterator pageIterator = extractor.extract();
//            Page page = extractor.extract(1);
            List<PDFRecord> pdfRecords = new ArrayList<>();
            int i = 0;
            boolean isLimited = false;
            while (pageIterator.hasNext()) {
                if (i == 2 && isLimited) {
                    break;
                }
                i++;
                LOG.info("!!Processing {} page!!", i);
                Page page = pageIterator.next();
                List<Table> tables = sea.extract(page);
                System.out.println("table size: " + tables.size());
                readTables(tables, pdfRecords);
            }

            // Extract tables using the "Spreadsheet" algorithm
            // (Works best for tables with visible grid lines)
            return pdfRecords;

        } catch (Exception e) {
            System.err.println("Error reading PDF: " + e.getMessage());
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static void readTables(final List<Table> tables, final List<PDFRecord> pdfRecords) {
        int i = 0;
        for (Table table : tables) {
            List<List<RectangularTextContainer>> rows = table.getRows();
            for (List<RectangularTextContainer> cells : rows) {
                if (i == 0) {
                    i++;
                    continue;
                }
                final PDFRecord record = new PDFRecord();
                record.setSerialNo(Long.parseLong(StringUtils.defaultIfBlank(cells.get(POSITION.S_NO.ordinal()).getText(), "0")));
                record.setRollNo(Long.parseLong(StringUtils.defaultIfBlank(cells.get(POSITION.ROLL.ordinal()).getText(), "0")));
                record.setStandard(StringUtils.defaultIfBlank(cells.get(POSITION.CLASS.ordinal()).getText(), ""));
                record.setAppNo(Long.parseLong(StringUtils.defaultIfBlank(cells.get(POSITION.APPNO.ordinal()).getText(), "0")));
                record.setCandidateName(StringUtils.defaultIfBlank(cells.get(POSITION.CANDIDATE_NAME.ordinal()).getText(), StringUtils.EMPTY));
                record.setGender(StringUtils.defaultIfBlank(cells.get(POSITION.GENDER.ordinal()).getText(), StringUtils.EMPTY));
                record.setCategory(StringUtils.defaultIfBlank(cells.get(POSITION.CAT.ordinal()).getText(), StringUtils.EMPTY));
                record.setDomicile(StringUtils.defaultIfBlank(cells.get(POSITION.DOMICILE.ordinal()).getText(), StringUtils.EMPTY));
                record.setTotalMarks(Integer.parseInt(StringUtils.defaultIfBlank(cells.get(POSITION.TOT_MRK.ordinal()).getText(), "0")));
                record.setResult(StringUtils.defaultIfBlank(cells.get(POSITION.RESULT.ordinal()).getText(), StringUtils.EMPTY));
                record.setAiRank(Integer.parseInt(StringUtils.defaultIfBlank(cells.get(POSITION.AIRANK.ordinal()).getText(), "0")));
                pdfRecords.add(record);
            }
        }
    }

}