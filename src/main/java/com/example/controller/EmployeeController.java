package com.example.controller;

import com.example.config.Datatable;
import com.example.config.ResultInsideDTO;
import com.example.data.dto.EmployeeDTO;
import com.example.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/employeeController")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @PostMapping(value = "/getEmployeeDTO")
    public ResponseEntity<Datatable> getListEmployeeDTO(@RequestBody EmployeeDTO employeeDTO) {
        Datatable datatable = employeeService.getListEmployeeDTO(employeeDTO);
        return new ResponseEntity<>(datatable, HttpStatus.OK);
    }

    @GetMapping(value = "/getDetail")
    public ResponseEntity<EmployeeDTO> findEmployeeById(@RequestParam Long employeeId) {
        EmployeeDTO employeeDTO = employeeService.findEmployeeById(employeeId);
        return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/insert")
    public ResponseEntity<ResultInsideDTO> insertEmployee(@RequestBody EmployeeDTO employeeDTO) {
        ResultInsideDTO resultInsideDTO = employeeService.insertEmployee(employeeDTO);
        return new ResponseEntity<>(resultInsideDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/update")
    public ResponseEntity<ResultInsideDTO> updateEmployee(@RequestBody EmployeeDTO employeeDTO) {
        ResultInsideDTO resultInsideDTO = employeeService.updateEmployee(employeeDTO);
        return new ResponseEntity<>(resultInsideDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/delete")
    public ResponseEntity<ResultInsideDTO> deleteEmployeeById(@RequestParam Long employeeId) {
        ResultInsideDTO resultInsideDTO = employeeService.deleteEmployeeById(employeeId);
        return new ResponseEntity<>(resultInsideDTO, HttpStatus.OK);
    }

}
