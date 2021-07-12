package com.example.common.utils;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FileUtil {

    public static ResponseEntity<Resource> responseFormFile(File file) throws Exception {
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    public static ResponseEntity<Resource> responseSourceFromFile(File file) throws Exception {
        InputStreamResource inputStreamResource = new InputStreamResource(new FileInputStream(file));
        HttpHeaders headers = new HttpHeaders();
        List<String> customHeaders = new ArrayList<>();
        customHeaders.add("Content-Disposition");
        headers.setAccessControlExposeHeaders(customHeaders);
        headers.setContentDispositionFormData("attachment", file.getName());
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return ResponseEntity.ok()
                .headers(headers)
//        .contentLength(inputStreamResource.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(inputStreamResource);
    }

    public static String saveTempFile(String originalFilename, byte[] bytes, String tempFolder)
            throws IOException {
        Date date = new Date();
        String fileName = createFileName(originalFilename, date);
        File file = new File(tempFolder + File.separator + createPathByDate(date));
        if (!file.exists()) {
            file.mkdirs();
        }
        File fileWrite = new File(file.getPath() + File.separator + fileName);
        try (FileOutputStream fos = new FileOutputStream(fileWrite)) {
            fos.write(bytes);
            fos.close();
        }
        return file.getPath() + File.separator + fileName;
    }

    public static String createFileName(String originalFilename, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY) + ""
                + calendar.get(Calendar.MINUTE) + "" + calendar.get(Calendar.SECOND) + ""
                + calendar.get(Calendar.MILLISECOND) + "_" + originalFilename;
    }

    public static String createPathByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String pathByDate = year
                + File.separator
                + (month < 10 ? "0" + month : month)
                + File.separator
                + (day < 10 ? "0" + day : day);
        return pathByDate;
    }
}