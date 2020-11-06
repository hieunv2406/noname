package com.example.repository;

import com.example.data.dto.EmployeeDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository {

    public EmployeeDTO findEmployeeById(Long employee_id);
}
