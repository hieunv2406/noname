package com.example.repository;

import com.example.config.ResultInsideDTO;
import com.example.data.dto.EmployeeDTO;

import java.util.List;

public interface EmployeeRepository {

    EmployeeDTO findEmployeeById(Long employee_id);

    ResultInsideDTO insertEmployee(EmployeeDTO employeeDTO);

    ResultInsideDTO updateEmployee(EmployeeDTO employeeDTO);

    ResultInsideDTO deleteEmployeeById(Long employeeId);

    List<EmployeeDTO> getListEmployeeDTO(EmployeeDTO employeeDTO);
}
