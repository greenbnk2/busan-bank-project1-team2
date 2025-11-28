package kr.co.bnkfirst.service;

import kr.co.bnkfirst.dto.corporate.employee.*;
import java.util.List;

public interface EmployeeService {

    List<EmployeeListDto> getEmployeeList();

    EmployeeDetailDto getEmployeeDetail(Long empId);

    List<EmployeeContributionDto> getEmployeeContributions(Long empId);

    int getTotalEmployees();

    List<EmployeeListDto> search(String keyword, String planType);

    Long getEmployeeCurrentBalance(Long empId);

    void updateEmployee(EmployeeUpdateDto dto);

    void deleteEmployee(Long empId);

    // ⭐ 자동완성 (전용 DTO)
    List<EmployeeAutoDto> autocomplete(String keyword);
}
