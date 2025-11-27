package kr.co.bnkfirst.service;

import kr.co.bnkfirst.domain.Contribution;
import java.util.List;

public interface ContributionService {
    List<Contribution> getContributionByEmpId(Long empId);
    Long getCurrentBalance(Long empId);
}
