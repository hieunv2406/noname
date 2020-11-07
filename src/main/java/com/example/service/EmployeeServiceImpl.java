package com.example.service;

import com.example.config.ResultInsideDTO;
import com.example.data.dto.EmployeeDTO;
import com.example.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public EmployeeDTO findEmployeeById(Long employeeId) {
        return employeeRepository.findEmployeeById(employeeId);
    }

    @Override
    public ResultInsideDTO insertEmployee(EmployeeDTO employeeDTO) {
        return employeeRepository.insertEmployee(employeeDTO);
    }

    @Override
    public ResultInsideDTO updateEmployee(EmployeeDTO employeeDTO) {
        return employeeRepository.updateEmployee(employeeDTO);
    }

    @Override
    public ResultInsideDTO deleteEmployeeById(Long employeeId) {
        return employeeRepository.deleteEmployeeById(employeeId);
    }

    @Override
    public List<EmployeeDTO> getListEmployeeDTO(EmployeeDTO employeeDTO) {
        return employeeRepository.getListEmployeeDTO(employeeDTO);
    }


}
