package com.example.repository;

import com.example.data.dto.EmployeeDTO;
import com.example.data.entity.EmployeeEntity;

import javax.persistence.EntityManager;

public class EmployeeRepositoryImpl implements EmployeeRepository {

    private EntityManager entityManager;

    @Override
    public EmployeeDTO findEmployeeById(Long employee_id) {
        EmployeeEntity employeeEntity = entityManager.find(EmployeeEntity.class, employee_id);
        EmployeeDTO employeeDTO = employeeEntity.toDto();
        return employeeDTO;
    }
}
