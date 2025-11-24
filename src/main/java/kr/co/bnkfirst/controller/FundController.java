package kr.co.bnkfirst.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class FundController {

    @GetMapping("/product/fund/list")
    public String fundList(){
        return "fund/fund_list";
    }

    @GetMapping("/product/TDF/list")
    public String TDFList(){
        return "fund/TDF_list";
    }

    @GetMapping("/product/ETF/list")
    public String ETFList(){
        return "fund/ETF_list";
    }
}
