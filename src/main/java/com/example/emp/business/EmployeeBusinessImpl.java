package com.example.emp.business;

import com.example.common.CommonExport;
import com.example.common.I18n;
import com.example.common.config.CellConfigExport;
import com.example.common.config.ConfigFileExport;
import com.example.common.config.ConfigHeaderExport;
import com.example.common.dto.Datatable;
import com.example.common.dto.ResultInsideDTO;
import com.example.emp.data.dto.EmployeeDTO;
import com.example.emp.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.util.*;

@Slf4j
@Service
public class EmployeeBusinessImpl implements EmployeeBusiness {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public EmployeeDTO findEmployeeById(Long employeeId) {
        log.info("findEmployeeById", employeeId);
        return employeeRepository.findEmployeeById(employeeId);
    }

    @Override
    public ResultInsideDTO insertEmployee(EmployeeDTO employeeDTO) {
        log.info("insertEmployee", employeeDTO);
        return employeeRepository.insertEmployee(employeeDTO);
    }

    @Override
    public ResultInsideDTO updateEmployee(EmployeeDTO employeeDTO) {
        log.info("updateEmployee", employeeDTO);
        return employeeRepository.updateEmployee(employeeDTO);
    }

    @Override
    public ResultInsideDTO deleteEmployeeById(Long employeeId) {
        log.info("deleteEmployeeById", employeeId);
        return employeeRepository.deleteEmployeeById(employeeId);
    }

    @Override
    public Datatable getListEmployeeDTO(EmployeeDTO employeeDTO) {
        log.info("getListEmployeeDTO", employeeDTO);
        return employeeRepository.getListEmployeeDTO(employeeDTO);
    }

    @Override
    public List<Map<String, Object>> getListEmployeeMap() {
        log.info("getListEmployeeMap");
        return employeeRepository.getListEmployeeMap();
    }

    @Override
    public List<EmployeeDTO> getListDataExport(EmployeeDTO employeeDTO) {
        log.info("getListDataExport", employeeDTO);
        return employeeRepository.getListDataExport(employeeDTO);
    }

    @Override
    public File exportData(EmployeeDTO employeeDTO) throws Exception {
        List<EmployeeDTO> employeeDTOList = employeeRepository.getListDataExport(employeeDTO);
        return exportTemplate(employeeDTOList, "EXPORT");
    }

    @Override
    public File getTemplate() throws Exception {
            ExcelWriterUtils excelWriterUtils = new ExcelWriterUtils();
            String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
            templatePathOut = StringUtils.removeSeparator(templatePathOut);
            Resource resource = new ClassPathResource(templatePathOut);
            InputStream fileTemplate = resource.getInputStream();

            //apache POI XSSF
            XSSFWorkbook workbook = new XSSFWorkbook(fileTemplate);
            XSSFSheet sheetOne = workbook.getSheetAt(0);
            XSSFSheet sheetParam = workbook.createSheet("param");
            XSSFSheet sheetParam2 = workbook.createSheet("param2");
            XSSFSheet sheetParam3 = workbook.createSheet("param3");
            //Tạo 1 mảng lưu header từng cột
            String[] header = new String[]{
                    I18n.getLanguage("common.STT"),
                    I18n.getLanguage("mrDevice.deviceIdStr"),
                    I18n.getLanguage("mrDevice.marketName"),
                    I18n.getLanguage("mrDevice.regionHard"),
                    I18n.getLanguage("mrDevice.arrayCodeStr"),
                    I18n.getLanguage("mrDevice.networkTypeStr"),
                    I18n.getLanguage("mrDevice.deviceTypeStr"),
                    I18n.getLanguage("mrDevice.nodeIp"),
                    I18n.getLanguage("mrDevice.nodeCode"),
                    I18n.getLanguage("mrDevice.deviceName"),
                    I18n.getLanguage("mrDevice.vendor"),
                    I18n.getLanguage("mrDevice.statusStr"),
                    I18n.getLanguage("mrDevice.dateIntegrated"),
                    I18n.getLanguage("mrDevice.stationCode"),
                    I18n.getLanguage("mrDevice.userMrHard"),
                    I18n.getLanguage("mrDevice.mrConfirmHardStr"),
                    I18n.getLanguage("mrDevice.mrHardStr"),
                    I18n.getLanguage("mrDevice.boUnitHard"),
                    I18n.getLanguage("mrDevice.mrTypeStr")
            };
            //Tiêu đề đánh dấu *
            String[] headerStar = new String[]{
                    I18n.getLanguage("mrDevice.marketName"),
                    I18n.getLanguage("mrDevice.arrayCodeStr"),
                    I18n.getLanguage("mrDevice.networkTypeStr"),
                    I18n.getLanguage("mrDevice.deviceTypeStr"),
                    I18n.getLanguage("mrDevice.nodeCode"),
                    I18n.getLanguage("mrDevice.deviceName"),
                    I18n.getLanguage("mrDevice.vendor"),
                    I18n.getLanguage("mrDevice.statusStr"),
                    I18n.getLanguage("mrDevice.mrHardStr")
            };

            XSSFDataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheetOne);
            List<String> listHeader = Arrays.asList(header);
            List<String> listHeaderStar = Arrays.asList(headerStar);

