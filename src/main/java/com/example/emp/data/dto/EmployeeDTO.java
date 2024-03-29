package com.example.emp.data.dto;

import com.example.common.dto.BaseDTO;
import com.example.common.validator.MultiFieldUnique;
import com.example.emp.data.entity.EmployeeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MultiFieldUnique(clazz = EmployeeEntity.class, uniqueFields = "code,username,email", idField = "employeeId", message = "{validation.unique.employee}")
public class EmployeeDTO extends BaseDTO {

    private Long employeeId;
    @NotEmpty(message = "{validation.employee.code.notnull}")
    private String code;
    @NotEmpty(message = "{validation.employee.username.notnull}")
    private String username;
    private String fullName;
    private String email;
    private Date birthday;
    private Long gender;
    private String address;
    private MultipartFile file;

    private String genderStr;
    private String birthdayStr;

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
