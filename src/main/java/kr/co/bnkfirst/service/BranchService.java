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

    public List<BranchDTO> getAllBranches() {
        return branchMapper.findAllBranches();
    }

    // ✅ 페이징 버전
    public List<BranchDTO> getBranchPage(PageRequestDTO pageRequestDTO) {
        int offset = pageRequestDTO.getOffset(); // (page-1)*size 이런식일 거야
        int size   = pageRequestDTO.getSize();   // 기본 5로 설정해놨겠지?
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

    // 🔥 영업점 삭제
    public void deleteBranch(int brid) {
        branchMapper.deleteBranch(brid);
    }
}