            int marketNameColumn = listHeader
                    .indexOf(I18n.getLanguage("mrDevice.marketName"));
            int arrayCodeStrColumn = listHeader.indexOf(I18n.getLanguage("mrDevice.arrayCodeStr"));
            int statusStrColumn = listHeader.indexOf(I18n.getLanguage("mrDevice.statusStr"));
            int mrConfirmHardStrColumn = listHeader.indexOf(I18n.getLanguage("mrDevice.mrConfirmHardStr"));
            int mrHardStrColumn = listHeader.indexOf(I18n.getLanguage("mrDevice.mrHardStr"));
            int mrTypeStrColumn = listHeader.indexOf(I18n.getLanguage("mrDevice.mrTypeStr"));

            Map<String, CellStyle> style = CommonExport.createStyles(workbook);
            //Tạo tiêu đề
            sheetOne.addMergedRegion(new CellRangeAddress(2, 2, 0, listHeader.size() - 1));
            Row titleRow = sheetOne.createRow(2);
            titleRow.setHeightInPoints(25);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue(I18n.getLanguage("mrDevice.title"));
            titleCell.setCellStyle(style.get("title"));

            XSSFFont starFont = workbook.createFont();
            starFont.setColor(IndexedColors.RED.getIndex());

            //Tạo Header

            Row headerRow = sheetOne.createRow(4);
            Row headerArray = sheetParam2.createRow(0);
            Row headerUnit = sheetParam3.createRow(0);
            headerRow.setHeightInPoints(30);
            for (int i = 0; i < listHeader.size(); i++) {
                Cell headerCell = headerRow.createCell(i);
                XSSFRichTextString richTextString = new XSSFRichTextString(listHeader.get(i));
                for (String headerCheck : listHeaderStar) {
                    if (headerCheck.equalsIgnoreCase(listHeader.get(i))) {
                        richTextString.append("*", starFont);
                    }
                }
                if (i == 12) {
                    headerCell.setCellComment(
                            setCommentHeader(workbook, headerCell, i, "Nhập định dạng dd/MM/yyyy HH24:mm:ss"));
                }
                if (i == 17) {
                    headerCell.setCellComment(setCommentHeader(workbook, headerCell, i, "Nhập ID đơn vị"));
                }
                headerCell.setCellValue(richTextString);
                headerCell.setCellStyle(style.get("header"));
                sheetOne.setColumnWidth(i, 7000);
            }

