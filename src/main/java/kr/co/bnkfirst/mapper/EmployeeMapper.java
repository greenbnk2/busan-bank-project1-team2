package kr.co.bnkfirst.mapper;

import kr.co.bnkfirst.dto.corporate.employee.EmployeeListDto;
import kr.co.bnkfirst.dto.corporate.employee.EmployeeDetailDto;
import kr.co.bnkfirst.dto.corporate.employee.EmployeeContributionDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface EmployeeMapper {

    List<EmployeeListDto> findAllEmployees();

    EmployeeDetailDto findEmployeeById(@Param("empId") Long empId);

    List<EmployeeContributionDto> findContributionsByEmpId(@Param("empId") Long empId);
}
