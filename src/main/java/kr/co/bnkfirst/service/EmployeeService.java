package kr.co.bnkfirst.service;

import kr.co.bnkfirst.dto.corporate.employee.EmployeeListDto;      // ⬅ 이거 추가!!
import kr.co.bnkfirst.dto.corporate.employee.EmployeeDetailDto;
import kr.co.bnkfirst.dto.corporate.employee.EmployeeContributionDto;

import java.util.List;

public interface EmployeeService {

    List<EmployeeListDto> getEmployeeList();

    EmployeeDetailDto getEmployeeDetail(Long empId);

    List<EmployeeContributionDto> getEmployeeContributions(Long empId);
}
