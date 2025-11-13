package kr.co.bnkfirst.service;

import kr.co.bnkfirst.dto.PageRequestDTO;
import kr.co.bnkfirst.dto.UsersDTO;
import kr.co.bnkfirst.dto.admin.PageResponseAdminProductDTO;
import kr.co.bnkfirst.dto.admin.PageResponseAdminUsersDTO;
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

    public void deleteByProduct(String pid) {
        adminMapper.deleteByProduct(pid);
    }

    // 유저 목록 페이지네이션
    public PageResponseAdminUsersDTO selectAllUsers(PageRequestDTO pageRequestDTO) {
        // MyBatis 처리
        List<UsersDTO> dtoList = adminMapper.selectAllUsers(pageRequestDTO);

        int total = adminMapper.selectCountTotalUsers(pageRequestDTO);

        return PageResponseAdminUsersDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    public int selectCountTotalUsers(PageRequestDTO pageRequestDTO) {
        return adminMapper.selectCountTotalUsers(pageRequestDTO);
    }





    // 전체회원 수 출력
    public int countAllUsers(){
        return adminMapper.countAllUsers();
    };
    // 신규가입 수 출력(현재 시간으로부터 6개월까지)
    public int countSixMonthUsers(){
        return adminMapper.countSixMonthUsers();
    };
    // 상태가 휴면인 회원 수 출력
    public int countWait(){
        return adminMapper.countWait();
    };
    // 상태가 탈퇴인 회원 수 출력
    public int countWithdrawal(){
        return adminMapper.countWithdrawal();
    };

}