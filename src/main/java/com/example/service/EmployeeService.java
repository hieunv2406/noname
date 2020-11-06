package com.example.service;

import com.example.data.dto.EmployeeDTO;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeService {
    public EmployeeDTO findEmployeeById(Long employeeId);
}
