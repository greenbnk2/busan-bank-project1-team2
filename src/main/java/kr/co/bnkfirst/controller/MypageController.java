package kr.co.bnkfirst.controller;

import kr.co.bnkfirst.dto.mypage.DealDTO;
import kr.co.bnkfirst.service.MypageService;
import kr.co.bnkfirst.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MypageController {

    private final MypageService mypageService;

    @GetMapping("/mypage/main")
    public String mainPage(Model model, Principal principal) {

        // a123은 나중에 로그인할 때 바꾸기
        model.addAttribute("usersList", mypageService.findById(principal.getName()));
        model.addAttribute("dealList", mypageService.findByDeal(principal.getName()));
        model.addAttribute("balance", mypageService.findByBalance(principal.getName()));
        model.addAttribute("contractList", mypageService.findByContract(principal.getName()));
        model.addAttribute("documentList", mypageService.findByDocumentList(principal.getName()));
        return "mypage/mypage_main";
    }
    @GetMapping("/mypage/prod")
    public String Prod(Model model, Principal principal) {
        model.addAttribute("plus",mypageService.findBySumPlusDbalance(principal.getName()));
        model.addAttribute("minus",mypageService.findBySumMinusDbalance(principal.getName()));
        model.addAttribute("dealList",mypageService.findByDealList(principal.getName()));
        model.addAttribute("contractList", mypageService.findByContract(principal.getName()));
        log.info(mypageService.findByContract(principal.getName()).toString());
        model.addAttribute("balance", mypageService.findByBalance(principal.getName()));

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
        model.addAttribute("contractList", mypageService.findByContract(principal.getName()));
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

}