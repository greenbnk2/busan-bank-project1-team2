package kr.co.bnkfirst.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.bnkfirst.dto.KftcBranchDTO;
import kr.co.bnkfirst.dto.KftcBranchDetailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KftcBranchApiService {

    // application.yml 에서 불러옴
    @Value("${kftc.access-token}")
    private String accessToken;  // application.yml에 등록한 Access Token

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * ① 금융결제원 지점 상세조회 API
     */
    public KftcBranchDetailResponse getBranchDetail(
            String trmsOrgCode,
            String brchCode,
            String dupBrchCode
    ) {

        String url = "https://openapi.finmap.or.kr/v1.0/kftc/inquiry/brch_detail";

        // 요청 바디(JSON)
        Map<String, String> body = new HashMap<>();
        body.put("trms_org_code", trmsOrgCode);
        body.put("brch_code", brchCode);
        body.put("dup_brch_code", dupBrchCode);

        log.info("📨 금융결제원 요청 Body = {}", body);


        // HTTP 헤더
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<KftcBranchDetailResponse> response =
                restTemplate.exchange(url, HttpMethod.POST, entity, KftcBranchDetailResponse.class);

        log.info("📩 금융결제원 응답 = {}", response.getBody());

        return response.getBody();
    }

    /**
     * ② ⭐ 금융결제원 지점 목록 조회 API (BranchService가 반드시 필요로 함)
     */
    public List<KftcBranchDTO> getKftcBranches() {

        String url = "https://openapi.finmap.or.kr/v1.0/kftc/inquiry/brch_list";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>("", headers);

        ResponseEntity<KftcBranchDTO[]> response =
                restTemplate.exchange(url, HttpMethod.POST, entity, KftcBranchDTO[].class);

        KftcBranchDTO[] arr = response.getBody();

        if (arr == null) {
            return new ArrayList<>();
        }

        return Arrays.asList(arr);
    }

}
