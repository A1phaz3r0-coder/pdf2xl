package com.example.pdftoxml.controllers;

import com.example.pdftoxml.entities.PDFRecord;
import com.example.pdftoxml.services.ExcelService;
import com.example.pdftoxml.services.PdfRecordService;
import jakarta.ws.rs.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PDFController {

    private static final Logger LOG = LoggerFactory.getLogger(PDFController.class);

    @Autowired
    private ExcelService excelService;
    @Autowired
    private PdfRecordService pdfRecordService;

    @GetMapping()
    public String helloWorld() {
        return "hello world";
    }

    @GetMapping("/records/{domicile}")
    public ResponseEntity<List<PDFRecord>> generateExcel(@PathVariable("domicile") String domicile) {
        return ResponseEntity.ok().body(pdfRecordService.findAllByDomicile(domicile));
    }

    @GetMapping("/excel")
    public ResponseEntity<InputStreamResource> getExcelForDomicile(@PathParam(value = "state") final String domicile) {
        try {
            final InputStreamResource file = new InputStreamResource(excelService.generateExcel(domicile));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Results_" + domicile + ".xlsx")
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(file);
        } catch (final Exception e) {
            LOG.error("Error while creating the excel: {}", e.getMessage(), e);
        }
        return ResponseEntity.internalServerError().build();

    }
}
