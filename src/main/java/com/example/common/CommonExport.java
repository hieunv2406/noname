package com.example.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class CommonExport {

    public static final String XLSX_FILE_EXTENTION = ".xlsx";
    public static final String DOC_FILE_EXTENTION = ".doc";
    public static final String DOCX_FILE_EXTENTION = ".docx";
    public static final String XLSM_FILE_EXTENTION = ".xlsm";
    public static final String PDF_FILE_EXTENTION = ".pdf";
    public static final String XLS_FILE_EXTENTION = ".xls";

    public static File exportExcel(
            String pathTemplate,
            String fileNameOut,
            List<ConfigFileExport> config,
            String pathOut,
            String... exportChart
    ) throws Exception {
        File folderOut = new File(pathOut);
        if (!folderOut.exists()) {
            folderOut.mkdir();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("dd/MM/yyy HH:mm:ss");
        String strCurTimeExp = dateFormat.format(new Date());
        strCurTimeExp = strCurTimeExp.replaceAll("/", "_");
        strCurTimeExp = strCurTimeExp.replaceAll(" ", "_");
        strCurTimeExp = strCurTimeExp.replaceAll(":", "_");
        pathOut = pathOut + fileNameOut + strCurTimeExp
                + (exportChart != null && exportChart.length > 0 ? XLSM_FILE_EXTENTION : XLSX_FILE_EXTENTION);
        HSSFWorkbook hssfWorkbook = null;
        try {
            log.info("Start get template file!");
//        pathTemplate = StringUtils.removeSeparator(pathTemplate);
            Resource resource = new ClassPathResource(pathTemplate);
            InputStream fileTemplate = resource.getInputStream();
            XSSFWorkbook workbookTemp = new XSSFWorkbook(fileTemplate);
            log.info("End get template file!");
            SXSSFWorkbook workbook = new SXSSFWorkbook(workbookTemp, 1000);
            hssfWorkbook = new HSSFWorkbook();

            // <editor-fold defaultstate="collapsed" desc="Declare style">

            // </editor-fold>

            for (ConfigFileExport item : config) {
                Map<String, String> fieldSpit = item.getFieldSplit();
                SXSSFSheet sheet;
                if (exportChart != null && exportChart.length > 0) {
                    sheet = workbook.getSheetAt(0);
                } else {
                    sheet = workbook.createSheet(item.getSheetName());
                }
                //title
                Row rowMainTitle = sheet.createRow(item.getCellTitleIndex());
                Cell cellMainTitle;
                if (item.getCustomTitle() != null && item.getCustomTitle().length > 0) {
                    cellMainTitle = rowMainTitle.createCell(0);
                } else {
                    cellMainTitle = rowMainTitle.createCell(1);
                }
                cellMainTitle.setCellValue(DataUtil.isNullOrEmpty(item.getTitle()) ? "" : item.getTitle());
                //sub title
                int indexSubTitle = DataUtil.isNullOrEmpty(item.getSubTitle()) ? item.getCellTitleIndex() :
                        item.getCellTitleIndex() + 2;
                Row rowSubTitle;
                int indexRowData = 0;

                // <editor-fold defaultstate="collapsed" desc="Build header">
                if (item.isCreateHeader()) {
                    int index = -1;
                    Cell cellHeader;
                    Row rowHeader = sheet.createRow(item.getStartRow());
                    rowHeader.setHeight((short) 500);
                    Row rowHeaderSub = null;
                    for (ConfigHeaderExport header : item.getHeader()) {
                        if (fieldSpit != null) {
                            if (fieldSpit.get(header.getFieldName()) != null) {
                                String[] fieldSplitHead = fieldSpit.get(header.getFieldName()).split(item.getSplitChar());
                                for (String field : fieldSplitHead) {
                                    cellHeader = rowHeader.createCell(index + 2);
                                    cellHeader.setCellValue(DataUtil.isNullOrEmpty(field) ? "" : field.replaceAll("\\<.*?>", " "));
                                    if (header.isHasMerge()) {
                                        CellRangeAddress cellRangeAddress = new CellRangeAddress(
                                                item.getStartRow(), item.getStartRow() + header.getMergeRow(),
                                                index + 2, index + 2 + header.getMergeColumn()
                                        );
                                        sheet.addMergedRegion(cellRangeAddress);
                                        RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
                                        RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
                                        RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
                                        RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
                                        if (header.getMergeRow() > 0) {
                                            indexRowData = header.getMergeRow();
                                        }
                                        if (header.getMergeColumn() > 0) {
                                            index++;
                                        }
                                        if (header.getSubHeader().length > 0) {
                                            if (rowHeaderSub == null) {
                                                rowHeaderSub = sheet.createRow(item.getStartRow() + 1);
                                            }
                                            int k = index + 1;
                                            int s = 0;
                                            for (String sub : header.getSubHeader()) {
                                                Cell cellHeaderSub1 = rowHeaderSub.createCell(k);
                                                cellHeaderSub1.setCellValue(item.getHeaderPrefix() + "." + sub);
                                                k++;
                                                s++;
                                            }
                                        }
                                    }
//                                    cellHeader.setCellStyle();
                                    index++;
                                }
                            } else {
                                cellHeader = rowHeader.createCell(index + 2);
                                cellHeader.setCellValue(item.getHeaderPrefix() + "." + header.getFieldName());
                                if (header.isHasMerge()) {
                                    CellRangeAddress cellRangeAddress = new CellRangeAddress(
                                            item.getStartRow(), item.getStartRow() + header.getMergeRow(),
                                            index + 2, index + 2 + header.getMergeColumn()
                                    );
                                    sheet.addMergedRegion(cellRangeAddress);
                                    RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
                                    RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
                                    RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
                                    RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
                                    if (header.getMergeRow() > 0) {
                                        indexRowData = header.getMergeRow();
                                    }
                                    if (header.getMergeColumn() > 0) {
                                        index++;
                                    }
                                }
//                                cellHeader.setCellStyle();
                                index++;
                            }
                        } else {
                            cellHeader = rowHeader.createCell(index + 2);
                            cellHeader.setCellValue(item.getHeaderPrefix() + "." + header.getFieldName());
                            if (header.isHasMerge()) {
                                CellRangeAddress cellRangeAddress = new CellRangeAddress(
                                        item.getStartRow(), item.getStartRow() + header.getMergeRow(),
                                        index + 2, index + 2 + header.getMergeColumn()
                                );
                                sheet.addMergedRegion(cellRangeAddress);
                                RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
                                RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
                                RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
                                RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
                                if (header.getMergeRow() > 0) {
                                    indexRowData = header.getMergeRow();
                                }
                                if (header.getMergeColumn() > 0) {
                                    index++;
                                }
                            }
//                                cellHeader.setCellStyle();
                            index++;
                        }
                    }
                }
                // </editor-fold>

                // <editor-fold defaultstate="collapsed" desc="Fill Data">
                if (item.getLstData() != null && !item.getLstData().isEmpty()) {
                    //init mapColumn
                    Object firstRow = item.getLstData().get(0);
                    Map<String, Field> mapField = new HashMap<>();
                    for (ConfigHeaderExport header : item.getHeader()) {
                        for (Field f : firstRow.getClass().getDeclaredFields()) {
                            f.setAccessible(true);
                            if (f.getName().equals(header.getFieldName())) {
                                mapField.put(header.getFieldName(), f);
                            }
                            String[] replace = header.getReplace();
                            if (!DataUtil.isNullOrEmpty(replace)) {
                                if (replace.length > 2) {
                                    for (int n = 2; n < replace.length; n++) {
                                        if (f.getName().equals(replace[n])) {
                                            mapField.put(replace[n], f);
                                        }
                                    }
                                }
                            }
                        }
                        if (firstRow.getClass().getSuperclass() != null) {
                            for (Field f : firstRow.getClass().getSuperclass().getDeclaredFields()) {
                                f.setAccessible(true);
                                if (f.getName().equals(header.getFieldName())) {
                                    mapField.put(header.getFieldName(), f);
                                }
                                String[] replace = header.getReplace();
                                if (!DataUtil.isNullOrEmpty(replace)) {
                                    if (replace.length > 2) {
                                        for (int n = 2; n < replace.length; n++) {
                                            if (f.getName().equals(replace[n])) {
                                                mapField.put(replace[n], f);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //fill Data
                    Row row;
                    List listData = item.getLstData();
                    List<ConfigHeaderExport> listHeader = item.getHeader();
                    int startRow = item.getStartRow();
                    String splitChar = item.getSplitChar();
                    for (int i=0; i< listData.size(); i++){
                        row = sheet.createRow(i + startRow+1+indexRowData);
                        row.setHeight((short) 250);
                        Cell cell;

                        cell = row.createCell(0);
                        cell.setCellValue(i+1);
//                        cell.setCellStyle();
                        int j = 0;
                        for (int e = 0; e< listHeader.size();e++){
                            ConfigHeaderExport head = listHeader.get(e);
                            String header = head.getFieldName();
                            String align = head.getAlign();
                            Object obj = listData.get(i);

                            Field f = mapField.get(header);
                            if (!DataUtil.isNullOrEmpty(fieldSpit) && fieldSpit.containsKey(header)){
                                String[] arrHead = fieldSpit.get(header).split(splitChar);
                                String value = "";
                                Object tempValue = f.get(obj);
                                if (tempValue instanceof Date){
//                                    value = tempValue == null ? "": tempValue;
                                }
                            }
                        }
                    }
                }
                // </editor-fold>


            }
        } catch (Exception e) {

        }
        return null;
    }
}
