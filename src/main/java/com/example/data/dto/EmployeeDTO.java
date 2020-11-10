package com.example.data.dto;

import com.example.common.dto.BaseDTO;
import com.example.data.entity.EmployeeEntity;
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
    private String username;
    private String fullName;
    private String email;
    private Date birthday;
    private Long gender;
    private String address;

    public EmployeeDTO(Long employeeId, String code, String username, String fullName, String email, Date birthday, Long gender, String address) {
        this.employeeId = employeeId;
        this.code = code;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.birthday = birthday;
        this.gender = gender;
        this.address = address;
    }

    public EmployeeEntity toEntity() {
        return new EmployeeEntity(
                employeeId,
                code,
                username,
                fullName,
                email,
                birthday,
                gender,
                address
        );
    }
}
