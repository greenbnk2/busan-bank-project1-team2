package kr.co.bnkfirst.mapper;

import kr.co.bnkfirst.dto.product.FundDTO;
import kr.co.bnkfirst.dto.product.PcontractDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {

    public PcontractDTO resultPcontract(String mid, String pcpid);
    public List<FundDTO> selectFund();
    public FundDTO selectFundDetail(@Param("fid") String fid);

    public PcontractDTO selectAllByUidAndType(@Param("pcuid") String pcuid, @Param("type") String type);
}
