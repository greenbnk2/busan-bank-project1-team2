package kr.co.bnkfirst.dto;
import lombok.Data;

@Data
public class KftcBranchDetailRequest {
    private String trms_org_code;
    private String brch_code;
    private String dup_brch_code;
}
