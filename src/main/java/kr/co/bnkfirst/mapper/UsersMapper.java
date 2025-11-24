package kr.co.bnkfirst.mapper;

import kr.co.bnkfirst.dto.UsersDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UsersMapper {
    UsersDTO findByMid(@Param("mid") String mid);

    // 정보입력(info) insert
    int insertUser(UsersDTO user);

    // 정보입력(info) 아이디 중복확인
    int existsByMid(@Param("mid") String mid);

    // 아이디 찾기(findid) Phone & Email
    String findIdByPhone(@Param("name") String name, @Param("phone") String phone);
    String findIdByEmail(@Param("name") String name, @Param("email") String email);

    // 비밀번호 찾기(findpw) Phone & Email
    UsersDTO findByMidAndPhone(@Param("mid") String mid, @Param("phone") String phone);
    UsersDTO findByMidAndEmail(@Param("mid") String mid, @Param("email") String email);

    int updatePassword(@Param("mid") String mid, @Param("mpw") String mpw);
}