            Cell headerCellUnitId = headerUnit.createCell(0);
            Cell headerCellUnit = headerUnit.createCell(1);
            XSSFRichTextString unitId = new XSSFRichTextString(I18n.getLanguage("mrDevice.unitId"));
            XSSFRichTextString unit = new XSSFRichTextString(I18n.getLanguage("mrDevice.unitName"));
            headerCellUnit.setCellValue(unit);
            headerCellUnit.setCellStyle(style.get("header"));
            headerCellUnitId.setCellValue(unitId);
            headerCellUnitId.setCellStyle(style.get("header"));

            Cell headerCellArray = headerArray.createCell(0);
            Cell headerCellNetwork = headerArray.createCell(1);
            Cell headerCellDevice = headerArray.createCell(2);
            XSSFRichTextString array = new XSSFRichTextString(
                    I18n.getLanguage("mrDevice.arrayCodeStr"));
            XSSFRichTextString network = new XSSFRichTextString(
                    I18n.getLanguage("mrDevice.networkTypeStr"));
            XSSFRichTextString device = new XSSFRichTextString(
                    I18n.getLanguage("mrDevice.deviceType"));
            headerCellArray.setCellValue(array);
            headerCellArray.setCellStyle(style.get("header"));
            headerCellNetwork.setCellValue(network);
            headerCellNetwork.setCellStyle(style.get("header"));
            headerCellDevice.setCellValue(device);
            headerCellDevice.setCellStyle(style.get("header"));
            sheetParam2.setColumnWidth(0, 15000);
            sheetParam2.setColumnWidth(1, 15000);
            sheetParam2.setColumnWidth(2, 15000);
            sheetOne.setColumnWidth(0, 3000);

            // Set dữ liệu vào column dropdown
            int row = 5;
            List<ItemDataCRInside> lstMarketCode = catLocationBusiness
                    .getListLocationByLevelCBB(null, 1L, null);
            for (ItemDataCRInside dto : lstMarketCode) {
                excelWriterUtils
                        .createCell(sheetParam, 2, row++, dto.getDisplayStr(), style.get("cell"));
            }
            sheetParam.autoSizeColumn(1);
            Name marketName = workbook.createName();
            marketName.setNameName("marketName");
            marketName.setRefersToFormula("param!$C$2:$C$" + row);
            XSSFDataValidationConstraint marketNameConstraint = new XSSFDataValidationConstraint(
                    DataValidationConstraint.ValidationType.LIST, "marketName");
            CellRangeAddressList marketNameCreate = new CellRangeAddressList(5, 65000,
                    marketNameColumn,
                    marketNameColumn);
            XSSFDataValidation dataValidationMarketName = (XSSFDataValidation) dataValidationHelper
                    .createValidation(
                            marketNameConstraint, marketNameCreate);
            dataValidationMarketName.setShowErrorBox(true);
            sheetOne.addValidationData(dataValidationMarketName);

            row = 5;
            List<CatItemDTO> lstArrayCode = mrCfgProcedureCDBusiness.getMrSubCategory();
            for (CatItemDTO dto : lstArrayCode) {
                excelWriterUtils
                        .createCell(sheetParam, 4, row++, dto.getItemName(), style.get("cell"));
            }
            sheetParam.autoSizeColumn(1);
            Name arrayCodeStr = workbook.createName();
            arrayCodeStr.setNameName("arrayCodeStr");
            arrayCodeStr.setRefersToFormula("param!$E$2:$E$" + row);
            XSSFDataValidationConstraint arrayCodeStrConstraint = new XSSFDataValidationConstraint(
                    DataValidationConstraint.ValidationType.LIST, "arrayCodeStr");
            CellRangeAddressList arrayCodeStrCreate = new CellRangeAddressList(5, 65000, arrayCodeStrColumn,
                    arrayCodeStrColumn);
            XSSFDataValidation dataValidationArrayCodeStr = (XSSFDataValidation) dataValidationHelper
                    .createValidation(
                            arrayCodeStrConstraint, arrayCodeStrCreate);
            dataValidationArrayCodeStr.setShowErrorBox(true);
            sheetOne.addValidationData(dataValidationArrayCodeStr);

