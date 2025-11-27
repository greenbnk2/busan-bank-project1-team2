package kr.co.bnkfirst.dto.corporate.employee;

import lombok.Data;

@Data
public class EmployeeDetailDto {
    private Long empId;
    private String name;
    private String planType;
    private String accountNo;
    private String status;
    private Long currentBalance;
}
