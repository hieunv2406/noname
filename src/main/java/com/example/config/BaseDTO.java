package com.example.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BaseDTO {

    private Long page;
    private Long pageSize;
    private String sortBy;
    private String sortOrder;

}