            row = 1;
            List<MrDeviceDTO> lstANT = mrDeviceRepository.getMrDeviceByA_N_T();
            Map<String, String> mapAN = new HashMap<>();
            Map<String, String> mapANT = new HashMap<>();
            Map<String, String> mapArrCodeEN = new HashMap<>();
            for (CatItemDTO dto : lstArrayCode) {
                mapArrCodeEN.put(dto.getItemValue(), dto.getItemName());
            }
            if (lstANT != null && !lstANT.isEmpty()) {
                for (MrDeviceDTO item : lstANT) {
                    if (!mapAN.containsKey(item.getArrayCode())) {
                        if (row > 1) {
                            excelWriterUtils
                                    .createCell(sheetParam2, 0, row, "", style.get("header"));
                            excelWriterUtils
                                    .createCell(sheetParam2, 1, row, "", style.get("header"));
                            excelWriterUtils
                                    .createCell(sheetParam2, 2, row, "", style.get("header"));
                            row++; // them ngan cach
                        }
                        String valueDefault = item.getArrayCode();
                        if (mapArrCodeEN.containsKey(valueDefault)) {
                            valueDefault = mapArrCodeEN.get(valueDefault);
                        }
                        excelWriterUtils
                                .createCell(sheetParam2, 0, row, valueDefault, style.get("cell"));
                    }
                    if (!mapANT.containsKey(item.getArrayCode() + ";" + item.getNetworkType())) {
                        excelWriterUtils
                                .createCell(sheetParam2, 1, row, item.getNetworkType(), style.get("cell"));
                    }
                    excelWriterUtils
                            .createCell(sheetParam2, 2, row++, item.getDeviceType(), style.get("cell"));
                    mapAN.put(item.getArrayCode(), item.getNetworkType());
                    mapANT.put(item.getArrayCode() + ";" + item.getNetworkType(), item.getDeviceType());
                }
            }
    /*
    for (CatItemDTO arrayCode : lstArrayCode) {
      List<MrDeviceDTO> lstNetwork = mrDeviceRepository
          .getListNetworkTypeByArrayCode(arrayCode.getItemValue());
      excelWriterUtils
          .createCell(sheetParam2, 0, row, arrayCode.getItemName(), style.get("cell"));
      for (MrDeviceDTO networkCode : lstNetwork) {
        List<MrDeviceDTO> lstDeviceType = mrDeviceRepository
            .getListDeviceTypeByNetworkType(arrayCode.getItemValue(), networkCode.getNetworkType());
        excelWriterUtils
            .createCell(sheetParam2, 1, row, networkCode.getNetworkType(), style.get("cell"));
        for (MrDeviceDTO deviceType : lstDeviceType) {
          excelWriterUtils
              .createCell(sheetParam2, 2, row++, deviceType.getDeviceType(), style.get("cell"));
        }
        excelWriterUtils
            .createCell(sheetParam2, 0, row++, null, style.get("cell"));
      }
      excelWriterUtils
          .createCell(sheetParam2, 0, row++, null, style.get("cell"));
    }
    */
            sheetParam2.autoSizeColumn(0);
            sheetParam2.autoSizeColumn(1);

            row = 5;
            excelWriterUtils.createCell(sheetParam, 11, row++, I18n.getLanguage("mrDevice.status.0")
                    , style.get("cell"));
            excelWriterUtils.createCell(sheetParam, 11, row++, I18n.getLanguage("mrDevice.status.1")
                    , style.get("cell"));
            sheetParam.autoSizeColumn(1);
            Name statusStr = workbook.createName();
            statusStr.setNameName("statusStr");
            statusStr.setRefersToFormula("param!$L$2:$L$" + row);
            XSSFDataValidationConstraint statusStrConstraint = new XSSFDataValidationConstraint(
                    DataValidationConstraint.ValidationType.LIST, "statusStr");
            CellRangeAddressList statusStrCreate = new CellRangeAddressList(5, 65000, statusStrColumn,
                    statusStrColumn);
            XSSFDataValidation dataValidationStatusStr = (XSSFDataValidation) dataValidationHelper
                    .createValidation(
                            statusStrConstraint, statusStrCreate);
            dataValidationStatusStr.setShowErrorBox(true);
            sheetOne.addValidationData(dataValidationStatusStr);

