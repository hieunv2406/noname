package com.example.emp.controller;

import com.example.common.utils.FileUtil;
import com.example.emp.data.dto.EmployeeDTO;
import com.example.emp.business.EmployeeBusiness;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@Slf4j
@RestController
@RequestMapping(value = "/mapi/empExport")
public class ExportController {
    @Autowired
    private EmployeeBusiness employeeBusiness;

    @PostMapping("/onExportFile")
    public ResponseEntity<Resource> onExportFile(String stringFormJson, String moduleKey) throws Exception {
        File file = null;
        ObjectMapper objectMapper = new ObjectMapper();
        switch (moduleKey) {
            case "EMPLOYEE_MANAGER":
                EmployeeDTO employeeDTO = objectMapper.readValue(stringFormJson, EmployeeDTO.class);
                file = employeeBusiness.exportData(employeeDTO);
                break;
            case "EMPLOYEE_MANAGER_01":
                EmployeeDTO employeeDTO1 = objectMapper.readValue(stringFormJson, EmployeeDTO.class);
                file = employeeBusiness.exportDataByTemplate(employeeDTO1);
                break;
            default:
        }
        if (file != null) {
            return FileUtil.responseFormFile(file);
        }
        return null;
    }
}