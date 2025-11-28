package kr.co.bnkfirst.mapper;

import kr.co.bnkfirst.dto.corporate.employee.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface EmployeeMapper {

    List<EmployeeListDto> findAllEmployees();

    EmployeeDetailDto findEmployeeById(@Param("empId") Long empId);

    List<EmployeeContributionDto> findContributionsByEmpId(@Param("empId") Long empId);

    int getTotalEmployees();

    List<EmployeeListDto> search(@Param("keyword") String keyword,
                                 @Param("planType") String planType);

    void updateEmployee(EmployeeUpdateDto dto);

    void deleteEmployee(@Param("empId") Long empId);

    Long getCurrentBalance(@Param("empId") Long empId);

    // ⭐ 자동완성 (EmployeeAutoDto로 타입 변경)
    List<EmployeeAutoDto> autocomplete(@Param("keyword") String keyword);
}