            row = 5;
            List<MrConfigDTO> lstMrConfirmHardStr = mrScheduleTelRepository.getConfigByGroup("LY_DO_KO_BD");
            for (MrConfigDTO dto : lstMrConfirmHardStr) {
                excelWriterUtils
                        .createCell(sheetParam, 15, row++, dto.getConfigName(), style.get("cell"));
            }
            sheetParam.autoSizeColumn(1);
            Name mrConfirmHardStr = workbook.createName();
            mrConfirmHardStr.setNameName("mrConfirmHardStr");
            mrConfirmHardStr.setRefersToFormula("param!$P$2:$P$" + row);
            XSSFDataValidationConstraint mrConfirmHardStrConstraint = new XSSFDataValidationConstraint(
                    DataValidationConstraint.ValidationType.LIST, "mrConfirmHardStr");
            CellRangeAddressList mrConfirmHardStrCreate = new CellRangeAddressList(5, 65000,
                    mrConfirmHardStrColumn,
                    mrConfirmHardStrColumn);
            XSSFDataValidation dataValidationMrConfirmHardStr = (XSSFDataValidation) dataValidationHelper
                    .createValidation(
                            mrConfirmHardStrConstraint, mrConfirmHardStrCreate);
            dataValidationMrConfirmHardStr.setShowErrorBox(true);
            sheetOne.addValidationData(dataValidationMrConfirmHardStr);

            row = 5;
            excelWriterUtils.createCell(sheetParam, 16, row++, I18n.getLanguage("mrDevice.mrHardStr.0")
                    , style.get("cell"));
            excelWriterUtils.createCell(sheetParam, 16, row++, I18n.getLanguage("mrDevice.mrHardStr.1")
                    , style.get("cell"));
            sheetParam.autoSizeColumn(1);
            Name mrHardStr = workbook.createName();
            mrHardStr.setNameName("mrHardStr");
            mrHardStr.setRefersToFormula("param!$Q$2:$Q$" + row);
            XSSFDataValidationConstraint mrHardStrConstraint = new XSSFDataValidationConstraint(
                    DataValidationConstraint.ValidationType.LIST, "mrHardStr");
            CellRangeAddressList mrHardStrCreate = new CellRangeAddressList(5, 65000, mrHardStrColumn,
                    mrHardStrColumn);
            XSSFDataValidation dataValidationMrHardStr = (XSSFDataValidation) dataValidationHelper
                    .createValidation(
                            mrHardStrConstraint, mrHardStrCreate);
            dataValidationMrHardStr.setShowErrorBox(true);
            sheetOne.addValidationData(dataValidationMrHardStr);

            row = 5;
            excelWriterUtils.createCell(sheetParam, 18, row++, I18n.getLanguage("mrDevice.mrTypeStr.BD")
                    , style.get("cell"));
            sheetParam.autoSizeColumn(1);
            Name mrTypeStr = workbook.createName();
            mrTypeStr.setNameName("mrTypeStr");
            mrTypeStr.setRefersToFormula("param!$S$2:$S$" + row);
            XSSFDataValidationConstraint mrTypeStrConstraint = new XSSFDataValidationConstraint(
                    DataValidationConstraint.ValidationType.LIST, "mrTypeStr");
            CellRangeAddressList mrTypeStrCreate = new CellRangeAddressList(5, 65000, mrTypeStrColumn,
                    mrTypeStrColumn);
            XSSFDataValidation dataValidationMrTypeStr = (XSSFDataValidation) dataValidationHelper
                    .createValidation(
                            mrTypeStrConstraint, mrTypeStrCreate);
            dataValidationMrTypeStr.setShowErrorBox(true);
            sheetOne.addValidationData(dataValidationMrTypeStr);

