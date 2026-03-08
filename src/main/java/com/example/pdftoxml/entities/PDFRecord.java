package com.example.pdftoxml.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="pdf_record")
@Entity
@Data
@NoArgsConstructor
public class PDFRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long pk;
    private Long serialNo;
    private String standard;
    private Long rollNo;
    private Long appNo;
    private String candidateName;
    private String gender;
    private String category;
    private String domicile;
    private int totalMarks;
    private String result;
    private long aiRank;
}
