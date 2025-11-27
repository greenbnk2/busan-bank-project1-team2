package kr.co.bnkfirst.service;

import kr.co.bnkfirst.domain.Contribution;
import kr.co.bnkfirst.mapper.ContributionMapper; // ★ 반드시 필요
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContributionServiceImpl implements ContributionService {

    private final ContributionMapper contributionMapper;

    @Override
    public List<Contribution> getContributionByEmpId(Long empId) {
        return contributionMapper.findByEmpId(empId);
    }

    @Override
    public Long getCurrentBalance(Long empId) {
        return contributionMapper.findCurrentBalance(empId);
    }
}
