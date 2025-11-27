package kr.co.bnkfirst.controller;

import kr.co.bnkfirst.service.EmployeeService;
import kr.co.bnkfirst.service.ContributionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/corporate/employee")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ContributionService contributionService;

    /** 직원 리스트 */
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("employees", employeeService.getEmployeeList());
        return "corporate/employee/list";
    }

    /** 직원 상세 */
    @GetMapping("/detail/{empId}")
    public String detail(@PathVariable Long empId, Model model) {
        model.addAttribute("employee", employeeService.getEmployeeDetail(empId));
        return "corporate/employee/detail";
    }

    /** 직원 수정 */
    @GetMapping("/edit/{empId}")
    public String edit(@PathVariable Long empId, Model model) {
        model.addAttribute("employee", employeeService.getEmployeeDetail(empId));
        return "corporate/employee/edit";
    }

    /** 직원 삭제 */
    @GetMapping("/delete/{empId}")
    public String delete(@PathVariable Long empId, Model model) {
        model.addAttribute("employee", employeeService.getEmployeeDetail(empId));
        return "corporate/employee/delete";
    }

    /** 월별 납입 내역 */
    @GetMapping("/contribution/{empId}")
    public String contribution(@PathVariable Long empId, Model model) {

        model.addAttribute("employee", employeeService.getEmployeeDetail(empId));
        model.addAttribute("contributions", contributionService.getContributionByEmpId(empId));
        model.addAttribute("currentBalance", contributionService.getCurrentBalance(empId));

        return "corporate/employee/contribution_list";
    }
}
