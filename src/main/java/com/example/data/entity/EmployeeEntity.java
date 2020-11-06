package com.example.data.entity;

import com.example.data.dto.EmployeeDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "EMPLOYEE")
public class EmployeeEntity {
    @Id
    @GeneratedValue
    @Column(name = "EMPLOYEE_ID", nullable = false)
    private Long employeeId;

    @Column(name = "CODE")
    private String code;

    @Column(name = "NAME")
    private String name;

    @Column(name = "FULL_NAME")
    private String fullName;

    @Column(name = "BIRTHDAY")
    private Date birthday;

    @Column(name = "GENDER")
    private Long gender;

    @Column(name = "ADDRESS")
    private String address;

    public EmployeeEntity(Long employeeId, String code, String name, String fullName, Date birthday, Long gender, String address) {
        this.employeeId = employeeId;
        this.code = code;
        this.name = name;
        this.fullName = fullName;
        this.birthday = birthday;
        this.gender = gender;
        this.address = address;
    }

    public EmployeeDTO toDto() {
        return new EmployeeDTO(
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
