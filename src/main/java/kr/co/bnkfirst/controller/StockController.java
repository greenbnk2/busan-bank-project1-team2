package kr.co.bnkfirst.controller;

import kr.co.bnkfirst.kiwoomRank.StockRankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class StockController {

    private final StockRankingService rankingService;

    @GetMapping("/stock/main")
    public String mainPage(Model model) {

        model.addAttribute("ranks",
                rankingService.getTopByTradingValue(10));
        log.info("rank = {}", rankingService.getTopByTradingValue(10));
        return "stock/stock_main";
    }

    @GetMapping("/stock/order")
    public String stockOrder(@RequestParam("code") String code,
                             @RequestParam(value = "name", required = false) String name,
                             Model model) {

        // name을 안 넘겨줬으면 code를 그냥 이름처럼 보여주도록 임시 처리
        String stockName = (name != null && !name.isBlank()) ? name : code;

        model.addAttribute("code", code);
        model.addAttribute("stockName", stockName);

        return "stock/stock_order";   // 템플릿 경로에 맞게
    }
}
