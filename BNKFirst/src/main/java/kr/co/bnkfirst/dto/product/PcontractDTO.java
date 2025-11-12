package kr.co.bnkfirst.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PcontractDTO {
    private String pcuid;
    private String pcpid;
    private String pccptp;
    private String pccprd;
    private String pcwcat;
    private String pcwdac;
    private int pcmdps;
    private int pcgamn;
    private String pcatapp;
    private int pcatdt;
    private String pcatac;
    private String pccns;
    private String pcntcs;
    private String pcnapw;
    private String pacc;
    @CreationTimestamp
    private LocalDateTime pnew;
    @CreationTimestamp
    private LocalDateTime pend;
    private int pbalance;

    // 추가 컬럼 - 손진일
    private String pname;
    private String ptype;
    private float phirate;
}
