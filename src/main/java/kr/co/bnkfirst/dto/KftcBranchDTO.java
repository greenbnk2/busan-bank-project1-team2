package kr.co.bnkfirst.dto;

import lombok.Data;

@Data
public class KftcBranchDTO {
    private String orgCode;     // 기관 코드
    private String brCode;      // 지점 코드
    private String brName;      // 지점명
    private String brAddress;   // 주소
    private String brTel;       // 전화번호
    private String latitude;    // 위도
    private String longitude;   // 경도
}
