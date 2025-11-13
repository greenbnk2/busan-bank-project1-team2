package kr.co.bnkfirst.service;

import kr.co.bnkfirst.dto.UsersDTO;
import kr.co.bnkfirst.mapper.UsersMapper;
import kr.co.bnkfirst.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersMapper usersMapper;

    // member_main 로그인
    public UsersDTO login(String mId, String mPw) {
        log.info("Trying to login user: {}", mId);

        // 쿼리로 사용자 조회
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

    // member_info 정보입력
    public boolean register(UsersDTO dto) {
        log.info("회원가입 시도: {}", dto.getMid());
        try {
            int result = usersMapper.insertUser(dto);
            return result == 1;
        } catch (Exception e) {
            log.error("회원가입 중 오류 발생", e);
            return false;
        }
    }

    // member_info 아이디 중복확인
    public boolean existsByMid(String mid) {
        return usersMapper.existsByMid(mid) > 0;
    }

}
