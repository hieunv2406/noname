package com.example.service;

import com.example.data.dto.EmployeeDTO;
import com.example.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public EmployeeDTO findEmployeeById(Long employeeId) {
        return employeeRepository.findEmployeeById(employeeId);
    }
}
