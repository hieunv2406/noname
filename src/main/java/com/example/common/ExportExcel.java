package com.example.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author owl
 * @CreatedDate 06/07/2021
 * @PACKAGE com.example.common
 */
@Slf4j
public class ExportExcel {
    private Workbook workbook;
    private FileOutputStream fileOutputStream;

    public void saveFileToExcel(Workbook workbook, String pathName, String fileName) {
        try {
            File file = new File(pathName);
            if (!file.exists()) {
                file.mkdir();
            }
            fileOutputStream = new FileOutputStream(pathName + fileName);
            workbook.write(fileOutputStream);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException ioException) {
                log.error(ioException.getMessage(), ioException);
            }
        }
    }
    public Cell createCell(Sheet sheet, int c, int r, String cellValue, CellStyle style) {
        Row row = sheet.getRow(r);
        if (row == null) {
            row = sheet.createRow(r);
        }
        // Create a cell and put a value in it.
        Cell cell = row.createCell(c);
        cell.setCellValue(cellValue);
        cell.setCellStyle(style);
        return cell;
    }
}
