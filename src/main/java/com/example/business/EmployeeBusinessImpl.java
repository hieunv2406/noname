package com.example.business;

import com.example.common.Datatable;
import com.example.common.dto.ResultInsideDTO;
import com.example.data.dto.EmployeeDTO;
import com.example.repository.EmployeeRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Log4j2
@Service
public class EmployeeBusinessImpl implements EmployeeBusiness {
    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public EmployeeDTO findEmployeeById(Long employeeId) {
        log.info("findEmployeeById", employeeId);
        return employeeRepository.findEmployeeById(employeeId);
    }

    @Override
    public ResultInsideDTO insertEmployee(EmployeeDTO employeeDTO) {
        log.info("insertEmployee", employeeDTO);
        return employeeRepository.insertEmployee(employeeDTO);
    }

    @Override
    public ResultInsideDTO updateEmployee(EmployeeDTO employeeDTO) {
        log.info("updateEmployee", employeeDTO);
        return employeeRepository.updateEmployee(employeeDTO);
    }

    @Override
    public ResultInsideDTO deleteEmployeeById(Long employeeId) {
        log.info("deleteEmployeeById", employeeId);
        return employeeRepository.deleteEmployeeById(employeeId);
    }

    @Override
    public Datatable getListEmployeeDTO(EmployeeDTO employeeDTO) {
        log.info("getListEmployeeDTO", employeeDTO);
        return employeeRepository.getListEmployeeDTO(employeeDTO);
    }

    @Override
    public List<Map<String, Object>> getListEmployeeMap() {
        log.info("getListEmployeeMap");
        return employeeRepository.getListEmployeeMap();
    }

    @Override
    public List<EmployeeDTO> getListDataExport(EmployeeDTO employeeDTO) {
        log.info("getListDataExport", employeeDTO);
        return employeeRepository.getListDataExport(employeeDTO);
    }


}
