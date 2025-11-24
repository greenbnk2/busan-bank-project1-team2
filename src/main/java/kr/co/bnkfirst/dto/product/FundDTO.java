package kr.co.bnkfirst.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FundDTO {

    private String fid;
    private String fname;
    private String famc;
    private int frlvl;
    private String ftype;
    private float frefpr;
    private String fsetdt;
    private float ftc;
    private float fm1pr;
    private float fm3pr;
    private float fm6pr;
    private float fm12pr;
    private float facmpr;

}
