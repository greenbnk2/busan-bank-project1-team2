package kr.co.bnkfirst.dto;
import lombok.Data;

@Data
public class KftcBranchDetailResponse {

    private String brch_name;
    private String brch_addr;
    private String telno;
    private String faxno;
    private String opentime;
    private String closetime;
    private String mapx;
    private String mapy;

}
