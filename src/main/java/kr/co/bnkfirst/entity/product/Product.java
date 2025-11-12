package kr.co.bnkfirst.entity.product;

import jakarta.persistence.*;
import kr.co.bnkfirst.dto.product.ProductDTO;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 500)
    private String pid;
    @Nationalized
    @Column(length = 500)
    private String ptype;
    @Nationalized
    @Column(length = 500)
    private String pname;
    @Column(name = "PBIRATE", columnDefinition = "NUMBER")
    private float pbirate;
    @Column(name = "PHIRATE", columnDefinition = "NUMBER")
    private float phirate;
    @Nationalized
    @Column(length = 100)
    private String pcprd;
    @Nationalized
    @Column(length = 500)
    private String pelgbl;
    @Nationalized
    @Column(length = 500)
    private String prmthd;
    @Nationalized
    @Column(length = 500)
    private String pprfcrt;
    @Lob
    private String pirinfo;
    private String pttitle;
    private String ptlink;
    @Nationalized
    @Column(length = 100)
    private String pcond;

    @CreationTimestamp
    private LocalDateTime pupdate;

    public ProductDTO toDTO(){
        return ProductDTO.builder()
                .id(id)
                .pid(pid)
                .ptype(ptype)
                .pname(pname)
                .pbirate(pbirate)
                .phirate(phirate)
                .pcprd(pcprd)
                .pelgbl(pelgbl)
                .prmthd(prmthd)
                .pprfcrt(pprfcrt)
                .pirinfo(pirinfo)
                .pttitle(pttitle)
                .ptlink(ptlink)
                .pcond(pcond)
                .pupdate(pupdate.toString())
                .build();
    }
}