            row = 1;
            List<UnitDTO> unitNameList = unitRepository.getListUnit(null);
            for (UnitDTO dto : unitNameList) {
                excelWriterUtils
                        .createCell(sheetParam3, 0, row, dto.getUnitId().toString(), style.get("cell"));
                excelWriterUtils.createCell(sheetParam3, 1, row++, dto.getUnitName(), style.get("cell"));
            }
            sheetParam.autoSizeColumn(0);
            sheetParam.autoSizeColumn(1);
            //set tên trang excel
            workbook.setSheetName(0, I18n.getLanguage("mrDevice.title"));
            workbook.setSheetName(2, I18n.getLanguage("mrDevice.arrNet"));
            workbook.setSheetName(3, I18n.getLanguage("mrDevice.boUnitHard"));
            workbook.setSheetHidden(1, true);
            sheetParam.setSelected(false);

            //set tên file excel
            String fileResult = tempFolder + File.separator;
            String fileName = "IMPORT_MR_DEVICE" + "_" + System.currentTimeMillis() + ".xlsx";
            excelWriterUtils.saveToFileExcel(workbook, fileResult, fileName);
            String resultPath = fileResult + fileName;
            File fileExport = new File(resultPath);
            return fileExport;
    }

    private File exportTemplate(List<EmployeeDTO> dtoList, String key) throws Exception {
        String fileNameOut;
        String subTitle = null;
        String sheetName = I18n.getLanguage("language.employee.title");
        String title = I18n.getLanguage("language.employee.title");
        List<ConfigFileExport> fileExportList = new ArrayList<>();
        List<ConfigHeaderExport> headerExportList;
        if ("RESULT_IMPORT".equalsIgnoreCase(key)) {
            headerExportList = readerHeaderSheet("code"
                    , "username"
                    , "fullName"
                    , "email"
                    , "birthday"
                    , "gender"
                    , "address");
            fileNameOut = "EMPLOYEE_RESULT_IMPORT";
        } else {
            headerExportList = readerHeaderSheet("code"
                    , "username"
                    , "fullName"
                    , "email"
                    , "birthday"
                    , "genderStr"
                    , "address");
            fileNameOut = "EMPLOYEE_EXPORT";
            subTitle = String.valueOf(new Date());
        }
        Map<String, String> fieldSplit = new HashMap<>();
        ConfigFileExport configFileExport = new ConfigFileExport(
                dtoList,
                sheetName,
                title,
                subTitle,
                7,
                3,
                9,
                true,
                "language.employee",
                headerExportList,
                fieldSplit,
                "",
                I18n.getLanguage("language.common.firstLeftHeaderTitle"),
                I18n.getLanguage("language.common.secondLeftHeaderTitle"),
                I18n.getLanguage("language.common.firstRightHeaderTitle"),
                I18n.getLanguage("language.common.secondRightHeaderTitle"));
        configFileExport.setLangKey("i18n/vi");
        List<CellConfigExport> lstCellSheet = new ArrayList<>();
        CellConfigExport cellSheet;
        cellSheet = new CellConfigExport(7,
                0,
                0,
                "OK",
                "HEAD",
                "STRING");
        lstCellSheet.add(cellSheet);
        configFileExport.setLstCreateCell(lstCellSheet);
        fileExportList.add(configFileExport);
        //cấu hình đường dẫn
        String fileTemplate = "template" + File.separator + "TEMPLATE_EXPORT.xlsx";
        String rootPath = "tempFolder" + File.separator;
        File fileExport = CommonExport.exportExcel(
                fileTemplate,
                fileNameOut,
                fileExportList,
                rootPath,
                new String[]{}
        );
        return fileExport;
    }

    private List<ConfigHeaderExport> readerHeaderSheet(String... col) {
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

}
