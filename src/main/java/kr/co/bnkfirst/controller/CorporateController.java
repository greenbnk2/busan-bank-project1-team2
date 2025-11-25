package kr.co.bnkfirst.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class CorporateController {

    @GetMapping("/corporate/main")
    public String corporateMain(){
        return "corporate/corporate_main";
    }
}
