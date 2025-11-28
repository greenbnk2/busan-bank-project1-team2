package kr.co.bnkfirst.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.bnkfirst.dto.MydataAccountDTO;
import kr.co.bnkfirst.dto.UsersDTO;
import kr.co.bnkfirst.dto.mypage.DealDTO;
import kr.co.bnkfirst.dto.product.PcontractDTO;
import kr.co.bnkfirst.dto.product.ProductDTO;
import kr.co.bnkfirst.service.MydataAccountService;
import kr.co.bnkfirst.service.MypageService;
import kr.co.bnkfirst.service.ProductService;
import kr.co.bnkfirst.service.UsersService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MypageController {

    private final MypageService mypageService;
    private final MydataAccountService mydataAccountService;
    private final ProductService productService;
    private final UsersService usersService;


    @GetMapping("/mypage/main")
    public String mainPage(Model model, Principal principal) {

        // a123은 나중에 로그인할 때 바꾸기
        model.addAttribute("usersList", mypageService.findById(principal.getName()));
        model.addAttribute("dealList", mypageService.findByDeal(principal.getName()));
        model.addAttribute("fundList", mypageService.findByFund(principal.getName()));
        model.addAttribute("balance", mypageService.findByBalance(principal.getName()) + mypageService.findByFundBalance(principal.getName()));
        model.addAttribute("contractList", mypageService.findByContract(principal.getName()));
        model.addAttribute("documentList", mypageService.findByDocumentList(principal.getName()));
        model.addAttribute("ETFList", mypageService.selectEtf(principal.getName()));
        return "mypage/mypage_main";
    }
    @GetMapping("/mypage/prod")
    public String Prod(Model model, Principal principal) {
        model.addAttribute("plus",mypageService.findBySumPlusDbalance(principal.getName()));
        model.addAttribute("minus",mypageService.findBySumMinusDbalance(principal.getName()));
        model.addAttribute("dealList",mypageService.findByDealList(principal.getName()));

        List<PcontractDTO> totalList = new ArrayList<>();
        totalList.addAll(mypageService.findByFundContract(principal.getName()));
        totalList.addAll(mypageService.selectEtf(principal.getName()));

        model.addAttribute("contractList", totalList);
        log.info(mypageService.findByContract(principal.getName()).toString());
        model.addAttribute("balance", mypageService.findByBalance(principal.getName()) + mypageService.findByFundBalance(principal.getName()));

        return "mypage/mypage_prod";
    }

    @PostMapping("/mypage/prod")
    public String Prod(int dbalance, String dwho, String myAcc, String yourAcc, Principal principal) {
        log.info("dbalance="+dbalance+"dwho="+dwho+"  myAcc="+myAcc+"  yourAcc="+yourAcc);
        // a123은 현재 로그인된 아이디로 할 것. 계좌이체
        mypageService.transfer(principal.getName(), dbalance, dwho, myAcc, yourAcc);

        return "redirect:/mypage/prod";
    }

    @GetMapping("/mypage/prod/cancel")
    public String ProdCancel(Model model, Principal principal) {

        model.addAttribute("contractList", mypageService.findByFundContract(principal.getName()));
        return "mypage/mypage_prodCancel";
    }
    @PostMapping("/mypage/prod/cancel")
    public String ProdCancel(String pacc, int pbalance, String recvAcc){
        log.info("pacc = "+pacc+"  pbalance="+pbalance+"  recvAcc="+recvAcc);

        mypageService.deleteContractProcess(pbalance, pacc, recvAcc);
        return "redirect:/mypage/prod";
    }
    @GetMapping("/mypage/setup")
    public String Setup() {
        return "mypage/mypage_setup";
    }

    // 은퇴설계 계산기 - RetirementCalcController로 이동

    // 은퇴설계 계산기 결과 - RetirementCalcController로 이동

    // 연금 계산기 - PensionCalcController로 이동

    // 연금 계산기 결과 - 연금수령방법 기준 - PensionCalcController로 이동

    // 연금 계산기 결과 - 현재 보유자산 기준 - PensionCalcController로 이동

    // 타행 계좌 조회 (마이데이터 시뮬레이션)
    @GetMapping("/mypage/mydata")
    public String mydataAccounts(Model model, Principal principal) {

        String mid = principal.getName();//로그인한 사용자 MID

        //타행 계좌 리스트 조회
        model.addAttribute("mydataAccounts",
                mydataAccountService.getMydataAccountsForUser(mid));

        return "mypage/mypage_mydata"; //새로운 타행계좌 조회 페이지
    }

    @GetMapping("/mypage/compare")
    public String compare(@RequestParam("myaccid") Long myaccid,
                          @RequestParam(value = "risk", required = false) String risk,
                          Model model){
        //선택된 타행 계좌 불러오기
        MydataAccountDTO foreign = mydataAccountService.findByAccid(myaccid);
        Double foreignRate = foreign.getYieldrate();

        List<Map<String, Object>> productList =
                productService.findBetterProducts(foreignRate,risk);

        //타행계좌
        model.addAttribute("foreign", foreign);
        //BNK 상품
        model.addAttribute("productList", productList);
        //필터링
        model.addAttribute("riskFilter", risk);

        return "mypage/mypage_compare";
    }

    // 비밀번호 변경 매핑 (이준우 2025.11.25)
    @GetMapping("/mypage/pwreset")
    public String pwResetPage() {

        return "mypage/mypage_pwreset";
    }
    // 비밀번호 변경 관련 내용 추가 (이준우 2025.11.25)
    @Getter
    @Setter
    public static class PwChangeRequest{
        private String currentPw;
        private String newPw;
    }
    // 현재 비밀번호
    @PostMapping("/pw/check-current")
    @ResponseBody
    public ResponseEntity<?> checkCurrentPw(@RequestBody java.util.Map<String, String> body,
                                            HttpSession session) {

        UsersDTO loginUser = (UsersDTO) session.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(java.util.Map.of("ok", false, "reason", "UNAUTHORIZED"));
        }

        String mid = loginUser.getMid();
        String currentPw = body.get("currentPw");

        boolean ok = usersService.checkCurrentPassword(mid, currentPw);

        return ResponseEntity.ok(java.util.Map.of("ok", ok));
    }
    // 새 비밀번호
    @PostMapping("/pw/change")
    public ResponseEntity<?> changePassword(@RequestBody PwChangeRequest req, HttpSession session) {

        UsersDTO loginUser = (UsersDTO) session.getAttribute("loginUser");
        if (loginUser == null) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("로그인이 필요합니다.");
        }
        String mid = loginUser.getMid();

        if (req.getNewPw() == null || req.getNewPw().isBlank()) {

            return ResponseEntity.badRequest().body("새 비밀번호를 입력해주세요.");
        }
        boolean changed = usersService.changePassword(mid, req.getCurrentPw(), req.getNewPw());
        if (!changed) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("현재 비밀번호가 올바르지 않습니다.");
        }

        log.info("비밀번호 변경 완료 mid={}", mid);

        return ResponseEntity.ok().body(new Object(){
            public final boolean success = true;
        });
    }
 }