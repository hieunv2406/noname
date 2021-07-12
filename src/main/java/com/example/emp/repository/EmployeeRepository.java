package com.example.emp.repository;

import com.example.common.dto.Datatable;
import com.example.common.dto.ResultInsideDTO;
import com.example.emp.data.dto.EmployeeDTO;

import java.util.List;
import java.util.Map;

public interface EmployeeRepository {

    EmployeeDTO findEmployeeById(Long employee_id);

    ResultInsideDTO insertEmployee(EmployeeDTO employeeDTO);

    ResultInsideDTO insertEmployeeList(List<EmployeeDTO> employeeDTOList);

    ResultInsideDTO updateEmployee(EmployeeDTO employeeDTO);

    ResultInsideDTO deleteEmployeeById(Long employeeId);

    Datatable getListEmployeeDTO(EmployeeDTO employeeDTO);

    List<EmployeeDTO> getListDataExport(EmployeeDTO employeeDTO);

    List<Map<String, Object>> getListEmployeeMap();
}
