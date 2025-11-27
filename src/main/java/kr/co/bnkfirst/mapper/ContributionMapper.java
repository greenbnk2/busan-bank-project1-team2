package kr.co.bnkfirst.mapper;

import kr.co.bnkfirst.domain.Contribution;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContributionMapper {
    List<Contribution> findByEmpId(Long empId);
    Long findCurrentBalance(Long empId);
}
