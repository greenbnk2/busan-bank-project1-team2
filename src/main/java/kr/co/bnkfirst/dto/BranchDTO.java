package kr.co.bnkfirst.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchDTO {

    // ====== 내부 DB용 =====
    private int brid;           // 내부 지점 ID
    private String brname;      // 내부 지점명
    private String braddr;      // 내부 주소
    private String brtel;       // 내부 전화번호
    private String brfax;       // 내부 팩스

    // ==== KFTC API 용 ====
    private String bankCode;
    private String bankName;
    private String branchCode;
    private String branchName;
    private String address;     // 외부 주소
    private String tel;         // 외부 전화번호
    private String latitude;
    private String longitude;


}
