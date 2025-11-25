package kr.co.bnkfirst.service;

import kr.co.bnkfirst.dto.BranchDTO;
import kr.co.bnkfirst.dto.PageRequestDTO;
import kr.co.bnkfirst.dto.KftcBranchDTO;
import kr.co.bnkfirst.mapper.BranchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchMapper branchMapper;
    private final KftcBranchApiService kftcBranchApiService;   // 금융결제원 API 서비스

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

    // ========================================
    // 🔥 신규 기능: DB + KFTC 외부 API 통합 조회
    // ========================================
    public List<BranchDTO> getIntegratedBranches() {

        // 1) DB 데이터
        List<BranchDTO> dbList = branchMapper.findAllBranches();

        // 2) 금융결제원 API 데이터
        List<KftcBranchDTO> apiList = kftcBranchApiService.getKftcBranches();

        // key: "branchCode" 또는 "brid"
        Map<String, BranchDTO> map = new LinkedHashMap<>();

        // ===========================
        // 1단계: DB 데이터 먼저 넣기
        // ===========================
        for (BranchDTO db : dbList) {

            BranchDTO dto = new BranchDTO();

            // 내부 DB 필드 채우기
            dto.setBrid(db.getBrid());
            dto.setBrname(db.getBrname());
            dto.setBraddr(db.getBraddr());
            dto.setBrtel(db.getBrtel());
            dto.setBrfax(db.getBrfax());

            // 외부 API 필드는 우선 null로 둠
            dto.setBankCode(null);
            dto.setBankName(null);
            dto.setBranchCode(null);
            dto.setBranchName(null);
            dto.setAddress(null);
            dto.setTel(null);
            dto.setLatitude(null);
            dto.setLongitude(null);

            map.put(String.valueOf(db.getBrid()), dto);
        }

        // ===========================
        // 2단계: API 데이터 보완
        // ===========================
        for (KftcBranchDTO api : apiList) {

            // API 지점코드가 key
            String key = api.getBrCode();

            BranchDTO existing = map.get(key);

            if (existing == null) {
                // DB에 없는 지점 → 새로 생성
                BranchDTO dto = new BranchDTO();

                // DB필드 없음 → null
                dto.setBrid(0);
                dto.setBrname(null);
                dto.setBraddr(null);
                dto.setBrtel(null);
                dto.setBrfax(null);

                // 외부 API 필드 채우기
                dto.setBankCode(api.getOrgCode());
                dto.setBankName(api.getBrName());
                dto.setBranchCode(api.getBrCode());
                dto.setBranchName(api.getBrName());
                dto.setAddress(api.getBrAddress());
                dto.setTel(api.getBrTel()); // 외부 전화번호
                dto.setLatitude(api.getLatitude());
                dto.setLongitude(api.getLongitude());

                map.put(key, dto);

            } else {
                // DB 정보는 유지하고 API 정보 보완
                existing.setBankCode(api.getOrgCode());
                existing.setBankName(api.getBrName());
                existing.setBranchCode(api.getBrCode());
                existing.setBranchName(api.getBrName());
                existing.setAddress(api.getBrAddress());

                if (api.getBrTel() != null) existing.setTel(api.getBrTel());
                if (api.getLatitude() != null) existing.setLatitude(api.getLatitude());
                if (api.getLongitude() != null) existing.setLongitude(api.getLongitude());

                map.put(key, existing);
            }
        }

        return new ArrayList<>(map.values());
    }

    // ======================================================
    // 🔍 통합 검색 — DB + API 병합된 리스트 대상으로 검색
    // ======================================================
    public List<BranchDTO> searchIntegratedBranches(String keyword) {

        List<BranchDTO> all = getIntegratedBranches();

        if (keyword == null || keyword.isBlank()) {
            return all;
        }

        String kw = keyword.toLowerCase();
        List<BranchDTO> result = new ArrayList<>();

        for (BranchDTO dto : all) {

            boolean match =
                    (dto.getBrname() != null && dto.getBrname().toLowerCase().contains(kw)) ||
                            (dto.getBranchName() != null && dto.getBranchName().toLowerCase().contains(kw)) ||
                            (dto.getBraddr() != null && dto.getBraddr().toLowerCase().contains(kw)) ||
                            (dto.getAddress() != null && dto.getAddress().toLowerCase().contains(kw)) ||
                            (dto.getBranchCode() != null && dto.getBranchCode().toLowerCase().contains(kw));

            if (match) result.add(dto);
        }

        return result;
    }
}
