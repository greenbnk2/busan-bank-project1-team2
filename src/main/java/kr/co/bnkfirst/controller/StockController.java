package kr.co.bnkfirst.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class StockController {

    @GetMapping("/stock/main")
    public String mainPage() {
        return "stock/stock_main";
    }

    @GetMapping("/stock/order")
    public String orderPage() {
        return "stock/stock_order";
    }
}
