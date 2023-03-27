package com.example.emp.business;

import com.example.common.*;
import com.example.common.config.CellConfigExport;
import com.example.common.config.ConfigFileExport;
import com.example.common.config.ConfigHeaderExport;
import com.example.common.dto.Datatable;
import com.example.common.dto.ResultInsideDTO;
import com.example.common.utils.DataUtil;
import com.example.common.utils.FileUtil;
import com.example.emp.data.dto.EmployeeDTO;
import com.example.emp.exceptions.EmployeeNotFoundException;
import com.example.emp.exceptions.FileInvalidException;
import com.example.emp.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.GenericValidator;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

@Slf4j
@Service
public class EmployeeBusinessImpl implements EmployeeBusiness {
    @Autowired
    private EmployeeRepository employeeRepository;
    final static String tempFolder = FileUtil.getConfigProperties("tempFolder");

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
    public ResultInsideDTO updateEmployee(EmployeeDTO employeeDTO) throws EmployeeNotFoundException {
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
    public File exportDataByTemplate(EmployeeDTO employeeDTO) throws Exception {
        String pathTemplate = "template" + File.separator + "emp_template.xlsx";
        String pathOut = tempFolder + File.separator;
        File folderOut = new File(pathOut);
        if (!folderOut.exists()) {
            folderOut.mkdir();
        }
        XSSFWorkbook workbook = null;
        InputStream fileTemplate = null;
        ExportExcel exportExcel = new ExportExcel();
        int beginRow = 7;
        int stt = 1;
        try {
            log.info("Start get template file!");
            pathTemplate = DataUtil.replaceSeparator(pathTemplate);
            Resource resource = new ClassPathResource(pathTemplate);
            fileTemplate = resource.getInputStream();
            workbook = new XSSFWorkbook(fileTemplate);
            log.info("End get template file!");
            XSSFSheet sheetOne = workbook.getSheetAt(0);
            // <editor-fold desc="fill Data">
            List<EmployeeDTO> employeeDTOList = employeeRepository.getListDataExport(employeeDTO);
            if (!DataUtil.isNullOrEmpty(employeeDTOList) && !employeeDTOList.isEmpty()) {
                for (EmployeeDTO item : employeeDTOList) {
                    exportExcel.createCell(sheetOne, 0, beginRow, String.valueOf(stt), null);
                    exportExcel.createCell(sheetOne, 1, beginRow, item.getCode(), null);
                    exportExcel.createCell(sheetOne, 2, beginRow, item.getUsername(), null);
                    exportExcel.createCell(sheetOne, 3, beginRow, item.getFullName(), null);
                    exportExcel.createCell(sheetOne, 4, beginRow, item.getEmail(), null);
                    exportExcel.createCell(sheetOne, 5, beginRow, DataUtil.convertDateToString(item.getBirthday()), null);
                    exportExcel.createCell(sheetOne, 6, beginRow, item.getGenderStr(), null);
                    exportExcel.createCell(sheetOne, 7, beginRow, item.getAddress(), null);
                    beginRow++;
                    stt++;
                }
            }
            // </editor-fold>
            try {
                String fileNameOut = "EMPLOYEE_EXPORT";
                pathOut = pathOut + fileNameOut + DataUtil.getCurrentTime() + ".xlsx";
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

    @Override
    public File getTemplate() throws Exception {
        ExportExcel exportExcel = new ExportExcel();
        String templatePathOut = "template" + File.separator + "TEMPLATE_EXPORT.xlsx";
//      templatePathOut = StringUtils.removeSeparator(templatePathOut);
        Resource resource = new ClassPathResource(templatePathOut);
        InputStream fileTemplate = resource.getInputStream();

        //apache POI XSSF
        XSSFWorkbook workbook = new XSSFWorkbook(fileTemplate);
        XSSFSheet sheetOne = workbook.getSheetAt(0);
        XSSFSheet sheetParam = workbook.createSheet("param");
        //Tạo 1 mảng lưu header từng cột
        String[] header = new String[]{
                I18n.getLanguage("language.common.stt"),
                I18n.getLanguage("language.employee.code"),
                I18n.getLanguage("language.employee.username"),
                I18n.getLanguage("language.employee.fullName"),
                I18n.getLanguage("language.employee.email"),
                I18n.getLanguage("language.employee.birthday"),
                I18n.getLanguage("language.employee.genderStr"),
                I18n.getLanguage("language.employee.address")
        };
        //Tiêu đề đánh dấu *
        String[] headerStar = new String[]{
                I18n.getLanguage("language.employee.code"),
                I18n.getLanguage("language.employee.username"),
                I18n.getLanguage("language.employee.fullName"),
                I18n.getLanguage("language.employee.email")
        };
        XSSFDataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheetOne);
        List<String> listHeader = Arrays.asList(header);
        List<String> listHeaderStar = Arrays.asList(headerStar);

        //lấy styles
//            Map<String, CellStyle> style = CommonExport.createStyles(workbook);

        //Tạo tiêu đề
        sheetOne.addMergedRegion(new CellRangeAddress(2, 2, 0, listHeader.size() - 1));
        Row titleRow = sheetOne.createRow(2);
        titleRow.setHeightInPoints(25);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(I18n.getLanguage("language.employee.title"));
//      titleCell.setCellStyle(style.get("title"));

        //Tạo font và set màu cho header
        XSSFFont starFont = workbook.createFont();
        starFont.setColor(IndexedColors.RED.getIndex());

        //Tạo Header
        Row headerRow = sheetOne.createRow(4);
        headerRow.setHeightInPoints(30);
        for (int i = 0; i < listHeader.size(); i++) {
            Cell headerCell = headerRow.createCell(i);
            XSSFRichTextString richTextString = new XSSFRichTextString(listHeader.get(i));
            for (String headerCheck : listHeaderStar) {
                if (headerCheck.equalsIgnoreCase(listHeader.get(i))) {
                    richTextString.append("*", starFont);
                }
            }
            if (i == 1) {
                headerCell.setCellComment(CommonExport.setCommentHeader(workbook, headerCell, i, "Nhập MaNV"));
            }
            headerCell.setCellValue(richTextString);
//          headerCell.setCellStyle(style.get("header"));
            sheetOne.setColumnWidth(i, 7000);
        }

        int row = 5;
        int genderColumn = listHeader.indexOf(I18n.getLanguage("language.employee.genderStr"));
        exportExcel.createCell(sheetParam, 6, row++, I18n.getLanguage("language.employee.gender.1")
                , null);
        exportExcel.createCell(sheetParam, 6, row++, I18n.getLanguage("language.employee.gender.0")
                , null);
        sheetParam.autoSizeColumn(1);
        Name genderName = workbook.createName();
        genderName.setNameName("genderStr");
        genderName.setRefersToFormula("param!$G$2:$G$" + row);
        XSSFDataValidationConstraint actionNameConstraint = new XSSFDataValidationConstraint(
                DataValidationConstraint.ValidationType.LIST, "genderStr");
        CellRangeAddressList actionNameCreate = new CellRangeAddressList(5, 65000, genderColumn,
                genderColumn);
        XSSFDataValidation dataValidationActionName = (XSSFDataValidation) dataValidationHelper
                .createValidation(
                        actionNameConstraint, actionNameCreate);
        dataValidationActionName.setShowErrorBox(true);
        sheetOne.addValidationData(dataValidationActionName);

        //set tên trang excel
        workbook.setSheetName(0, I18n.getLanguage("language.employee.title"));
        workbook.setSheetHidden(1, true);
        sheetParam.setSelected(false);

        //set tên file excel
        String fileResult = tempFolder + File.separator;
        String fileName = "EMPLOYEE" + "_" + System.currentTimeMillis() + ".xlsx";
        exportExcel.saveFileToExcel(workbook, fileResult, fileName);
        File fileExport = new File(fileResult + fileName);
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
            headerExportList = CommonExport.readerHeaderSheet("code"
                    , "username"
                    , "fullName"
                    , "email"
                    , "birthdayStr"
                    , "genderStr"
                    , "address"
                    , "resultImport");
            fileNameOut = "EMPLOYEE_RESULT_IMPORT";
        } else {
            headerExportList = CommonExport.readerHeaderSheet("code"
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
        String rootPath = tempFolder + File.separator;
        File fileExport = CommonExport.exportExcel(
                fileTemplate,
                fileNameOut,
                fileExportList,
                rootPath,
                new String[]{}
        );
        return fileExport;
    }

    @Override
    public ResultInsideDTO importData(MultipartFile multipartFile) {
        List<EmployeeDTO> employeeDTOList = new ArrayList<>();
        ResultInsideDTO resultInSideDto = new ResultInsideDTO(new ResultInsideDTO.Status(HttpStatus.OK.value(), Constants.ResponseKey.SUCCESS));
        try {
            //Kiểm tra file đầu vào
            if (multipartFile == null || multipartFile.isEmpty()) {
                throw new FileInvalidException(Constants.ResponseKey.FILE_IS_NULL);
            } else {
                String filePath = FileUtil
                        .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                                tempFolder);
                File fileImport = new File(filePath);
                List<Object[]> headerListInFile;
                headerListInFile = CommonImport.getDataFromExcelFile(
                        fileImport,
                        0, //vị trí sheet cần đọc
                        4, //vị trí dòng header
                        0, //vị trí cột từ
                        7, //vị trí cột đến
                        1000 //giới hạn 1000 dòng
                );
                String[] headerNameList = new String[]{
                        I18n.getLanguage("language.common.stt"),
                        I18n.getLanguage("language.employee.code") + "*",
                        I18n.getLanguage("language.employee.username") + "*",
                        I18n.getLanguage("language.employee.fullName") + "*",
                        I18n.getLanguage("language.employee.email") + "*",
                        I18n.getLanguage("language.employee.birthday"),
                        I18n.getLanguage("language.employee.genderStr"),
                        I18n.getLanguage("language.employee.address")
                };
                //Kiểm tra form header có đúng chuẩn
                if (headerListInFile.isEmpty() || !CommonExport.validFileFormat(headerListInFile, headerNameList.length, Arrays.asList(headerNameList))) {
                    throw new FileInvalidException(Constants.ResponseKey.FILE_INVALID_FORMAT);
                }
                //Lấy dữ liệu import
                List<Object[]> dataImportList = CommonImport.getDataFromExcelFile(
                        fileImport,
                        0,
                        5,
                        0,
                        7,
                        1000
                );
                if (dataImportList.size() > 1500) {
                    throw new FileInvalidException(Constants.ResponseKey.DATA_OVER);
                }
                if (!dataImportList.isEmpty()) {
                    int row = 4;
                    int index = 0;
                    for (Object[] obj : dataImportList) {
                        // <editor-fold desc = "lấy dữ liệu từ file chuyển vào thuộc tính đối tượng">
                        EmployeeDTO employeeDTO = new EmployeeDTO();
                        if (obj[1] != null) {
                            employeeDTO.setCode(obj[1].toString().trim());
                        } else {
                            employeeDTO.setCode(null);
                        }
                        if (obj[2] != null) {
                            employeeDTO.setUsername(obj[2].toString().trim());
                        } else {
                            employeeDTO.setUsername(null);
                        }
                        if (obj[3] != null) {
                            employeeDTO.setFullName(obj[3].toString().trim());
                        } else {
                            employeeDTO.setFullName(null);
                        }
                        if (obj[4] != null) {
                            employeeDTO.setEmail(obj[4].toString().trim().toUpperCase());
                        } else {
                            employeeDTO.setEmail(null);
                        }
                        if (obj[5] != null) {
                            employeeDTO.setBirthdayStr(obj[5].toString().trim());
                            if (GenericValidator.isDate(obj[5].toString().trim(), I18n.getLocale())) {
                                employeeDTO.setBirthday(DataUtil.convertStringToDateddMMyyy(obj[5].toString().trim()));
                            } else {
                                employeeDTO.setBirthday(null);
                            }
                        } else {
                            employeeDTO.setBirthdayStr(null);
                        }
                        if (obj[6] != null) {
                            employeeDTO.setGenderStr(obj[6].toString().trim());
                            if (employeeDTO.getGenderStr().equals(I18n.getLanguage("language.employee.gender.0"))) {
                                employeeDTO.setGender(0L);
                            } else if (employeeDTO.getGenderStr().equals(I18n.getLanguage("language.employee.gender.1"))) {
                                employeeDTO.setGender(1L);
                            } else {
                                employeeDTO.setGender(null);
                            }
                        } else {
                            employeeDTO.setGenderStr(null);
                        }
                        if (obj[7] != null) {
                            employeeDTO.setAddress(obj[7].toString().trim());
                        } else {
                            employeeDTO.setAddress(null);
                        }
                        // </editor-fold >
                        EmployeeDTO employeeDTOTmp = validateImportInfo(employeeDTO, employeeDTOList);
                        if (DataUtil.isNullOrEmpty(employeeDTOTmp.getResultImport())) {
                            employeeDTO
                                    .setResultImport(I18n.getLanguage("validation.common.result.import"));
                            employeeDTOList.add(employeeDTOTmp);
                        } else {
                            employeeDTOList.add(employeeDTOTmp);
                            index++;
                        }
                        row++;
                    }
                    if (index == 0) {
                        if (!employeeDTOList.isEmpty()) {
                            //Hàm thêm mới
                            resultInSideDto = employeeRepository.insertEmployeeList(employeeDTOList);
                        }
                    } else {
                        File fileExport = exportTemplate(employeeDTOList,
                                Constants.RESULT_IMPORT);
                        resultInSideDto.setStatus(new ResultInsideDTO.Status(HttpStatus.OK.value(), Constants.ResponseKey.ERROR));
                        resultInSideDto.setFile(fileExport);
                    }
                } else {
                    resultInSideDto.setStatus(new ResultInsideDTO.Status(HttpStatus.OK.value(), Constants.ResponseKey.NO_DATA));
                    resultInSideDto.setMessage(Constants.ResponseKey.FILE_IS_NULL);
                    File fileExport = exportTemplate(employeeDTOList,
                            Constants.RESULT_IMPORT);
                    resultInSideDto.setFile(fileExport);
                    return resultInSideDto;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultInSideDto.setStatus(new ResultInsideDTO.Status(HttpStatus.OK.value(), Constants.ResponseKey.ERROR));
            resultInSideDto.setMessage(e.getMessage());
        }
        return resultInSideDto;
    }

    private EmployeeDTO validateImportInfo(EmployeeDTO employeeDTO, List<EmployeeDTO> list) {
        if (DataUtil.isNullOrEmpty(employeeDTO.getCode())) {
            employeeDTO.setResultImport(I18n.getLanguage("validation.employee.err.code"));
            return employeeDTO;
        }
        if (DataUtil.isNullOrEmpty(employeeDTO.getUsername())) {
            employeeDTO.setResultImport(I18n.getLanguage("validation.employee.err.username"));
            return employeeDTO;
        }
        if (DataUtil.isNullOrEmpty(employeeDTO.getFullName())) {
            employeeDTO.setResultImport(I18n.getLanguage("validation.employee.err.fullName"));
            return employeeDTO;
        }
        if (DataUtil.isNullOrEmpty(employeeDTO.getEmail())) {
            employeeDTO.setResultImport(I18n.getLanguage("validation.employee.err.email"));
            return employeeDTO;
        }
        if (DataUtil.isNullOrEmpty(employeeDTO.getBirthday())) {
            employeeDTO.setResultImport(I18n.getLanguage("validation.employee.err.birthday"));
            return employeeDTO;
        }
        if (DataUtil.isNullOrEmpty(employeeDTO.getGender())) {
            employeeDTO.setResultImport(I18n.getLanguage("validation.employee.err.gender"));
            return employeeDTO;
        }
//        if (DataUtil.isNullOrEmpty(employeeDTO.getAddress())) {
//            employeeDTO.setResultImport(I18n.getLanguage("employee.err.address"));
//            return employeeDTO;
//        }
        if (!DataUtil.isNullOrEmpty(list) && !list.isEmpty() && DataUtil.isNullOrEmpty(employeeDTO.getResultImport())) {
            employeeDTO = validateDuplicate(list, employeeDTO);
        }
        return employeeDTO;
    }

    private EmployeeDTO validateDuplicate(List<EmployeeDTO> list, EmployeeDTO employeeDTO) {
        for (int i = 0; i < list.size(); i++) {
            EmployeeDTO employeeDTOTmp = list.get(i);
            if (I18n.getLanguage("validation.common.result.import").equals(employeeDTOTmp.getResultImport())
                    && employeeDTOTmp.getCode().equalsIgnoreCase(employeeDTO.getCode())
                    && employeeDTOTmp.getUsername().equalsIgnoreCase(employeeDTO.getUsername())
                    && employeeDTOTmp.getEmail().equalsIgnoreCase(employeeDTO.getEmail())) {
                employeeDTO.setResultImport(I18n.getLanguage("validation.common.err.dup-code-in-file")
                        .replaceAll("0", String.valueOf((i) + 1)));
                break;
            }
        }
        return employeeDTO;
    }

    @ExceptionHandler(FileInvalidException.class)
    public ResponseEntity<ResultInsideDTO> handleFileInvalidException(FileInvalidException fileInvalidException) {
        ResultInsideDTO resultInsideDTO = new ResultInsideDTO(
                new ResultInsideDTO.Status(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name()),
                fileInvalidException.getMessage());
        return new ResponseEntity<>(resultInsideDTO, HttpStatus.BAD_REQUEST);
    }
}
