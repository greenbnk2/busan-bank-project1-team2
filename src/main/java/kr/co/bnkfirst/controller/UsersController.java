package kr.co.bnkfirst.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.bnkfirst.dto.UsersDTO;
import kr.co.bnkfirst.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class UsersController {

    private final UsersService usersService;

    // 로그인 처리
    @PostMapping("/main")
    public String login(@ModelAttribute UsersDTO userDTO, HttpSession session) {
        UsersDTO foundUser = usersService.login(userDTO.getMid(), userDTO.getMpw());

        if (foundUser == null) {
            log.warn("로그인 실패 - 아이디 또는 비밀번호 오류");
            return "redirect:/member/main";
        }

        // 로그인 성공 시 세션 저장 + 20분 유지
        session.setAttribute("loginUser", foundUser);
        session.setMaxInactiveInterval(1200);
        session.setAttribute("sessionStart", System.currentTimeMillis());

        log.info("로그인 성공: {}", foundUser.getMid());

        return "redirect:/main/main";
    }

    // 로그인 메인 페이지
    @GetMapping("/main")
    public String memberMain(Model model, HttpSession session) {
        UsersDTO loginUser = (UsersDTO) session.getAttribute("loginUser");

        // 로그인 후 login 쪽에 머무를 수 없도록 설정
        if (loginUser != null) {
            return "redirect:/main/main";
        }

        // 로그인 안 한 경우에만 로그인 화면 보여줌
        model.addAttribute("userDTO", new UsersDTO());
        return "member/member_main";
    }

    // 로그아웃 기능(세션 삭제)
    @GetMapping("/logout")
    @ResponseBody
    public void logout(HttpSession session) {
        UsersDTO loginUser = (UsersDTO) session.getAttribute("loginUser");

        if (loginUser == null) {
            log.info("비로그인 상태에서 로그아웃 접근 발생");
            return;
        }
        log.info("로그아웃 성공 : {}", loginUser.getMid());

        session.invalidate();
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

}
