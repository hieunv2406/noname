package com.example.common.controller;

import com.example.business.EmployeeBusiness;
import com.example.common.service.FilesStorageService;
import com.example.data.dto.EmployeeDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@Log4j2
@RestController
@RequestMapping(value = "/commonExport")
public class ExportController {
    @Autowired
    FilesStorageService filesStorageService;
    @Autowired
    private ServletContext servletContext;
    @Autowired
    private EmployeeBusiness employeeBusiness;

//    @GetMapping("/files/{key}")
//    public ResponseEntity<Resource> getFile(@PathVariable("key") String key) throws Exception {
//        switch (key){
//            case "EMPLOYEE_MANAGER" :
//                EmployeeDTO employeeDTO = new EmployeeDTO();
//                file =  employeeBusiness.exportData(employeeDTO);
//                break;
//            default:
//        }
//        Path path = filesStorageService.load(file.get)
//        byte[] data = Files.readAllBytes(path);
//        ByteArrayResource resource = new ByteArrayResource(data);
//        MediaType mediaType = getMediaTypeForFileName(servletContext, fileName);
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
//                .contentType(mediaType)
//                .body(resource);
//    }

    private MediaType getMediaTypeForFileName(ServletContext servletContext, String fileName) {
        String mineType = servletContext.getMimeType(fileName);
        try {
            MediaType mediaType = MediaType.parseMediaType(mineType);
            return mediaType;
        } catch (Exception e) {
            log.info(e.getMessage());
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}