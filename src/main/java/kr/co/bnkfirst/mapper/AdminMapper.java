package kr.co.bnkfirst.mapper;

import kr.co.bnkfirst.dto.PageRequestDTO;
import kr.co.bnkfirst.dto.UsersDTO;
import kr.co.bnkfirst.dto.product.ProductDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminMapper {

    // 상품 목록 출력
    public List<ProductDTO> selectAllProduct(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);
    public int selectCountTotal(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);


    // 유저 목록 출력
    public List<UsersDTO> selectAllUsers(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);
    public int selectCountTotalUsers(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);

    // 전체회원 수 출력
    public int countAllUsers();
    // 신규가입 수 출력(현재 시간으로부터 6개월까지)
    public int countSixMonthUsers();
    // 상태가 휴면인 회원 수 출력
    public int countWait();
    // 상태가 탈퇴인 회원 수 출력
    public int countWithdrawal();

    // 상품 삭제
    public void deleteByProduct(String pid);




}
