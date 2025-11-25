package kr.co.bnkfirst.controller;

import kr.co.bnkfirst.dto.KftcBranchDTO;
import kr.co.bnkfirst.dto.KftcBranchDetailRequest;
import kr.co.bnkfirst.dto.KftcBranchDetailResponse;
import kr.co.bnkfirst.service.KftcBranchApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/kftc/branch")
@RequiredArgsConstructor
public class KftcBranchController {

    private final KftcBranchApiService apiService;

    // ⭐ 목록 조회 API ———— 여기가 정확한 위치 + 정확한 코드
    @GetMapping("/list")
    public List<KftcBranchDTO> getBranchList() {
        return apiService.getKftcBranches();
    }


    // ⭐ 상세 조회 API
    @PostMapping("/detail")
    public KftcBranchDetailResponse getBranchDetail(@RequestBody KftcBranchDetailRequest req) {
        return apiService.getBranchDetail(
                req.getTrms_org_code(),
                req.getBrch_code(),
                req.getDup_brch_code()
        );
    }
}
