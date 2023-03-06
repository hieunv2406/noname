package com.example.feignClient;

import com.example.emp.data.dto.EmployeeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "employee", url = "localhost:8082")
public interface EmployeeServiceProxy {

    @GetMapping("/employee/getDetail")
    EmployeeDTO findEmployeeById(@RequestParam Long employeeId);
}
