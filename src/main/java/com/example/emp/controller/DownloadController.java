package com.example.emp.controller;

import com.example.common.utils.FileUtil;
import com.example.emp.business.EmployeeBusiness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@Slf4j
@RestController
@RequestMapping(value = "/mapi/empDownload")
public class DownloadController {
    @Autowired
    private EmployeeBusiness employeeBusiness;

    @GetMapping("/onDownloadFile")
    public ResponseEntity<Resource> onDownloadFile(String moduleKey) throws Exception {
        File file = null;
        switch (moduleKey) {
            case "EMPLOYEE_MANAGER":
                file = employeeBusiness.getTemplate();
                break;
            default:
        }
        if (file != null) {
            return FileUtil.responseFormFile(file);
        }
        return null;
    }
}