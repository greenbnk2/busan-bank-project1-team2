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


    // 상품 삭제
    public void deleteByProduct(String pid);




}
