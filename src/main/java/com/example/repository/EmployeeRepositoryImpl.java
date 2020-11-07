package com.example.repository;

import com.example.config.BaseRepository;
import com.example.config.Constant;
import com.example.config.ResultInsideDTO;
import com.example.data.dto.EmployeeDTO;
import com.example.data.entity.EmployeeEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Repository
public class EmployeeRepositoryImpl extends BaseRepository implements EmployeeRepository {


    @Override
    public EmployeeDTO findEmployeeById(Long employee_id) {
        EmployeeEntity employeeEntity = getEntityManager().find(EmployeeEntity.class, employee_id);
        EmployeeDTO employeeDTO = employeeEntity.toDto();
        return employeeDTO;
    }

    @Override
    public ResultInsideDTO insertEmployee(EmployeeDTO employeeDTO) {
        ResultInsideDTO resultInsideDTO = new ResultInsideDTO();
        resultInsideDTO.setKey(Constant.RESPONSE_KEY.SUCCESS);
        EmployeeEntity employeeEntity = getEntityManager().merge(employeeDTO.toEntity());
        resultInsideDTO.setId(employeeEntity.getEmployeeId());
        return resultInsideDTO;
    }

    @Override
    public ResultInsideDTO updateEmployee(EmployeeDTO employeeDTO) {
        ResultInsideDTO resultInsideDTO = new ResultInsideDTO();
        resultInsideDTO.setKey(Constant.RESPONSE_KEY.SUCCESS);
        EmployeeEntity employeeEntity = getEntityManager().find(EmployeeEntity.class, employeeDTO.getEmployeeId());
        if (employeeEntity != null) {
            employeeEntity = getEntityManager().merge(employeeDTO.toEntity());
            resultInsideDTO.setId(employeeEntity.getEmployeeId());
        } else {
            resultInsideDTO.setKey(Constant.RESPONSE_KEY.RECORD_NOT_EXIST);
        }
        return resultInsideDTO;
    }

    @Override
    public ResultInsideDTO deleteEmployeeById(Long employeeId) {
        ResultInsideDTO resultInsideDTO = new ResultInsideDTO();
        resultInsideDTO.setKey(Constant.RESPONSE_KEY.SUCCESS);
        EmployeeEntity employeeEntity = getEntityManager().find(EmployeeEntity.class, employeeId);
        getEntityManager().remove(employeeEntity);
        return resultInsideDTO;
    }

    @Override
    public List<EmployeeDTO> getListEmployeeDTO(EmployeeDTO employeeDTO) {
        Map<String,String> parameter = new HashMap<>();
        String sql = "Select * from employee";
        if (employeeDTO != null){
            if (employeeDTO.getCode() != null){
                sql += " And e.code = :code ";
                parameter.put("code", employeeDTO.getCode());
            }
        }

        return null;
    }
}
