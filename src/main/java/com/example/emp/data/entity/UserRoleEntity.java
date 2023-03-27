package com.example.emp.data.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USER_ROLE")
public class UserRoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userRoleId")
    private Long userRoleId;

    @Column(name = "createdAt")
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Date createdAt;

    @Column(name = "createdBy")
    private String createdBy;

    @Column(name = "updatedAt")
    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Date updatedAt;

    @Column(name = "updatedBy")
    private String updatedBy;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private UserEntity user;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "roleId", nullable = false)
    private RoleEntity role;

//    public UserRolesDto toDTO() {
//        return new UserRolesDto(
//                id,
//                userId,
//                rolesId
//        );
//    }

}
