package kr.co.bnkfirst.controller;

import kr.co.bnkfirst.dto.BranchDTO;
import kr.co.bnkfirst.dto.DocumentDTO;
import kr.co.bnkfirst.dto.PageRequestDTO;
import kr.co.bnkfirst.dto.admin.PageResponseAdminDocumentDTO;
import kr.co.bnkfirst.dto.admin.PageResponseAdminProductDTO;
import kr.co.bnkfirst.dto.admin.PageResponseAdminUsersDTO;
import kr.co.bnkfirst.service.AdminService;
import kr.co.bnkfirst.service.BranchService;
import kr.co.bnkfirst.service.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final DocumentService documentService;
    private final BranchService branchService;


    @GetMapping("/admin/main")
    public String main(){
        return "admin/admin_main";
    }
    @GetMapping("/admin/member")
    public String member(Model model, PageRequestDTO pageRequestDTO){

        log.info("pageRequestDTO={}", pageRequestDTO);
        PageResponseAdminUsersDTO pageResponseDTO = adminService.selectAllUsers(pageRequestDTO);

        model.addAttribute("pageResponseDTO", pageResponseDTO);

        // 전체회원 수 출력
        model.addAttribute("countAllUsers", adminService.countAllUsers());
        // 신규가입 수 출력(현재 시간으로부터 6개월까지)
        model.addAttribute("countSixMonthUsers", adminService.countSixMonthUsers());
        // 상태가 휴면인 회원 수 출력
        model.addAttribute("countWait", adminService.countWait());
        // 상태가 탈퇴인 회원 수 출력
        model.addAttribute("countWithdrawal", adminService.countWithdrawal());

        return "admin/admin_member";
    }
    @GetMapping("/admin/member/search")
    public String adminmemberSearch(PageRequestDTO pageRequestDTO, Model model){

        log.info("pageRequestDTO:{}",pageRequestDTO);

        // 전체회원 수 출력
        model.addAttribute("countAllUsers", adminService.countAllUsers());
        // 신규가입 수 출력(현재 시간으로부터 6개월까지)
        model.addAttribute("countSixMonthUsers", adminService.countSixMonthUsers());
        // 상태가 휴면인 회원 수 출력
        model.addAttribute("countWait", adminService.countWait());
        // 상태가 탈퇴인 회원 수 출력
        model.addAttribute("countWithdrawal", adminService.countWithdrawal());

        PageResponseAdminUsersDTO pageResponseDTO = adminService.selectAllUsers(pageRequestDTO);
        model.addAttribute("pageResponseDTO", pageResponseDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);

        return "admin/admin_member_searchList";
    }

    @GetMapping("/admin/env")
    public String env(){
        return "admin/admin_env";
    }
    @GetMapping("/admin/prod")
    public String prod(Model model, PageRequestDTO pageRequestDTO){

        log.info("pageRequestDTO={}", pageRequestDTO);
        PageResponseAdminProductDTO pageResponseDTO = adminService.selectAllProduct(pageRequestDTO);

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


    /* ///////////////////////////
     * 고객센터 관리 (전세현)
     * /////////////////////////// */
    @GetMapping("/admin/cs")
    public String csList(
            @RequestParam(defaultValue = "cs") String group,   // cs / form / data
            @RequestParam(defaultValue = "faq") String type,   // faq / doc / qna / ....
            Model model
    ) {
        log.info("admin cs list group={}, type={}", group, type);

        // 기본 값: 빈 리스트
        List<DocumentDTO> docList = List.of();
        List<BranchDTO>   branchList = List.of();

        if ("cs".equals(group) && "branch".equals(type)) {
            // 영업점은 BRANCH 테이블에서 전체 조회
            branchList = branchService.getAllBranches();
        } else {
            // 나머지는 DOCUMENT.DOCTYPE 기준
            String doctype = documentService.resolveDoctype(group, type);
            if (doctype == null) {
                throw new IllegalArgumentException("지원하지 않는 group/type: " + group + "/" + type);
            }
            docList = documentService.getAdminDocuments(doctype); // 전체 로드 → JS에서 페이지네이션
        }

        model.addAttribute("group", group);
        model.addAttribute("type", type);
        model.addAttribute("docList", docList);
        model.addAttribute("branchList", branchList);

        return "admin/admin_cs";
    }

    @GetMapping("/admin/cs/register")
    public String csRegisterForm(
            @RequestParam String group,
            @RequestParam String type,
            Model model
    ){
        model.addAttribute("group", group);
        model.addAttribute("type", type);
        model.addAttribute("document", new DocumentDTO());
        return "admin/admin_csregister";
    }

    @PostMapping("/admin/cs/register")
    public String csRegister(
            DocumentDTO documentDTO,
            @RequestParam String group,
            @RequestParam String type,
            RedirectAttributes ra
    ){
        documentDTO.setDocgroup(group);
        documentDTO.setDoctype(type);

        documentService.insertAdminDocument(documentDTO);

        ra.addFlashAttribute("toastSuccess", "게시물이 등록되었습니다.");
        return "redirect:/admin/cs?group=" + group + "&type=" + type;
    }

    @GetMapping("/admin/cs/modify")
    public String csModifyForm(
            @RequestParam int docid,
            @RequestParam String group,
            @RequestParam String type,
            Model model
    ){
        DocumentDTO dto = documentService.getDocumentById(docid);

        model.addAttribute("group", group);
        model.addAttribute("type", type);
        model.addAttribute("document", dto);

        return "admin/admin_csmodify";
    }

    @PostMapping("/admin/cs/modify")
    public String csModify(
            DocumentDTO documentDTO,
            @RequestParam String group,
            @RequestParam String type,
            RedirectAttributes ra
    ){
        documentDTO.setDocgroup(group);
        documentDTO.setDoctype(type);

        documentService.updateAdminDocument(documentDTO);

        ra.addFlashAttribute("toastSuccess", "게시물이 수정되었습니다.");
        return "redirect:/admin/cs?group=" + group + "&type=" + type;
    }

    @PostMapping("/admin/cs/delete")
    public String csDelete(
            @RequestParam int docid,
            @RequestParam String group,
            @RequestParam String type,
            RedirectAttributes ra
    ){
        documentService.deleteAdminDocument(docid);
        ra.addFlashAttribute("toastSuccess", "게시물이 삭제되었습니다.");
        return "redirect:/admin/cs?group=" + group + "&type=" + type;
    }



    // 고객센터 리스트 JSON 디버그용
    @GetMapping("/admin/cs/debug")
    @ResponseBody
    public PageResponseAdminDocumentDTO csListDebug(
            @RequestParam(defaultValue = "cs") String group,   // cs / form / data
            @RequestParam(defaultValue = "faq") String type,   // faq / qna / db / notice ...
            PageRequestDTO pageRequestDTO
    ) {
        log.info("admin cs DEBUG group={}, type={}, page={}", group, type, pageRequestDTO);

        return documentService.getAdminDocumentPage(group, type, pageRequestDTO);
    }

}
