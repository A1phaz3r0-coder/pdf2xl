package com.example.pdftoxml.daos;

import com.example.pdftoxml.entities.PDFRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PDFRecordRepository extends JpaRepository<PDFRecord, Long> {
    public List<PDFRecord> findByDomicile(final String domicile);
}
