package kr.co.bnkfirst.dto.corporate.employee;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmployeeCreateDto {

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @NotBlank(message = "제도(DB/DC)는 필수입니다.")
    private String planType;

    @NotBlank(message = "계좌번호는 필수입니다.")
    private String accountNo;

    private String status = "ACTIVE"; // 기본값
}
