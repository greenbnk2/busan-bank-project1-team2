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

    private int brid;
    private String brname;
    private String braddr;
    private String brtel;
    private String brfax;

}
