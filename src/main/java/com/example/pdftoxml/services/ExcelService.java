package com.example.pdftoxml.services;

import com.example.pdftoxml.entities.PDFRecord;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelService {

    private final String[] COLUMNS = {"serial_no", "roll_no", "ai_rank", "candidate_name", "category", "domicile", "gender", "result", "class", "total_marks"};

    private enum POSITION {
        SERIAL_NO, ROLL_NO, AI_RANK, CANDIDATE_NAME, CATEGORY, DOMICILE, GENDER, RESULT, CLASS, TOTAL_MARKS
    }

    @Autowired
    private PdfRecordService pdfRecordService;

    public ByteArrayInputStream generateExcel(final String domicile) throws IOException {

        final Workbook workbook = new SXSSFWorkbook();
        final Sheet sheet = workbook.createSheet("Results_" + domicile);

        final Row headerRow = sheet.createRow(0);

        for (int i = 0; i < COLUMNS.length; i++) {
            headerRow.createCell(i).setCellValue(COLUMNS[i]);
        }

        final List<PDFRecord> pdfRecords = pdfRecordService.findAllByDomicile(domicile);

        int rowIdx = 1;

        for (PDFRecord pdfRecord : pdfRecords) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(POSITION.SERIAL_NO.ordinal()).setCellValue((rowIdx + 1));
            row.createCell(POSITION.ROLL_NO.ordinal()).setCellValue(pdfRecord.getRollNo());
            row.createCell(POSITION.AI_RANK.ordinal()).setCellValue(pdfRecord.getAiRank());
            row.createCell(POSITION.CANDIDATE_NAME.ordinal()).setCellValue(pdfRecord.getCandidateName());
            row.createCell(POSITION.CATEGORY.ordinal()).setCellValue(pdfRecord.getCategory());
            row.createCell(POSITION.DOMICILE.ordinal()).setCellValue(pdfRecord.getDomicile());
            row.createCell(POSITION.GENDER.ordinal()).setCellValue(pdfRecord.getGender());
            row.createCell(POSITION.RESULT.ordinal()).setCellValue(pdfRecord.getResult());
            row.createCell(POSITION.CLASS.ordinal()).setCellValue(pdfRecord.getStandard());
            row.createCell(POSITION.TOTAL_MARKS.ordinal()).setCellValue(pdfRecord.getTotalMarks());
        }

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
}