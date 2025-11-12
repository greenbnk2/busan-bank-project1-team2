package kr.co.bnkfirst.entity;

import jakarta.persistence.*;
import kr.co.bnkfirst.dto.UsersDTO;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int uid;
    private String mid;
    private String mpw;
    private String mname;
    @CreationTimestamp
    private LocalDateTime mbirth;
    private String mgender;
    private String mcarrier;
    private String memail;
    private String mphone;
    @CreationTimestamp
    private LocalDateTime mdate;
    private String mgrade;
    private String mjumin;
    private String mcond;
    private String mnum;
    @CreationTimestamp
    private LocalDateTime maccess;
    private String mlimit;

    /*
        시간 남으면 DB 추가 후 출력 예정
        진행시 패널 형식x → 각 약관을 버튼으로 바꾼 후 누르면 모달창 형식으로 변경
        ↓ 원래 하드 코딩으로 예정
     */
    private String mtitle;
    private String mcontent;

    public UsersDTO toDTO(){
        return UsersDTO.builder()
                .uid(uid)
                .mid(mid)
                .mpw(mpw)
                .mname(mname)
                .mbirth(mbirth)
                .mgender(mgender)
                .mcarrier(mcarrier)
                .memail(memail)
                .mphone(mphone)
                .mdate(mdate)
                .mgrade(mgrade)
                .mjumin(mjumin)
                .mcond(mcond)
                .mnum(mnum)
                .maccess(maccess)
                .mlimit(mlimit)
                .mtitle(mtitle)
                .mcontent(mcontent)
                .build();
    }

}
