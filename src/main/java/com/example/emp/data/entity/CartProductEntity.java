package com.example.emp.data.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CART_PRODUCT")
public class CartProductEntity implements Serializable {
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = Constant.SEQUENCE_KEY.EMPLOYEE)
//    @SequenceGenerator(name = Constant.SEQUENCE_KEY.EMPLOYEE, sequenceName = "SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartProductId")
    private Long cartProductId;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "status")
    private Long status;

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
    @JoinColumn(name = "cartId", nullable = false)
    private CartEntity cart;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private ProductEntity product;
}
