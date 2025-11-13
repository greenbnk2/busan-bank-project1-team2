package kr.co.bnkfirst.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.bnkfirst.dto.UsersDTO;
import kr.co.bnkfirst.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
        session.setMaxInactiveInterval(600);
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

    // 로그아웃 기능(세션 삭제)
    @GetMapping("/logout")
    @ResponseBody
    public void logout(HttpSession session) {
        session.invalidate();
    }

    @GetMapping("/terms")
    public String memberTerms() {
        return "member/member_terms";
    }

    @GetMapping("/auth")
    public String memberAuth() {
        return "member/member_auth";
    }

    @GetMapping("/info")
    public String memberInfo() {
        return "member/member_info";
    }

    // 회원가입 데이터 저장
    @PostMapping("/insert")
    public String insert(@ModelAttribute UsersDTO usersDTO, HttpSession session) {
        boolean result = usersService.register(usersDTO);

        if (result) {
            session.setAttribute("newUser", usersDTO);
            return "redirect:active";
        } else {
            return "redirect:info";
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

        model.addAttribute("member", newUser);  // DTO 그대로 전달
        model.addAttribute("xml", xml);         // XML 별도 전달

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

    /* AWS prod 시스템함수 추가 후 진행(이메일 인증)
    @RestController
    @RequiredArgsConstructor
    public class MailTestController {

        private final JavaMailSender mailSender;

        @GetMapping("/test-mail")
        public String testMail() {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("받을이메일@gmail.com");
            message.setSubject("테스트 메일");
            message.setText("메일 발송 테스트 성공!");

            mailSender.send(message);
            return "메일 발송 완료!";
        }
    }
     */
}
