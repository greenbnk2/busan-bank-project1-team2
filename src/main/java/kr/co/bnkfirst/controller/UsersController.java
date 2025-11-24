package kr.co.bnkfirst.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.bnkfirst.dto.FinanceCertResult;
import kr.co.bnkfirst.dto.UsersDTO;
import kr.co.bnkfirst.entity.Users;
import kr.co.bnkfirst.jwt.JwtProvider;
import kr.co.bnkfirst.service.FinanceCertService;
import kr.co.bnkfirst.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class UsersController {
    private final JwtProvider jwtProvider;
    private final UsersService usersService;
    private final FinanceCertService financeCertService;

    /*
        날짜 : 2025/11/21
        이름 : 이준우
        내용 : 로그인 세션 + jwt 하이브리드 방식 변경
     */

    @PostMapping("/login")
    public String login(
            @RequestParam String mid,
            @RequestParam String mpw,
            HttpSession session
    ) {

        log.info("로그인 시도 ID: {}", mid);

        UsersDTO dto = usersService.login(mid, mpw);
        log.info("비밀번호 암호화 체크 : "+mpw);
        log.info("dto 체크 : "+dto);

        if (dto == null) {
            log.warn("로그인 실패 - 존재하지 않거나 비밀번호 불일치: {}", mid);
            return "redirect:/member/main?error=fail";
        }

        // JWT 생성
        Users user = dto.toEntity();
        String role = dto.getRole();

        String token = jwtProvider.createToken(user, role);
        log.info("token: {}", token);

        // 세션 저장
        session.setAttribute("jwtToken", token);
        session.setAttribute("loginUser", dto);

        long now = System.currentTimeMillis();
        session.setAttribute("sessionStart", now);
        session.setMaxInactiveInterval(1200);

        log.info("로그인 성공 - ID: {}, JWT 발급됨, 세션 시작시간: {}", mid, now);

        return "redirect:/main/main";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        Object userObj = session.getAttribute("loginUser");

        if (userObj != null) {
            UsersDTO user = (UsersDTO) userObj;
            log.info("사용자 로그아웃: {}", user.getMid());
        } else {
            log.info("로그아웃 요청: 로그인된 사용자 없음");
        }

        session.invalidate(); // 세션 삭제
//        log.info("세션 체크: {}", session.getAttribute("loginUser"));
        return "redirect:/member/main";
    }

    @GetMapping("/main")
    public String memberMain(Model model, HttpSession session) {

        UsersDTO loginUser = (UsersDTO) session.getAttribute("loginUser");

        if (loginUser != null) {
            return "redirect:/main/main";
        }

        model.addAttribute("userDTO", new UsersDTO());

        return "member/member_main";
    }

    @GetMapping("/terms")
    public String memberTerms() {
        return "member/member_terms";
    }

    @PostMapping("/auth/save")
    public String authSave(@ModelAttribute UsersDTO dto, HttpSession session) {

        // 세션 저장
        session.setAttribute("authData", dto);

        return "redirect:/member/info";
    }

    @GetMapping("/auth")
    public String memberAuth(HttpSession session, HttpServletRequest request) {
        String referer = request.getHeader("Referer");

        if (referer == null || !referer.contains("/member/terms")) {

            // terms 넘어온 게 아니면 차단
            return "redirect:/member/terms";
        }
        return "member/member_auth";
    }

    @GetMapping("/info")
    public String memberInfo(HttpSession session, Model model) {
        // 인증 단계 건너뛰기 방지
        UsersDTO authData = (UsersDTO) session.getAttribute("authData");
        if (authData == null) {
            return "redirect:/member/auth";
        }

        model.addAttribute("auth", authData);
        return "member/member_info";
    }

    // 회원가입 데이터 저장
    @PostMapping("/insert")
    public String insert(@ModelAttribute UsersDTO usersDTO, HttpSession session) {
        boolean result = usersService.register(usersDTO);

        if (result) {
            UsersDTO savedUser = usersService.findByMid(usersDTO.getMid());
            session.setAttribute("newUser", savedUser);

            // 인증 정보 소멸
            session.removeAttribute("authData");

            return "redirect:/member/active";
        } else {
            return "redirect:/member/info";
        }
    }

    @GetMapping("/id-check")
    @ResponseBody
    public boolean idCheck(@RequestParam String mid) {
        return usersService.existsByMid(mid);
    }

    @GetMapping("/active")
    public String memberActive(HttpSession session, Model model) {

        UsersDTO newUser = (UsersDTO) session.getAttribute("newUser");

        if (newUser == null) {
            return "redirect:/member/info";
        }
        // XML로 추가 정보만 전달 (SYSDATE, Family)
        String xml =
                "<extra>" +
                        "   <grade>Family</grade>" +
                        "   <joinDate>SYSDATE</joinDate>" +
                        "</extra>";

        model.addAttribute("member", newUser);
        model.addAttribute("xml", xml);

        // 회원가입 완료 후 세션 초기화
        session.removeAttribute("newUser");

        return "member/member_active";
    }

    // findId
    @PostMapping("/findid/phone")
    @ResponseBody
    public Map<String, Object> findIdByPhone(@RequestBody Map<String, String> request) {

        String name = request.get("name");
        String phone = request.get("phone");

        String mid = usersService.findIdByPhone(name, phone);

        if (mid == null) {
            return Map.of("ok", false,"message", "해당 휴대폰 번호로 가입된 아이디가 없습니다.");
        }

        return Map.of("ok", true,"mid", mid);
    }

    @PostMapping("/findid/email")
    @ResponseBody
    public Map<String, Object> findIdByEmail(@RequestBody Map<String, String> request) {

        String name = request.get("name");
        String email = request.get("email");

        String mid = usersService.findIdByEmail(name, email);

        if (mid == null) {
            return Map.of("ok", false, "message", "해당 정보와 일치하는 아이디가 없습니다.");
        }

        return Map.of("ok", true, "mid", mid);
    }

    @GetMapping("/findid")
    public String memberFindid() {
        return "member/member_findid";
    }

    // findpw
    @PostMapping("/findpw/phone")
    @ResponseBody
    public Map<String, Object> findPwByPhone(@RequestBody Map<String, String> request) {

        String mid = request.get("mid");
        String phone = request.get("phone");

        String tempPw = usersService.resetPasswordByPhone(mid, phone);

        if(tempPw == null) {
            return Map.of("ok", false, "message", "일치하는 회원이 없습니다");
        }
        return Map.of("ok", true,"tempPw", tempPw);
    }

    @PostMapping("/findpw/email")
    @ResponseBody
    public Map<String, Object> findPwByEmail(@RequestBody Map<String, String> request) {

        String mid = request.get("mid");
        String email = request.get("email");

        String tempPw = usersService.resetPasswordByEmail(mid, email);

        if(tempPw == null) {
            return Map.of("ok", false, "message", "일치하는 회원이 없습니다");
        }
        return Map.of("ok", true,"tempPw", tempPw);
    }

    @GetMapping("/findpw")
    public String memberFindpw() {
        return "member/member_findpw";
    }

    // 현재 세션 남은 시간 조회
    @GetMapping("/session/remaining")
    @ResponseBody
    public Map<String, Object> getRemaining(HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) {
            result.put("remainSeconds", 0);
            return result;
        }
        Long startTime = (Long) session.getAttribute("sessionStart");
        if (startTime == null) {
            // 혹시 null이면 다시 저장 (예외 처리용)
            startTime = System.currentTimeMillis();
            session.setAttribute("sessionStart", startTime);
        }
        long now = System.currentTimeMillis();
        long passed = (now - startTime) / 1000;
        long remain = 1200 - passed;
        if (remain < 0) remain = 0;

        result.put("remainSeconds", remain);
        return result;
    }

    // 세션 연장 (다시 20분으로 리셋)
    @PostMapping("/session/extend")
    @ResponseBody
    public Map<String, Object> extend(HttpSession session) {

        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) {
            return Map.of("remainSeconds", 0);
        }
        session.setAttribute("sessionStart", System.currentTimeMillis());
        session.setMaxInactiveInterval(1200);

        return getRemaining(session);
    }

    // 금융인증서 11월19일 추가
    @PostMapping("/api/finance-cert/start")
    @ResponseBody
    public Map<String, String> startFinanceCert(HttpSession session) {

        String txId = UUID.randomUUID().toString();
        session.setAttribute("FIN_TX_ID", txId);

        String redirectUrl = financeCertService.buildAuthUrl(txId);

        return Map.of("redirectUrl", redirectUrl);
    }

    @GetMapping("/auth/finance-cert/callback")
    @ResponseBody
    public Map<String, Object> financeCertCallback(
            @RequestParam("tx_id") String txId,
            @RequestParam("code") String code,
            HttpSession session
    ) {
        String savedTxId = (String) session.getAttribute("FIN_TX_ID");
        if (savedTxId == null || !savedTxId.equals(txId)) {
            return Map.of("ok", false, "message", "잘못된 인증 요청입니다.");
        }

        FinanceCertResult r = financeCertService.fetchResult(txId, code);

        session.setAttribute("FIN_USER", r);

        return Map.of(
                "ok", true,
                "name", r.getName(),
                "birth", r.getBirth(),
                "gender", r.getGender(),
                "carrier", r.getCarrier(),
                "phone", r.getPhone(),
                "ci", r.getCi()
        );
    }
}
