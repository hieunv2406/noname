package com.example.emp.repository;

import com.example.common.Constants;
import com.example.common.dto.BaseDTO;
import com.example.common.dto.Datatable;
import com.example.common.dto.ResultInsideDTO;
import com.example.common.repository.BaseRepository;
import com.example.common.utils.DataUtil;
import com.example.emp.data.dto.EmployeeDTO;
import com.example.emp.data.entity.EmployeeEntity;
import com.example.emp.exceptions.EmployeeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Repository
public class EmployeeRepositoryImpl extends BaseRepository implements EmployeeRepository {


    @Override
    public EmployeeDTO findEmployeeById(Long employeeId) {
        EmployeeEntity employeeEntity = getEntityManager().find(EmployeeEntity.class, employeeId);
        EmployeeDTO employeeDTO = employeeEntity.toDto();
        return employeeDTO;
    }

    @Override
    public ResultInsideDTO insertEmployee(EmployeeDTO employeeDTO) {
        ResultInsideDTO resultInsideDTO = new ResultInsideDTO(new ResultInsideDTO.Status(HttpStatus.OK.value(), Constants.ResponseKey.SUCCESS));
        EmployeeEntity employeeEntity = getEntityManager().merge(employeeDTO.toEntity());
        resultInsideDTO.setData(employeeEntity);
        return resultInsideDTO;
    }

    @Override
    public ResultInsideDTO insertEmployeeList(List<EmployeeDTO> employeeDTOList) {
        ResultInsideDTO resultInsideDTO = new ResultInsideDTO(new ResultInsideDTO.Status(HttpStatus.OK.value(), Constants.ResponseKey.SUCCESS));
        for (EmployeeDTO employeeDTO : employeeDTOList) {
            getEntityManager().merge(employeeDTO.toEntity());
        }
        return resultInsideDTO;
    }

    @Override
    public ResultInsideDTO updateEmployee(EmployeeDTO employeeDTO) throws EmployeeNotFoundException {
        ResultInsideDTO resultInsideDTO = new ResultInsideDTO(new ResultInsideDTO.Status(HttpStatus.OK.value(), Constants.ResponseKey.SUCCESS));
        EmployeeEntity employeeEntity = getEntityManager().find(EmployeeEntity.class, employeeDTO.getEmployeeId());
        if (employeeEntity != null) {
            employeeEntity = getEntityManager().merge(employeeDTO.toEntity());
            resultInsideDTO.setData(employeeEntity);
        } else {
            throw new EmployeeNotFoundException(Constants.ResponseKey.RECORD_NOT_EXIST);
        }
        return resultInsideDTO;
    }

    @Override
    public ResultInsideDTO deleteEmployeeById(Long employeeId) {
        ResultInsideDTO resultInsideDTO = new ResultInsideDTO(new ResultInsideDTO.Status(HttpStatus.OK.value(), Constants.ResponseKey.SUCCESS));
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

    @Override
    public List<EmployeeDTO> getListDataExport(EmployeeDTO employeeDTO) {
        BaseDTO baseDTO = sqlSearch(employeeDTO);
        return getNamedParameterJdbcTemplate().query(baseDTO.getSqlQuery()
                , baseDTO.getParameters()
                , BeanPropertyRowMapper.newInstance(EmployeeDTO.class));
    }

    @Override
    public List<Map<String, Object>> getListEmployeeMap() {

        Map<String, Object> beanMap = new HashMap<>();
        String sql = " select " +
                " t.cot1 cot1, " +
                " t.cot2 cot2, " +
                " t.cot3 cot3 " +
                " from " +
                " table_test t " +
                " where " +
                " 1 = 1 ";
        List<Map<String, Object>> mapResult = getNamedParameterJdbcTemplate().queryForList(sql, beanMap);
        return mapResult;
    }

    private BaseDTO sqlSearch(EmployeeDTO employeeDTO) {
        BaseDTO baseDTO = new BaseDTO();
        Map<String, Object> parameter = new HashMap<>();
        String sql = getSQLFromFile("employee", "getEmployeeDTO");
        if (employeeDTO != null) {
            if (!DataUtil.isNullOrEmpty(employeeDTO.getCode())) {
                sql += " And e.code = :code ";
                parameter.put("code", employeeDTO.getCode());
            }
            if (!DataUtil.isNullOrEmpty(employeeDTO.getUsername())) {
                sql += " And lower(e.username) Like lower(:username) ";
                parameter.put("username", DataUtil.convertSqlLike(employeeDTO.getUsername()));
            }
            if (!DataUtil.isNullOrEmpty(employeeDTO.getGender())) {
                sql += " And e.gender = :gender ";
                parameter.put("gender", employeeDTO.getGender());
            }
        }
        sql += " ORDER BY e.employee_id ASC ";
        baseDTO.setSqlQuery(sql);
        baseDTO.setParameters(parameter);
        return baseDTO;
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ResultInsideDTO> handleEmployeeNotFoundException(EmployeeNotFoundException employeeNotFoundException) {
        ResultInsideDTO resultInsideDTO = new ResultInsideDTO(
                new ResultInsideDTO.Status(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name()),
                employeeNotFoundException.getMessage());
        return new ResponseEntity<>(resultInsideDTO, HttpStatus.NOT_FOUND);
    }
}
