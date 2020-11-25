package com.example.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
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
            SXSSFWorkbook workbook = new SXSSFWorkbook(workbookTemp,1000);
            hssfWorkbook = new HSSFWorkbook();

            // <editor-fold defaultstate="collapsed" desc="Declare style">

            // </editor-fold>

            for (ConfigFileExport item: config){
                Map<String, String> fieldSpit = item.getFieldSplit();
                SXSSFSheet sheet;
                if (exportChart != null && exportChart.length > 0){
                    sheet = workbook.getSheetAt(0);
                }else {
                    sheet = workbook.createSheet(item.getSheetName());
                }
                //title
                Row rowMainTitle = sheet.createRow(item.getCellTitleIndex());
                Cell cellMainTitle;
                if (item.getCustomTitle() != null && item.getCustomTitle().length>0){

                }
            }
        } catch (Exception e) {

        }
        return null;
    }
}
