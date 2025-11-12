package kr.co.bnkfirst.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class MainpageController {

    @GetMapping("/main/main")
    public String mainpage() {
        return "/main/main";
    }

}
