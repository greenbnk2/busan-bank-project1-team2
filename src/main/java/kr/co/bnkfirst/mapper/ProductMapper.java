package kr.co.bnkfirst.mapper;

import kr.co.bnkfirst.dto.product.PcontractDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper {

    public PcontractDTO resultPcontract(String mid, String pcpid);
}
