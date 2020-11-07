package com.example.service;

import com.example.config.ResultInsideDTO;
import com.example.data.dto.EmployeeDTO;

import java.util.List;

public interface EmployeeService {
    EmployeeDTO findEmployeeById(Long employeeId);

    ResultInsideDTO insertEmployee(EmployeeDTO employeeDTO);

    ResultInsideDTO updateEmployee(EmployeeDTO employeeDTO);

    ResultInsideDTO deleteEmployeeById(Long employeeId);

    List<EmployeeDTO> getListEmployeeDTO(EmployeeDTO employeeDTO);
}
