package com.example.service;

import com.example.config.Datatable;
import com.example.config.ResultInsideDTO;
import com.example.data.dto.EmployeeDTO;

public interface EmployeeService {
    EmployeeDTO findEmployeeById(Long employeeId);

    ResultInsideDTO insertEmployee(EmployeeDTO employeeDTO);

    ResultInsideDTO updateEmployee(EmployeeDTO employeeDTO);

    ResultInsideDTO deleteEmployeeById(Long employeeId);

    Datatable getListEmployeeDTO(EmployeeDTO employeeDTO);
}
