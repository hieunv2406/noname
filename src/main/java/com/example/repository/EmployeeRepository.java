package com.example.repository;

import com.example.common.Datatable;
import com.example.common.dto.ResultInsideDTO;
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
