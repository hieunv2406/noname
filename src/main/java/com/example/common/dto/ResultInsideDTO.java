package com.example.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultInsideDTO {
    private Status status;
    private String message;
    private Object data;
    private File file;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Status{
        private int code;
        private String description;
    }

    public ResultInsideDTO(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public ResultInsideDTO(Status status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ResultInsideDTO(Status status) {
        this.status = status;
    }
}