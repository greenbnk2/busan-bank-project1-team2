package kr.co.bnkfirst.controller;

import jakarta.annotation.security.PermitAll;
import kr.co.bnkfirst.dto.product.ProductDTO;
import kr.co.bnkfirst.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/product/main")
    public String mainPage() {
        return "product/product_main";
    }

    @GetMapping("/product/list")
    public String listPage() {
        return "product/product_list";
    }

    @PermitAll
    @ResponseBody
    @GetMapping("/product/items")
    public ResponseEntity<Page<ProductDTO>> getItems(@RequestParam String sort,
                                                     @RequestParam(defaultValue = "1") int page,
                                                     @RequestParam(defaultValue = "6") int pageSize,
                                                     @RequestParam(required = false) String target,
                                                     @RequestParam(required = false) String join,
                                                     @RequestParam(required = false) String tax,
                                                     @RequestParam(required = false) String keyword) {
//        log.info("args = {}, {}, {}, {}, {}, {}. {}", sort, page, pageSize,target, join, tax, keyword);
        Pageable pageable = PageRequest.of(page-1,pageSize, Sort.by(sort).descending());
        Page<ProductDTO> products = productService.findProducts(sort, page, pageSize,target, join, tax, keyword);
        log.info("products total {}", products.getTotalElements());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/product/view")
    public String viewPage() {
        return "product/product_view";
    }

    @GetMapping("/product/details")
    public String getProductDetails(@RequestParam String pid, Model model) {
        return null;
    }

    @GetMapping("/product/insertInfo")
    public String insertInfoPage() {
        return "product/product_insert_info";
    }

    @GetMapping("/product/subCmpl")
    public String subCmplPage() {
        return "product/product_sub_cmpl";
    }
}
