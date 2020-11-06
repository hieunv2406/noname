package com.example.controller;

import com.example.data.dto.EmployeeDTO;
import com.example.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController(value = "/employeeController")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;


    @GetMapping(value = "/getDetail/{employeeId}")
    public ResponseEntity<EmployeeDTO> findEmployeeById(@PathVariable("employeeId") Long employeeId) {
        EmployeeDTO employeeDTO = employeeService.findEmployeeById(employeeId);
        return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
    }
}
