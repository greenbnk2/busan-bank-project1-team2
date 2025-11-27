package kr.co.bnkfirst.service;

import kr.co.bnkfirst.dto.product.FundDTO;
import kr.co.bnkfirst.dto.product.PcontractDTO;
import kr.co.bnkfirst.mapper.MypageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockService {

    private final MypageMapper mypageMapper;

    public List<PcontractDTO> findByContract(String mid){
        return mypageMapper.findByContract(mid);
    }
}
