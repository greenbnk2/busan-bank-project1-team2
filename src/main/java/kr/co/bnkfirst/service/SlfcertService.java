package kr.co.bnkfirst.service;

import kr.co.bnkfirst.dto.product.SlfcertDTO;
import kr.co.bnkfirst.mapper.SlfcertMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SlfcertService {
    private final SlfcertMapper slfcertMapper;

    public boolean saveSlfcert(SlfcertDTO slfcertDTO){
        if (slfcertDTO.getNatcd().equals("US"))
            slfcertDTO.setFtype("W9");
        else {
            slfcertDTO.setFtype("W8");
        }
        return slfcertMapper.saveSlfcert(slfcertDTO) == 1;
    }
}
