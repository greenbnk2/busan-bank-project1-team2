package kr.co.bnkfirst.dto.product;

import kr.co.bnkfirst.entity.product.Product;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProductDTO {
    private int id;
    private String pid;
    private String ptype;
    private String pname;
    private float pbirate;
    private float phirate;
    private String pcprd;
    private String pelgbl;
    private String prmthd;
    private String pprfcrt;
    private String pirinfo;
    private String pttitle;
    private String ptlink;
    private String pcond;
    private String pupdate;

    public Product toEntity(){
        return Product.builder()
                .id(id)
                .pid(pid)
                .ptype(ptype)
                .pname(pname)
                .pbirate(pbirate)
                .phirate(phirate)
                .pcprd(pcprd)
                .pelgbl(pelgbl)
                .pprfcrt(pprfcrt)
                .pirinfo(pirinfo)
                .pttitle(pttitle)
                .ptlink(ptlink)
                .pcond(pcond)
                .build();
    }
}
