package com.example.data.entity;

import com.example.data.dto.EmployeeDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "EMPLOYEE")
public class EmployeeEntity implements Serializable {
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = Constant.SEQUENCE_KEY.EMPLOYEE)
//    @SequenceGenerator(name = Constant.SEQUENCE_KEY.EMPLOYEE, sequenceName = "SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EMPLOYEE_ID")
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
