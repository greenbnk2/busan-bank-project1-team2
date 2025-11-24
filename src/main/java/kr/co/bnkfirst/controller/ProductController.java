package kr.co.bnkfirst.controller;

import jakarta.annotation.security.PermitAll;
import kr.co.bnkfirst.dto.UsersDTO;
import kr.co.bnkfirst.dto.product.PcontractDTO;
import kr.co.bnkfirst.dto.product.ProductDTO;
import kr.co.bnkfirst.dto.product.SlfcertDTO;
import kr.co.bnkfirst.service.MypageService;
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
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

/*
    이름 : 강민철, 손진일
    날짜 : 2025.11.21.
    내용 : 상품 페이지 컨트롤러
 */

@Slf4j
@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final SlfcertService slfcertService;
    private final MypageService mypageService;

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

    @GetMapping("/product/insertInfo/{pid}")
    public String insertInfoPage(Model model, Principal principal, @PathVariable String pid) {
        if (principal == null) {
            throw new ErrorResponseException(HttpStatus.FORBIDDEN); // 비로그인시 403
        }
        String mid = principal.getName();
        log.info("mid {}", mid);
        boolean isExist = slfcertService.countSlfcertByMid(mid);
        UsersDTO userInfo = mypageService.findById(mid);
        model.addAttribute("mid", mid);
        model.addAttribute("pid", pid);
        model.addAttribute("hasInfo", isExist ? "true" : "false");
        model.addAttribute("userInfo", userInfo);
        return "product/product_insert_info";
    }

    @PostMapping("/api/slfcert")
    public ResponseEntity<SlfcertDTO> slfcertForm(SlfcertDTO slfcertDTO, Principal principal) {
        log.info("slfcert {}", slfcertDTO);
        String cusid = principal.getName();
        log.info("cusid {}", cusid);
        slfcertDTO.setCusid(cusid);
        boolean isExist = slfcertService.countSlfcertByMid(cusid);
        if (isExist) {
            log.info("slfcert already exist");
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // Conflict : 409
        }
        SlfcertDTO saved = slfcertService.saveSlfcert(slfcertDTO);
        if(saved == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else if (saved.getCusid().equals(cusid)) {
            return ResponseEntity.ok(slfcertDTO);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Bad Request : 400
    }

    @GetMapping("/api/slfcert/{mid}")
    public ResponseEntity<Void> chkSlfcertExist(@PathVariable String mid) {
        // 로그인 기능 구현 전까지 임시 데이터 주입
        boolean exists = slfcertService.countSlfcertByMid(mid);
        return exists ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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
