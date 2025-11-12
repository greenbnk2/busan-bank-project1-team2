package kr.co.bnkfirst.service;

import kr.co.bnkfirst.dto.BranchDTO;
import kr.co.bnkfirst.mapper.BranchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchService {
    private final BranchMapper branchMapper;

    public List<BranchDTO> getAllBranches() {
        return branchMapper.findAllBranches();
    }

    public List<BranchDTO> searchBranches(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return branchMapper.findAllBranches();
        }
        return branchMapper.searchBranches(keyword);
    }
}
