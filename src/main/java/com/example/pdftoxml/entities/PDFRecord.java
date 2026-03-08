package com.example.pdftoxml.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PDFRecord {
    private Long id;
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
