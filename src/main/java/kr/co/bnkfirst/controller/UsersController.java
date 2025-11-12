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

        // 로그인 성공 시 세션 저장 + 10분 유지
        session.setAttribute("loginUser", foundUser);
        session.setMaxInactiveInterval(600); // 600초 = 10분
        log.info("로그인 성공: {}", foundUser.getMid());

        return "redirect:/member/main";
    }

    // 로그인 메인 페이지
    @GetMapping("/main")
    public String memberMain(Model model, HttpSession session) {
        UsersDTO loginUser = (UsersDTO) session.getAttribute("loginUser");

        // 로그인 안 되어 있어도 메인 페이지 유지
        if (loginUser == null) {
            log.info("비로그인 접근");
            model.addAttribute("userDTO", new UsersDTO());
        } else {
            model.addAttribute("loginUser", loginUser);
        }
        model.addAttribute("userDTO", new UsersDTO());
        return "member/member_main";
    }

    @GetMapping("/logout")
    @ResponseBody
    public void logout(HttpSession session) {
        session.invalidate(); // 세션 전체 삭제
    }

    @GetMapping("/terms")
    public String memberTerms() {
        return "member/member_terms";
    }

    @GetMapping("/info")
    public String memberInfo() {
        return "member/member_info";
    }

    @GetMapping("/auth")
    public String memberAuth() {
        return "member/member_auth";
    }

    @GetMapping("/active")
    public String memberActive() {
        return "member/member_active";
    }

    @GetMapping("/findid")
    public String memberFindid() {
        return "member/member_findid";
    }

    @GetMapping("/findpw")
    public String memberFindpw() {
        return "member/member_findpw";
    }
}
