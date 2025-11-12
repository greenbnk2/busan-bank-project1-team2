package kr.co.bnkfirst.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class CsController {
    //consultation
    @GetMapping("/cs/consultation/document")
    public String cs_document() {
        return "/cs/consultation/document/cs_document";
    }
    @GetMapping("/cs/consultation/FAQ")
    public String cs_faq() {
        return "/cs/consultation/FAQ/cs_faq";
    }
    @GetMapping("/cs/consultation/findStore")
    public String cs_findstore() {
        return "/cs/consultation/findStore/cs_findstore";
    }
    @GetMapping("/cs/consultation/Q&A")
    public String cs_qna() {
        return "/cs/consultation/Q&A/cs_qna";
    }

    //data room
    @GetMapping("/cs/data_room/deposit")
    public String deposit() {
        return "/cs/data_room/deposit/deposit";
    }
    @GetMapping("/cs/data_room/fund")
    public String fund() {
        return "/cs/data_room/fund/fund";
    }
    @GetMapping("/cs/data_room/loan")
    public String loan() {
        return "/cs/data_room/loan/loan";
    }
    @GetMapping("/cs/data_room/trust")
    public String trust() {
        return "/cs/data_room/trust/trust";
    }

    //consumerProtection
    @GetMapping("/cs/consumerProtection")
    public String consumerProtection() {
        return "/cs/consumerProtection/cs_consumerProtection";
    }

    //howUse
    @GetMapping("/cs/howUse/feeInfo")
    public String feeInfo() {
        return "/cs/howUse/feeinfo/feeInfo";
    }
    @GetMapping("/cs/howUse/pwInfo")
    public String pwInfo() {
        return "/cs/howUse/pwInfo/pwInfo";
    }
    @GetMapping("/cs/howUse/regProcess")
    public String regProcess() {
        return "/cs/howUse/regProcess/regProcess";
    }
    @GetMapping("/cs/howUse/serviceTime")
    public String serviceTime() {
        return "/cs/howUse/serviceTime/serviceTime";
    }

    //infoProtect
    @GetMapping("/cs/infoProtect/agencyGuide")
    public String agencyGuide() {
        return "/cs/infoProtect/agencyGuide/agencyGuide";
    }
    @GetMapping("/cs/infoProtect/complaintProcessing")
    public String complaintProcessing() {
        return "/cs/infoProtect/complaintProcessing/complaintProcessing";
    }
    @GetMapping("/cs/infoProtect/financialProtection")
    public String financialProtection() {
        return "/cs/infoProtect/financialProtection/financialProtection";
    }
    @GetMapping("/cs/infoProtect/protectionInformation")
    public String protectionInformation() {
        return "/cs/infoProtect/protectionInformation/protectionInformation";
    }

    //main
    @GetMapping("/cs/main")
    public String cs_main() {
        return "/cs/main/cs_main";
    }

    //productsRoom
    @GetMapping("/cs/productsRoom/AnyProduct")
    public String AnyProduct() {
        return "/cs/productsRoom/AnyProduct/AnyProduct";
    }
    @GetMapping("/cs/productsRoom/bankManual")
    public String bankManual() {
        return "/cs/productsRoom/bankManual/bankManual";
    }
    @GetMapping("/cs/productsRoom/depositProduct")
    public String depositProduct() {
        return "/cs/productsRoom/depositProduct/depositProduct";
    }
    @GetMapping("/cs/productsRoom/serviceFee")
    public String serviceFee() {
        return "/cs/productsRoom/serviceFee/serviceFee";
    }
}
