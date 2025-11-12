package kr.co.bnkfirst.controller;

import kr.co.bnkfirst.dto.PageRequestDTO;
import kr.co.bnkfirst.dto.admin.PageResponseAdminProductDTO;
import kr.co.bnkfirst.dto.admin.PageResponseAdminUsersDTO;
import kr.co.bnkfirst.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/admin/main")
    public String main(){
        return "admin/admin_main";
    }
    @GetMapping("/admin/member")
    public String member(Model model, PageRequestDTO pageRequestDTO){

        log.info("pageRequestDTO={}", pageRequestDTO);
        PageResponseAdminProductDTO pageResponseDTO = adminService.selectAllProduct(pageRequestDTO);

        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "admin/admin_member";
    }
    @GetMapping("/admin/env")
    public String env(){
        return "admin/admin_env";
    }
    @GetMapping("/admin/prod")
    public String prod(Model model, PageRequestDTO pageRequestDTO){

        log.info("pageRequestDTO={}", pageRequestDTO);
        PageResponseAdminUsersDTO pageResponseDTO = adminService.selectAllUsers(pageRequestDTO);

        model.addAttribute("pageResponseDTO", pageResponseDTO);
        return "admin/admin_prod";
    }
    @GetMapping("/admin/prod/modify")
    public String prodmodify(){
        return "admin/admin_prodModify";
    }
    @GetMapping("/admin/prod/register")
    public String prodregister(){
        return "admin/admin_prodregister";
    }
    @GetMapping("/admin/prod/delete")
    public String proddelete(@RequestParam("pid") String pid, RedirectAttributes ra){
        log.info("pid={}", pid);

        try {
            adminService.deleteByProduct(pid);
            ra.addFlashAttribute("toastSuccess", "상품이 삭제되었습니다.");
        } catch (Exception e) {
            ra.addFlashAttribute("toastError", "해당 상품에 가입한 회원이 있어 삭제할 수 없습니다.");
        }


        return "redirect:/admin/prod";
    }
    @GetMapping("/admin/cs")
    public String cs(){
        return "admin/admin_cs";
    }
    @GetMapping("/admin/cs/register")
    public String csregister(){
        return "admin/admin_csregister";
    }
    @GetMapping("/admin/cs/modify")
    public String csmodify(){
        return "admin/admin_csmodify";
    }
}
