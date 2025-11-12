package kr.co.bnkfirst.service;

import kr.co.bnkfirst.dto.UsersDTO;
import kr.co.bnkfirst.mapper.UsersMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersMapper usersMapper;

    /*
     * 로그인 로직
     * @param mId 아이디
     * @param mPw 비밀번호
     * @return 로그인 성공 시 UsersDTO, 실패 시 null
     */
    public UsersDTO login(String mId, String mPw) {
        log.info("Trying to login user: {}", mId);

        // MyBatis 쿼리로 사용자 조회
        UsersDTO user = usersMapper.findUserById(mId);

        // 결과 확인
        if (user == null) {
            log.warn("No user found for ID: {}", mId);
            return null;
        }

        // 비밀번호 검증
        if (!user.getMpw().equals(mPw)) {
            log.warn("Password mismatch for ID: {}", mId);
            return null;
        }

        log.info("Login success for ID: {}", mId);
        return user;

    }
}
