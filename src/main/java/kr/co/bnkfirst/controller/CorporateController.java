package kr.co.bnkfirst.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class CorporateController {

    // 메인 페이지
    @GetMapping("/corporate/main")
    public String main() {
        return "corporate/main/corporate_main";
    }

    // 고객센터
    @GetMapping("/corporate/cs")
    public String cs() {
        return "corporate/cs/cs_main";
    }

}