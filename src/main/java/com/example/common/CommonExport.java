package com.example.common;

import com.example.common.config.ConfigFileExport;
import com.example.common.config.ConfigHeaderExport;
import com.example.common.utils.DataUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class CommonExport {

    private final static Locale locale = LocaleContextHolder.getLocale();
    public static final String XLSX_FILE_EXTENTION = ".xlsx";
    public static final String DOC_FILE_EXTENTION = ".doc";
    public static final String DOCX_FILE_EXTENTION = ".docx";
    public static final String XLSM_FILE_EXTENTION = ".xlsm";
    public static final String PDF_FILE_EXTENTION = ".pdf";
    public static final String XLS_FILE_EXTENTION = ".xls";
    private static Object workbook;

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
        //cắt tên file + thời gian
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("dd/MM/yyy HH:mm:ss");
        String strCurTimeExp = dateFormat.format(new Date());
        strCurTimeExp = strCurTimeExp.replaceAll("/", "_");
        strCurTimeExp = strCurTimeExp.replaceAll(" ", "_");
        strCurTimeExp = strCurTimeExp.replaceAll(":", "_");
        pathOut = pathOut + fileNameOut + strCurTimeExp
                + (exportChart != null && exportChart.length > 0 ? XLSM_FILE_EXTENTION : XLSX_FILE_EXTENTION);
        HSSFWorkbook hssfWorkbook = null;
        XSSFWorkbook workbookTemp = null;
        SXSSFWorkbook workbook = null;
        InputStream fileTemplate = null;
        try {
            log.info("Start get template file!");
            pathTemplate = DataUtil.replaceSeparator(pathTemplate);
            Resource resource = new ClassPathResource(pathTemplate);
            fileTemplate = resource.getInputStream();
            workbookTemp = new XSSFWorkbook(fileTemplate);
            log.info("End get template file!");
            workbook = new SXSSFWorkbook(workbookTemp, 1000);
            hssfWorkbook = new HSSFWorkbook();

            // <editor-fold defaultstate="collapsed" desc="Declare style">

            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Config">
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
                                                cellHeaderSub1.setCellValue(I18n.getLanguage(item.getHeaderPrefix() + "." + sub));
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
                                cellHeader.setCellValue(I18n.getLanguage(item.getHeaderPrefix() + "." + header.getFieldName()));
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
                    for (int i = 0; i < listData.size(); i++) {
                        row = sheet.createRow(i + startRow + 1 + indexRowData);
                        row.setHeight((short) 250);
                        Cell cell;

                        cell = row.createCell(0);
                        cell.setCellValue(i + 1);
//                        cell.setCellStyle();
                        int j = 0;
                        for (int e = 0; e < listHeader.size(); e++) {
                            ConfigHeaderExport head = listHeader.get(e);
                            String header = head.getFieldName();
                            String align = head.getAlign();
                            Object obj = listData.get(i);

                            Field f = mapField.get(header);
                            if (!DataUtil.isNullOrEmpty(fieldSpit) && fieldSpit.containsKey(header)) {
                                String[] arrHead = fieldSpit.get(header).split(splitChar);
                                String value = "";
                                Object tempValue = f.get(obj);
                                if (tempValue instanceof Date) {
                                    value = DataUtil.isNullOrEmpty(tempValue) ? "" : DataUtil.convertDateToString((Date) tempValue);
                                } else {
                                    value = DataUtil.isNullOrEmpty(tempValue) ? "" : tempValue.toString();
                                }
                                String[] fieldSplitValue = value.split(splitChar);
                                for (int m = 0; m < arrHead.length; m++) {
                                    if (head.isHasMerge() && head.getSubHeader().length > 0) {
                                        int s = 0;
                                        for (String sub : head.getSubHeader()) {
                                            cell = row.createCell(j + 1);
                                            String[] replace = head.getReplace();
                                            if (!DataUtil.isNullOrEmpty(replace)) {
                                                List<String> more = new ArrayList<>();
                                                if (replace.length > 2) {
                                                    for (int n = 2; n < replace.length; n++) {
                                                        Object objStr = mapField.get(replace[n]).get(obj);
                                                        String valueStr = objStr == null ? "" : objStr.toString();
                                                        more.add(valueStr);
                                                    }
                                                }
                                                if ("NUMBER".equals(head.getStyleFormat())) {
                                                    double numberValue = replaceNumberValue(replace[0], m,
                                                            more, s);
                                                    if (Double.compare(numberValue, -888) == 0) {
                                                        cell.setCellValue("*");
                                                    } else if (Double.compare(numberValue, -999) == 0) {
                                                        cell.setCellValue("-");
                                                    } else {
                                                        cell.setCellValue(numberValue);
                                                    }
                                                } else {
                                                    cell.setCellValue(replaceStringValue(replace[0], m, more, s));
                                                }
                                                s++;
                                            } else {
                                                String subValue = "";
                                                for (Field subf : firstRow.getClass().getDeclaredFields()) {
                                                    subf.setAccessible(true);
                                                    if (subf.getName().equals(sub)) {
                                                        String[] arrSub = (subf.get(obj) == null ? "" : subf.get(
                                                                obj).toString()).split(item.getSplitChar());
                                                        subValue = arrSub[m];
                                                    }
                                                }
                                                if ("NUMBER".equals(head.getStyleFormat())) {
                                                    if (!DataUtil.isNullOrEmpty(subValue)) {
                                                        cell.setCellValue(Double.valueOf(subValue));
                                                    } else {
                                                        cell.setCellValue(subValue == null ? "" : subValue);
                                                    }
                                                } else {
                                                    if (subValue == null) {
                                                        cell.setCellValue("");
                                                    } else if (subValue.length() > 32767) {
                                                        cell.setCellValue(subValue.substring(0, 32766));
                                                    } else {
                                                        cell.setCellValue(subValue);//cut cho nay
                                                    }
                                                }
                                            }
                                            if ("CENTER".equals(align)) {
//                                                cell.setCellStyle(cellStyleCenter);
                                            }
                                            if ("LEFT".equals(align)) {
//                                                cell.setCellStyle(cellStyleLeft);
                                            }
                                            if ("RIGHT".equals(align)) {
//                                                cell.setCellStyle(cellStyleRight);
                                            }
                                            j++;
                                        }
                                    } else {
                                        if (item.getCustomColumnWidth() != null
                                                && item.getCustomColumnWidth().length > 0) {
                                            if (j > 0) {
                                                j++;
                                            }
                                            cell = row.createCell(j + 1);
                                        } else {
                                            cell = row.createCell(j + 1);
                                        }
                                        String[] replace = head.getReplace();
                                        if (replace != null) {
                                            Object valueReplace = mapField.get(replace[1]).get(obj);
                                            List<String> more = new ArrayList<>();
                                            if (replace.length > 2) {
                                                for (int n = 2; n < replace.length; n++) {
                                                    Object objStr = mapField.get(replace[n]).get(obj);
                                                    String valueStr = objStr == null ? "" : objStr.toString();
                                                    more.add(valueStr);
                                                }
                                            }
                                            if ("NUMBER".equals(head.getStyleFormat())) {
                                                double numberValue = replaceNumberValue(replace[0],
                                                        valueReplace, more, m);
                                                if (Double.compare(numberValue, -888) == 0) {
                                                    cell.setCellValue("*");
                                                } else if (Double.compare(numberValue, -999) == 0) {
                                                    cell.setCellValue("-");
                                                } else {
                                                    cell.setCellValue(numberValue);
                                                }
                                            } else {
                                                cell.setCellValue(replaceStringValue(replace[0], valueReplace, more, m));
                                            }
                                        } else {
                                            if ("NUMBER".equals(head.getStyleFormat())) {
                                                if (!DataUtil.isNullOrEmpty(fieldSplitValue[m])) {
                                                    cell.setCellValue(Double.valueOf(fieldSplitValue[m]));
                                                } else {
                                                    cell.setCellValue(fieldSplitValue[m] == null ? "" : fieldSplitValue[m]);
                                                }
                                            } else {
                                                cell.setCellValue(fieldSplitValue[m] == null ? "" : fieldSplitValue[m]);
                                            }
                                        }

                                        if ("CENTER".equals(align)) {
//                                            cell.setCellStyle(cellStyleCenter);
                                        }
                                        if ("LEFT".equals(align)) {
//                                            cell.setCellStyle(cellStyleLeft);
                                        }
                                        if ("RIGHT".equals(align)) {
//                                            cell.setCellStyle(cellStyleRight);
                                        }
                                        j++;
                                    }
                                }
                            } else {
                                String value = "";
                                if (f != null) {
                                    Object tempValue = f.get(obj);
                                    if (tempValue instanceof Date) {
                                        value = tempValue == null ? "" : DataUtil.convertDateToString((Date) tempValue);
                                    } else {
                                        value = tempValue == null ? "" : tempValue.toString();
                                    }
                                }
                                if (item.getCustomColumnWidth() != null && item.getCustomColumnWidth().length > 0) {
                                    if (j > 0) {
                                        j++;
                                    }
                                    cell = row.createCell(j + 1);
                                } else {
                                    cell = row.createCell(j + 1);
                                }

                                String[] replace = head.getReplace();
                                if (!DataUtil.isNullOrEmpty(replace)) {
                                    Object valueReplace = mapField.get(replace[1]).get(obj);
                                    List<String> more = new ArrayList<>();
                                    if (replace.length > 2) {
                                        for (int n = 2; n < replace.length; n++) {
                                            Object objStr = mapField.get(replace[n]).get(obj);
                                            String valueStr = objStr == null ? "" : objStr.toString();
                                            more.add(valueStr);
                                        }
                                    }
                                    if ("NUMBER".equals(head.getStyleFormat())) {
                                        double numberValue = replaceNumberValue(replace[0], valueReplace, more,
                                                i);
                                        if (Double.compare(numberValue, -888) == 0) {
                                            cell.setCellValue("*");
                                        } else if (Double.compare(numberValue, -999) == 0) {
                                            cell.setCellValue("-");
                                        } else {
                                            cell.setCellValue(numberValue);
                                        }
                                    } else {
                                        cell.setCellValue(
                                                replaceStringValue(replace[0], valueReplace, more, i));
                                    }
                                } else {
                                    if ("NUMBER".equals(head.getStyleFormat())) {
                                        if (!DataUtil.isNullOrEmpty(value)) {
                                            cell.setCellValue(Double.valueOf(value));
                                        } else {
                                            cell.setCellValue(value == null ? "" : value);
                                        }
                                    } else {
                                        if (value == null) {
                                            cell.setCellValue("");
                                        } else if (value.length() > 32767) {
                                            cell.setCellValue(value.substring(0, 32766));
                                        } else {
                                            cell.setCellValue(value);//cut cho nay
                                        }
                                    }
                                }

                                if ("CENTER".equals(align)) {
//                                    cell.setCellStyle(cellStyleCenter);
                                }
                                if ("LEFT".equals(align)) {
//                                    cell.setCellStyle(cellStyleLeft);
                                }
                                if ("RIGHT".equals(align)) {
//                                    cell.setCellStyle(cellStyleRight);
                                }

                                j++;
                            }
                        }
                        if (item.getCustomColumnWidth() != null && item.getCustomColumnWidth().length > 0) {
                            int b = 1;
                            int size = 0;
                            if (item.getCustomColumnWidth().length % 2 == 0) {
                                size = item.getCustomColumnWidth().length / 2;
                            } else {
                                size = (item.getCustomColumnWidth().length / 2) + 1;
                            }
                            for (int a = 1; a < size; a++) {
                                CellRangeAddress cellRangeAddress = new CellRangeAddress(row.getRowNum(),
                                        row.getRowNum(), b, b + 1);
                                if (b == 1) {
                                    b = a + 2;
                                } else {
                                    b += 2;
                                }
                                sheet.addMergedRegion(cellRangeAddress);
                                RegionUtil
                                        .setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
                                RegionUtil
                                        .setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
                                RegionUtil
                                        .setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
                                RegionUtil
                                        .setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
                            }
                        }
                    }
                }
                // </editor-fold>

                // <editor-fold defaultstate="collapsed" desc="Merge row">

                // </editor-fold>

                // <editor-fold defaultstate="collapsed" desc="Auto size column">

                // </editor-fold>
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="remove index sheet">
            if (exportChart == null || exportChart.length == 0) {
                workbook.removeSheetAt(0);
            }
            // </editor-fold>

            try {
                FileOutputStream fileOut = new FileOutputStream(pathOut);
                workbook.write(fileOut);
                fileOut.flush();
                fileOut.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
        } finally {
            if (hssfWorkbook != null) {
                try {
                    hssfWorkbook.close();
                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                }
            }
            if (workbookTemp != null) {
                try {
                    workbookTemp.close();
                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                }
            }
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                }
            }
            if (fileTemplate != null) {
                try {
                    fileTemplate.close();
                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                }
            }
        }
        return new File(pathOut);
    }

    public static double replaceNumberValue(String modul, Object valueReplace, List<String> more, int index) {
        double valueReturn = 0;
        return valueReturn;
    }

    public static String replaceStringValue(String modul, Object valueReplace, List<String> more, int index) {
        String valueReturn = "";
        return valueReturn;
    }


    /**
     * Thêm comment cho header
     *
     * @param workbook
     * @param headerCell
     * @param col        vị trí cột cần set comment
     * @param content    nội dung comment
     */
    public static Comment setCommentHeader(XSSFWorkbook workbook, Cell headerCell, int col,
                                           String content) {
        Drawing drawing = headerCell.getSheet().createDrawingPatriarch();
        CreationHelper factory = workbook.getCreationHelper();
        ClientAnchor anchor = factory.createClientAnchor();
        anchor.setCol1(col + 1);
        anchor.setCol2(col + 2);
        anchor.setRow1(3);
        anchor.setRow2(4);
        Comment comment = drawing.createCellComment(anchor);
        comment.setString(factory.createRichTextString(content));
        return comment;
    }

    public static List<ConfigHeaderExport> readerHeaderSheet(String... col) {
        List<ConfigHeaderExport> configHeaderExports = new ArrayList<>();
        for (int i = 0; i < col.length; i++) {
            configHeaderExports.add((new ConfigHeaderExport(col[i]
                    , "LEFT"
                    , false
                    , 0
                    , 0
                    , new String[]{}
                    , new String[]{}
                    , "STRING")));
        }
        return configHeaderExports;
    }

    /**
     * Kiểm tra định dạng header của file template
     *
     * @param headerList     List giá trị header lấy từ file
     * @param totalColumn    int tổng số cột
     * @param headerNameList List giá trị header cần đối chiếu
     **/
    public static boolean validFileFormat(List<Object[]> headerList, int totalColumn, List<String> headerNameList) {
        Object[] objects = headerList.get(0);
        if (objects == null) {
            return false;
        }
        int count = 0;
        for (Object data : objects) {
            if (data != null) {
                count += 1;
            }
        }
        if (count != totalColumn) {
            return false;
        }
        if (objects[0] == null) {
            return false;
        }
        for (int i = 0; i < headerNameList.size(); i++) {
            if (!headerNameList.get(i).equalsIgnoreCase(objects[i].toString().trim())) {
                return false;
            }
        }
        return true;
    }
}
