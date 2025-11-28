package kr.co.bnkfirst.controller;

import jakarta.validation.Valid;
import kr.co.bnkfirst.dto.corporate.employee.*;
import kr.co.bnkfirst.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/corporate/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    /** =====================================
     *  직원 목록 (전체 조회 + 검색)
     *  ===================================== */
    @GetMapping("/list")
    public String list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "ALL") String planType,
            Model model
    ) {

        boolean isSearch =
                (keyword != null && !keyword.isEmpty())
                        || (planType != null && !planType.equals("ALL"));

        if (isSearch) {
            model.addAttribute("employees", employeeService.search(keyword, planType));
        } else {
            model.addAttribute("employees", employeeService.getEmployeeList());
        }

        model.addAttribute("keyword", keyword);
        model.addAttribute("planType", planType);

        return "corporate/employee/list";
    }


    /** =====================================
     *  직원 상세
     *  ===================================== */
    @GetMapping("/detail/{empId}")
    public String detail(@PathVariable Long empId, Model model) {

        model.addAttribute("employee", employeeService.getEmployeeDetail(empId));
        model.addAttribute("contributions", employeeService.getEmployeeContributions(empId));
        model.addAttribute("currentBalance", employeeService.getEmployeeCurrentBalance(empId));

        // 왼쪽 리스트
        model.addAttribute("employees", employeeService.getEmployeeList());

        return "corporate/employee/detail";
    }


    /** =====================================
     *  직원 수정 화면
     *  ===================================== */
    @GetMapping("/edit/{empId}")
    public String edit(@PathVariable Long empId, Model model) {

        model.addAttribute("employee", employeeService.getEmployeeDetail(empId));
        model.addAttribute("employeeUpdateDto", new EmployeeUpdateDto());

        return "corporate/employee/edit";
    }


    /** =====================================
     *  직원 수정 실행
     *  ===================================== */
    @PostMapping("/edit/{empId}")
    public String editAction(
            @PathVariable Long empId,
            @Valid @ModelAttribute("employeeUpdateDto") EmployeeUpdateDto dto,
            BindingResult bindingResult,
            Model model
    ) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("employee", employeeService.getEmployeeDetail(empId));
            return "corporate/employee/edit";
        }

        dto.setEmpId(empId);
        employeeService.updateEmployee(dto);

        return "redirect:/corporate/employee/detail/" + empId;
    }


    /** =====================================
     *  직원 삭제 확인
     *  ===================================== */
    @GetMapping("/delete/{empId}")
    public String deleteConfirm(@PathVariable Long empId, Model model) {
        model.addAttribute("employee", employeeService.getEmployeeDetail(empId));
        model.addAttribute("employeeDeleteDto", new EmployeeDeleteDto());
        return "corporate/employee/delete";
    }


    /** =====================================
     *  직원 삭제 실행
     *  ===================================== */
    @PostMapping("/delete/{empId}")
    public String deleteAction(
            @PathVariable Long empId,
            @ModelAttribute("employeeDeleteDto") EmployeeDeleteDto dto
    ) {
        employeeService.deleteEmployee(empId);
        return "redirect:/corporate/employee/list";
    }


    /** =====================================
     *  직원 납입 내역
     *  ===================================== */
    @GetMapping("/contribution/{empId}")
    public String contribution(@PathVariable Long empId, Model model) {

        model.addAttribute("employee", employeeService.getEmployeeDetail(empId));
        model.addAttribute("contributions", employeeService.getEmployeeContributions(empId));
        model.addAttribute("currentBalance", employeeService.getEmployeeCurrentBalance(empId));

        return "corporate/employee/contribution_list";
    }


    /** =====================================
     *  ⭐ 직원 자동완성 API (JSON)
     *  ===================================== */
    @GetMapping("/autocomplete")
    @ResponseBody
    public List<EmployeeAutoDto> autocomplete(@RequestParam String keyword) {
        return employeeService.autocomplete(keyword);
    }

}
