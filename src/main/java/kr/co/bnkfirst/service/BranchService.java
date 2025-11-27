package kr.co.bnkfirst.service;

import kr.co.bnkfirst.dto.BranchDTO;
import kr.co.bnkfirst.dto.PageRequestDTO;
import kr.co.bnkfirst.mapper.BranchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchMapper branchMapper;

    // ========================================
    // 기존 관리자용(DB) 기능 유지
    // ========================================

    public List<BranchDTO> getAllBranches() {
        return branchMapper.findAllBranches();
    }

    public List<BranchDTO> getBranchPage(PageRequestDTO pageRequestDTO) {
        int offset = pageRequestDTO.getOffset();
        int size = pageRequestDTO.getSize();
        return branchMapper.findBranchPage(offset, size);
    }

    public int getBranchTotal() {
        return branchMapper.countBranches();
    }

    public List<BranchDTO> searchBranches(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return branchMapper.findAllBranches();
        }
        return branchMapper.searchBranches(keyword);
    }

    public void insertBranch(BranchDTO dto) {
        branchMapper.insertBranch(dto);
    }

    public BranchDTO getBranchById(int brid) {
        return branchMapper.findBranchById(brid);
    }

    public void updateBranch(BranchDTO dto) {
        branchMapper.updateBranch(dto);
    }

    public void deleteBranch(int brid) {
        branchMapper.deleteBranch(brid);
    }
}
