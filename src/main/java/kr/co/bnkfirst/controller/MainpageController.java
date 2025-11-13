package kr.co.bnkfirst.controller;

import kr.co.bnkfirst.dto.product.ProductDTO;
import kr.co.bnkfirst.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Slf4j
public class MainpageController {

    @Autowired
    private ProductService productService;

    @GetMapping({"/main/main","/"})
    public String mainpage() {
        return "/main/main";
    }

    @ResponseBody
    @GetMapping("/main/search")
    public List<ProductDTO> searchProducts(@RequestParam("keyword") String keyword) {
        log.info("검색요청 keyword = {}", keyword);

        List<ProductDTO> products = productService.searchProducts(keyword);

        log.info("검색 결과 개수 = {}", products.size());
        for (ProductDTO p : products) {
            log.info("상품명: {}, 유형: {}, 금리: {}~{}",
                    p.getPname(), p.getPtype(), p.getPbirate(), p.getPhirate());
        }
        return products;
    }

}
