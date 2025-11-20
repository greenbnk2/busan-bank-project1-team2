package kr.co.bnkfirst.controller;

import jakarta.annotation.security.PermitAll;
import kr.co.bnkfirst.dto.product.PcontractDTO;
import kr.co.bnkfirst.dto.product.ProductDTO;
import kr.co.bnkfirst.dto.product.SlfcertDTO;
import kr.co.bnkfirst.service.ProductService;
import kr.co.bnkfirst.service.SlfcertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final SlfcertService slfcertService;

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

    @GetMapping("/product/view/{pid}")
    public String viewPage(@PathVariable String pid, Model model) {
        model.addAttribute("pid", pid);
        return "product/product_view";
    }

    @ResponseBody
    @GetMapping("/product/details/{pid}")
    public ResponseEntity<Optional<ProductDTO>> getProductDetails(@PathVariable String pid, Model model) {
        Optional<ProductDTO> dtoOptional = productService.findProductByPid(pid);
        if(dtoOptional.isPresent()) {
            return ResponseEntity.ok(dtoOptional);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/product/insertInfo")
    public String insertInfoPage() {
        return "product/product_insert_info";
    }

    @PostMapping("/product/slfcert")
    public ResponseEntity<SlfcertDTO> slfcertForm(SlfcertDTO slfcertDTO) {
        log.info("slfcert {}", slfcertDTO);
        // 로그인 기능 구현 전까지 임시 데이터 주입
        String cusid = "a123";
        slfcertDTO.setCusid(cusid);
        boolean isSaved = slfcertService.saveSlfcert(slfcertDTO);
        if(isSaved) {
            return ResponseEntity.ok(slfcertDTO);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Bad Request : 400
    }

    @GetMapping("/product/subCmpl/list")
    public String subCmplPage(Model model) {
        model.addAttribute("mid", "a123");
        model.addAttribute("pcpid", "BNK-TD-1");
        return "product/product_sub_cmpl";
    }


    @GetMapping("/product/subCmpl")
    public ResponseEntity<PcontractDTO> subCmplPage(
                                                    @RequestParam("mid") String mid,
                                                    @RequestParam("pcpid") String pcpid) {

        return ResponseEntity.ok(productService.resultPcontract(mid, pcpid));
    }
}
