package com.example.emp.controller;

import com.example.common.dto.ResultInsideDTO;
import com.example.emp.business.EmployeeBusiness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping(value = "/mapi/empImport")
public class ImportController {
    @Autowired
    private EmployeeBusiness employeeBusiness;

    @PostMapping("/onImportFile")
    public ResponseEntity<ResultInsideDTO> onImportFile(MultipartFile multipartFile, String moduleKey) throws Exception {
        ResultInsideDTO resultInsideDTO = null;
        switch (moduleKey) {
            case "EMPLOYEE_MANAGER":
                resultInsideDTO = employeeBusiness.importData(multipartFile);
                break;
            default:
        }
        return new ResponseEntity<>(resultInsideDTO, HttpStatus.OK);
    }
}