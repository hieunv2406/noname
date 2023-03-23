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
    private Long id;
    private String key;
    private String message;
    private Object object;
    private File file;
    private Map<String, String> errors;

    public ResultInsideDTO(String key, String message, Object object, HashMap<String, String> errors) {
        this.key = key;
        this.message = message;
        this.object = object;
        this.errors = errors;
    }

    public ResultInsideDTO(String key, String message, HashMap<String, String> errors) {
        this.key = key;
        this.message = message;
        this.errors = errors;
    }

    public ResultInsideDTO(String key) {
        this.key = key;
    }

    public ResultInsideDTO(String key, String keyMap, String valueMap) {
        Map<String, String> errors = new HashMap<>();
        errors.put(keyMap, valueMap);
        this.key = key;
        this.errors = errors;
    }
}