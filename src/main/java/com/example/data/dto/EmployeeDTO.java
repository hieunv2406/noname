package com.example.data.dto;

import com.example.config.BaseDTO;
import com.example.data.entity.EmployeeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeDTO extends BaseDTO {

    private Long employeeId;
    private String code;
    private String name;
    private String fullName;
    private Date birthday;
    private Long gender;
    private String address;

    public EmployeeDTO(Long employeeId, String code, String name, String fullName, Date birthday, Long gender, String address) {
        this.employeeId = employeeId;
        this.code = code;
        this.name = name;
        this.fullName = fullName;
        this.birthday = birthday;
        this.gender = gender;
        this.address = address;
    }

    public EmployeeEntity toEntity() {
        return new EmployeeEntity(
                employeeId,
                code,
                name,
                fullName,
                birthday,
                gender,
                address
        );
    }
}
