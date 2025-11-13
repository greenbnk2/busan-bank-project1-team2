package kr.co.bnkfirst.mapper;

import kr.co.bnkfirst.dto.UsersDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UsersMapper {
    UsersDTO findUserById(@Param("mid") String mid);

    // 정보입력(info) insert
    int insertUser(UsersDTO user);

    // 정보입력(info) 아이디 중복확인
    int existsByMid(@Param("mid") String mid);


}
