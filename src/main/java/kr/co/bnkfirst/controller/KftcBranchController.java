package kr.co.bnkfirst.controller;

import kr.co.bnkfirst.dto.KftcBranchDetailRequest;
import kr.co.bnkfirst.dto.KftcBranchDetailResponse;
import kr.co.bnkfirst.service.KftcBranchApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/kftc/branch")
@RequiredArgsConstructor
public class KftcBranchController {

    private final KftcBranchApiService apiService;

    @PostMapping("/detail")
    public KftcBranchDetailResponse getBranchDetail(@RequestBody KftcBranchDetailRequest req) {
        return apiService.getBranchDetail(
                req.getTrms_org_code(),
                req.getBrch_code(),
                req.getDup_brch_code()
        );
    }
}
