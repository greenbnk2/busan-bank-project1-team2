package kr.co.bnkfirst.service;

import kr.co.bnkfirst.dto.UsersDTO;
import kr.co.bnkfirst.mapper.UsersMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersMapper usersMapper;
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화기 주입

    // 로그인
    public UsersDTO login(String mId, String mPw) {
        log.info("Trying to login user: {}", mId);

        UsersDTO user = usersMapper.findUserById(mId);
        if (user == null) {
            log.warn("No user found for ID: {}", mId);
            return null;
        }

        // 암호화된 비밀번호 검증 추가
        String storedPw = user.getMpw();
        boolean matches;

        if (storedPw.startsWith("$2a$") || storedPw.startsWith("$2b$")) {
            matches = passwordEncoder.matches(mPw, storedPw);
        } else {
            // 기존 DB 평문 비밀번호용 (초기데이터 호환)
            matches = mPw.equals(storedPw);
        }

        if (!matches) {
            log.warn("Password mismatch for ID: {}", mId);
            return null;
        }

        log.info("Login success for ID: {}", mId);
        return user;
    }

    // 회원가입(member_info)
    public boolean register(UsersDTO dto) {
        log.info("회원가입 시도: {}", dto.getMid());
        try {
            // 비밀번호 암호화 추가
            String encodedPw = passwordEncoder.encode(dto.getMpw());
            dto.setMpw(encodedPw);

            int result = usersMapper.insertUser(dto);
            return result == 1;
        } catch (Exception e) {
            log.error("회원가입 중 오류 발생", e);
            return false;
        }
    }

    // 아이디 중복확인(member_info)
    public boolean existsByMid(String mid) {
        return usersMapper.existsByMid(mid) > 0;
    }

    public UsersDTO findByMid(String mid) {
        return usersMapper.findUserById(mid);
    }
}
