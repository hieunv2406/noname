package com.example.repository;

import com.example.config.*;
import com.example.data.dto.EmployeeDTO;
import com.example.data.entity.EmployeeEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
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
    public Datatable getListEmployeeDTO(EmployeeDTO employeeDTO) {
        BaseDTO baseDTO = sqlSearch(employeeDTO);
        return getListDataTableBySqlQuery(baseDTO.getSqlQuery(),
                baseDTO.getParameters(), employeeDTO.getPage(), employeeDTO.getPageSize(),
                EmployeeDTO.class,
                employeeDTO.getSortName(), employeeDTO.getSortType());
    }

    private BaseDTO sqlSearch(EmployeeDTO employeeDTO) {
        BaseDTO baseDTO = new BaseDTO();
        Map<String, Object> parameter = new HashMap<>();
        String sql = " select " +
                " e.employee_id employeeId, " +
                " e.code code, " +
                " e.username username, " +
                " e.full_name fullName, " +
                " e.email email, " +
                " e.birthday birthday, " +
                " e.gender gender, " +
                " e.address " +
                " from " +
                " employee e " +
                " where " +
                " 1 = 1 ";
        if (employeeDTO != null) {
            if (employeeDTO.getCode() != null) {
                sql += " And e.code = :code ";
                parameter.put("code", employeeDTO.getCode());
            }
        }
        baseDTO.setSqlQuery(sql);
        baseDTO.setParameters(parameter);
        return baseDTO;
    }
}
