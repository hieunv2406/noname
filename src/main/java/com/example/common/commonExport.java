package com.example.common;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;

@Slf4j
public class commonExport {

    public static final String XLSX_FILE_EXTENTION = ".xlsx";
    public static final String DOC_FILE_EXTENTION = ".doc";
    public static final String DOCX_FILE_EXTENTION = ".docx";
    public static final String XLSM_FILE_EXTENTION = ".xlsm";
    public static final String PDF_FILE_EXTENTION = ".pdf";
    public static final String XLS_FILE_EXTENTION = ".xls";

    public static File exportExcel(
            String pathTemplate,
            String fileNameOut,
//            List<> config,
            String pathOut,
            String... exportChart
    ) throws Exception {

        return null;
    }
}
