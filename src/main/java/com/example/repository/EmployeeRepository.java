package com.example.repository;

import com.example.config.Datatable;
import com.example.config.ResultInsideDTO;
import com.example.data.dto.EmployeeDTO;

import java.util.List;
import java.util.Map;

public interface EmployeeRepository {

    EmployeeDTO findEmployeeById(Long employee_id);

    ResultInsideDTO insertEmployee(EmployeeDTO employeeDTO);

    ResultInsideDTO updateEmployee(EmployeeDTO employeeDTO);

    ResultInsideDTO deleteEmployeeById(Long employeeId);

    Datatable getListEmployeeDTO(EmployeeDTO employeeDTO);

    List<Map<String, Object>> getListEmployeeMap();
}
