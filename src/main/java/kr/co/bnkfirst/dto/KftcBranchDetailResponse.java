package kr.co.bnkfirst.dto;

import lombok.Data;

@Data
public class KftcBranchDetailResponse {

    private String trms_org_code;
    private String brch_code;
    private String dup_brch_code;

    private String brch_name;
    private String addr;
    private String tel;
    private String fax;

    private String open_time;
    private String close_time;

    private String latitude;
    private String longitude;
}
