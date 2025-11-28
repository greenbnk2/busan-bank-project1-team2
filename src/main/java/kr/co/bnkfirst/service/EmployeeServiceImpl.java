package kr.co.bnkfirst.service;

import kr.co.bnkfirst.dto.corporate.employee.*;
import kr.co.bnkfirst.mapper.EmployeeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Override
    public int getTotalEmployees() {
        return employeeMapper.getTotalEmployees();
    }

    @Override
    public List<EmployeeListDto> search(String keyword, String planType) {
        if (planType == null || planType.isEmpty()) {
            planType = "ALL";
        }
        return employeeMapper.search(keyword, planType);
    }

    @Transactional
    @Override
    public void updateEmployee(EmployeeUpdateDto dto) {
        employeeMapper.updateEmployee(dto);
    }

    @Transactional
    @Override
    public void deleteEmployee(Long empId) {
        employeeMapper.deleteEmployee(empId);
    }

    @Override
    public Long getEmployeeCurrentBalance(Long empId) {
        return employeeMapper.getCurrentBalance(empId);
    }

    @Override
    public List<EmployeeAutoDto> autocomplete(String keyword) {
        return employeeMapper.autocomplete(keyword);
    }
}
