package kr.co.bnkfirst.mapper;

import kr.co.bnkfirst.dto.product.SlfcertDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface SlfcertMapper {
    public int saveSlfcert(SlfcertDTO slfcertDTO);
}
