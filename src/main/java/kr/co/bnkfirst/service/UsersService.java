package kr.co.bnkfirst.service;

import kr.co.bnkfirst.dto.UsersDTO;
import kr.co.bnkfirst.entity.Users;
import kr.co.bnkfirst.mapper.UsersMapper;
import kr.co.bnkfirst.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersMapper usersMapper;
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화기 주입
    private final UsersRepository usersRepository;

    // 로그인
    public UsersDTO login(String mId, String mPw) {
        log.info("Trying to login user: {}", mId);

        UsersDTO user = usersMapper.findByMid(mId);
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
        log.info("UsersService 반환값 체크: "+user.toString() );
        return user;
    }

    // 회원가입(info)
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

    // 아이디 중복확인(info)
    public boolean existsByMid(String mid) {
        return usersMapper.existsByMid(mid) > 0;
    }

    // 가입 후 사용자 다시 조회 (MDATE 가져오기)
    public UsersDTO findByMid(String mid) {
        return usersMapper.findByMid(mid);  // mapper 이미 있으므로 그대로 사용
    }

    // 아이디 찾기(findid)
    public String findIdByPhone(String name, String phone) {
        return usersMapper.findIdByPhone(name, phone);
    }
    public String findIdByEmail(String name, String email) {
        return usersMapper.findIdByEmail(name, email);
    }

    // 비밀번호 찾기(findpw)
    public String resetPasswordByPhone(String mid, String phone) {

        UsersDTO user = usersMapper.findByMidAndPhone(mid, phone);
        if (user == null) return null;

        // 임시 비밀번호 생성
        String tempPw = RandomStringUtils.randomAlphanumeric(10);

        String hashed = passwordEncoder.encode(tempPw);
        usersMapper.updatePassword(mid, hashed);

        return tempPw;
    }
    public String resetPasswordByEmail(String mid, String email) {

        UsersDTO user = usersMapper.findByMidAndEmail(mid, email);
        if (user == null) return null;

        String tempPw = RandomStringUtils.randomAlphanumeric(10);

        String hashed = passwordEncoder.encode(tempPw);
        usersMapper.updatePassword(mid, hashed);

        return tempPw;
    }

    // 금융인증서
    public Users save(UsersDTO dto) {

        Users entity = dto.toEntity();

        return usersRepository.save(entity);
    }

    public Users findById(int uid){

        return usersRepository.findById(uid)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 회원"));
    }
}