package kr.co.bnkfirst.service;

import kr.co.bnkfirst.dto.PageRequestDTO;
import kr.co.bnkfirst.dto.admin.PageResponseAdminProductDTO;
import kr.co.bnkfirst.dto.product.ProductDTO;
import kr.co.bnkfirst.mapper.AdminMapper;
import kr.co.bnkfirst.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    private final AdminMapper adminMapper;

    // 상품 목록 페이지네이션
    public PageResponseAdminProductDTO selectAllProduct(PageRequestDTO pageRequestDTO) {
        // MyBatis 처리
        List<ProductDTO> dtoList = adminMapper.selectAllProduct(pageRequestDTO);

        int total = adminMapper.selectCountTotal(pageRequestDTO);

        return PageResponseAdminProductDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    public int selectCountTotal(PageRequestDTO pageRequestDTO) {
        return adminMapper.selectCountTotal(pageRequestDTO);
    }

    // 상품 삭제

    public void deleteByProduct(String pid){
        adminMapper.deleteByProduct(pid);
    }

}
