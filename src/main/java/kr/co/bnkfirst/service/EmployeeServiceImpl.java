package kr.co.bnkfirst.service;

import kr.co.bnkfirst.dto.corporate.employee.EmployeeListDto;       // ⬅ 추가
import kr.co.bnkfirst.dto.corporate.employee.EmployeeDetailDto;     // ⬅ 이미 있을 가능성
import kr.co.bnkfirst.dto.corporate.employee.EmployeeContributionDto;  // ⬅ 이미 있을 가능성

import kr.co.bnkfirst.mapper.EmployeeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeMapper employeeMapper;

    @Override
    public List<EmployeeListDto> getEmployeeList() {
        return employeeMapper.findAllEmployees();
    }

    @Override
    public EmployeeDetailDto getEmployeeDetail(Long empId) {
        return employeeMapper.findEmployeeById(empId);
    }

    @Override
    public List<EmployeeContributionDto> getEmployeeContributions(Long empId) {
        return employeeMapper.findContributionsByEmpId(empId);
    }
}
