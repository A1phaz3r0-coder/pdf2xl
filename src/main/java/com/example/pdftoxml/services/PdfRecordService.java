package com.example.pdftoxml.services;

import com.example.pdftoxml.daos.PDFRecordRepository;
import com.example.pdftoxml.entities.PDFRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PdfRecordService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PdfRecordService.class);

    private final PDFRecordRepository pdfRecordRepository;

    public PdfRecordService(PDFRecordRepository pdfRecordRepository) {
        this.pdfRecordRepository = pdfRecordRepository;
    }

    public boolean saveAll(final List<PDFRecord> pdfRecords) {
        try {
            pdfRecordRepository.saveAll(pdfRecords);
            return true;
        } catch (Exception e) {
            LOGGER.error("Some error occurred while saving records,{}", e.getMessage(), e);
        }
        return false;
    }

    public List<PDFRecord> findAll() {
        return pdfRecordRepository.findAll();
    }

    public long findAllCount() {
        return pdfRecordRepository.count();
    }

}
