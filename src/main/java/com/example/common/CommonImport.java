package com.example.common;

import com.example.common.utils.DataUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author owl
 * @CreatedDate 09/07/2021
 * @PACKAGE com.example.common
 */

@Slf4j
public class CommonImport {

    public static List getDataFromExcelFile(
            File file
            , int iSheet
            , int iBeginRow
            , int iFromCol
            , int iToCol
            , int rowBack
    ) throws IOException {
        List result = new ArrayList();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        FileInputStream fileInput = null;
        Workbook workbook = null;
        try {
            fileInput = new FileInputStream(file);
            workbook = WorkbookFactory.create(fileInput);
            Sheet worksheet = workbook.getSheetAt(iSheet);

            int irowBack = 0;

            for (int i = iBeginRow; i <= worksheet.getLastRowNum(); i++) {
                Object[] obj = new Object[iToCol - iFromCol + 1];
                Row row = worksheet.getRow(i);

                if (row != null) {
                    int iCount = 0;
                    int check = 0;
                    for (int j = iFromCol; j <= iToCol; j++) {
                        Cell cell = row.getCell(j);
                        if (cell != null) {
                            CellType cellType = cell.getCellType();
                            switch (cellType) {
                                case STRING:
                                    obj[iCount] = cell.getStringCellValue().trim();
                                    break;
                                case NUMERIC:
                                    Double doubleValue = (Double) cell.getNumericCellValue();
                                    if (DateUtil.isCellDateFormatted(cell)) {
                                        Date date = DateUtil.getJavaDate(doubleValue);
                                        obj[iCount] = simpleDateFormat.format(date);
                                        break;
                                    }
                                    List<String> lstValue = DataUtil.splitDot(String.valueOf(doubleValue));
                                    String value = lstValue.get(1);
                                    if (lstValue.get(1).matches("[0]+")) {
                                        obj[iCount] = lstValue.get(0);
                                    } else if (value.contains("E")) {
                                        List<String> lstValueTemp = DataUtil.splitCharE(value);
                                        if (lstValueTemp.get(0).length() > Integer.valueOf(lstValueTemp.get(1))) {
                                            value =
                                                    lstValueTemp.get(0).substring(0, Integer.valueOf(lstValueTemp.get(1)))
                                                            + "." + lstValueTemp.get(0)
                                                            .substring(Integer.valueOf(lstValueTemp.get(1)),
                                                                    lstValueTemp.get(0).length());
                                            obj[iCount] = lstValue.get(0) + value;
                                        } else if (lstValueTemp.get(0).length() == Integer
                                                .valueOf(lstValueTemp.get(1))) {
                                            value = lstValueTemp.get(0);
                                            obj[iCount] = lstValue.get(0) + value;
                                        } else if (lstValueTemp.get(0).length() < Integer
                                                .valueOf(lstValueTemp.get(1))) {
                                            int temp =
                                                    Integer.valueOf(lstValueTemp.get(1)) - lstValueTemp.get(0).length();
                                            value = lstValueTemp.get(0);
                                            for (int t = 0; t < temp; t++) {
                                                value = value + "0";
                                            }
                                            obj[iCount] = lstValue.get(0) + value;
                                        }
                                    } else {
                                        obj[iCount] = lstValue.get(0) + "." + value;
                                    }
                                    break;
                                case BLANK:
                                    check++;
                                    break;
                                case FORMULA:
                                    obj[iCount] = cell.getCellFormula().trim();
                                    break;
                                default:
                                    obj[iCount] = cell.getStringCellValue().trim();
                                    break;
                            }
                        } else {
                            obj[iCount] = null;
                        }
                        iCount += 1;
                    }
                    if (check != (iToCol - iFromCol + 1)) {
                        boolean isOK = false;
                        for (int k = 0; k < obj.length; k++) {
                            if (!DataUtil.isNullOrEmpty(obj[k])) {
                                isOK = true;
                            }
                        }
                        if (isOK) {
                            result.add(obj);
                        }
                    }
                } else {
                    irowBack += 1;
                }
                if (irowBack == rowBack) {
                    break;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result = null;
        } finally {
            if (workbook != null) {
                workbook.close();
            }
            if (fileInput != null) {
                fileInput.close();
            }
        }
        return result;
    }
}
