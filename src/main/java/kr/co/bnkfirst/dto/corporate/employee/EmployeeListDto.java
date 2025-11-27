package kr.co.bnkfirst.dto.corporate.employee;

import lombok.Data;

@Data
public class EmployeeListDto {
    private Long empId;
    private String name;
    private String planType;
    private String accountNo;
    private String status;
    private Long currentBalance; // 조회 시 계산 or 조인
}
