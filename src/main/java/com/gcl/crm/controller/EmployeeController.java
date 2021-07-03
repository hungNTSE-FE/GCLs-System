package com.gcl.crm.controller;

import com.gcl.crm.entity.*;
import com.gcl.crm.enums.EmployeeStatus;
import com.gcl.crm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.constraints.Null;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/employee")
public class EmployeeController {
    private static final String HOME_GROUP_PAGE = "employee/group-employee-page-V2";
    private static final String UPDATE_GROUP_PAGE = "employee/edit-group-employee-page-V2";
    private static final String ERROR_400 = "error/error-400";

    @Autowired
    EmployeeService employeeService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    PositionService positionService;

    @Autowired
    UserService userService;

    @Autowired
    MarketingGroupService marketingGroupService;



    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String getHomePage(Model model, Principal principal) {
        if (principal == null) {
            return ERROR_400;
        }
        List<Employee> employees = employeeService.getAllNotGroupedEmployees();
        List<Department> departments = departmentService.findAllDepartments();
        List<Position> positions = positionService.findAllPositions();
        model.addAttribute("employees", employees);
        model.addAttribute("departments", departments);
        model.addAttribute("positions", positions);
        return "employee/home-employee-page-V2";
    }

    @RequestMapping(value = "/marketing-group", method = RequestMethod.GET)
    public String getHomeGroupPage(Model model, Principal principal) {
        if (principal == null) {
            return ERROR_400;
        }
        List<Employee> employees = employeeService.getAllNotGroupedEmployees();
        MarketingGroup marketingGroup = new MarketingGroup();
        List<MarketingGroup> marketingGroups = marketingGroupService.getAllMktByStatus();
        model.addAttribute("marketingGroups", marketingGroups);
        model.addAttribute("marketingGroup", marketingGroup);
        model.addAttribute("employees", employees);
        return HOME_GROUP_PAGE;
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String getInsertPage(Model model, Principal principal) {
        if (principal == null) {
            return ERROR_400;
        }
        Employee employee = new Employee();
        employee.setAppUser(new AppUser());

        List<Department> departments = departmentService.findAllDepartments();
        List<Position> positions = positionService.findAllPositions();
        model.addAttribute("employee", employee);
        model.addAttribute("departments", departments);
        model.addAttribute("positions", positions);
        return "employee/insert-employee-page-V2";
    }

    @RequestMapping(value = "/marketing-group/update/{id}", method = RequestMethod.GET)
    public String getUpdateGroupPage(Model model, @Nullable @PathVariable("id") String id, Principal principal) {
        List<Long> arr = new ArrayList<Long>();
        if (principal == null) {
            return ERROR_400;
        }
        MarketingGroup marketingGroupById = marketingGroupService.findMarketGroupById(id);
        if (marketingGroupById == null) {
            return "redirect:/employee/marketing-group";
        }

        List<Employee> empTest = employeeService.getAllNotGroupedEmployees();
        List<Employee> employeesInGroup = marketingGroupById.getEmployees();
        List<Employee> union = Stream.concat( empTest.stream(), employeesInGroup.stream())
                .collect( Collectors.toList());

        for (int i = 0; i < employeesInGroup.size(); i++) {
            arr.add(employeesInGroup.get(i).getId());
        }
        String stringArr = arr.stream()
                .map(n -> String.valueOf(n))
                .collect(Collectors.joining(", "));

        model.addAttribute("membersGroup", stringArr);
        model.addAttribute("employeeInsiteGroup", employeesInGroup);
        model.addAttribute("employees", union);
        model.addAttribute("marketingGroup", marketingGroupById);
        return UPDATE_GROUP_PAGE;
    }

    @RequestMapping(value = "/marketing-group/create", method = RequestMethod.POST)
    public String createGroupMKT(Model model,
                                 @Nullable @ModelAttribute("marketingGroup") MarketingGroup marketingGroup,
                                 @RequestParam("listSelected") List<Long> aidList,
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {
        User loginedUser = (User) ((Authentication) principal).getPrincipal();
        AppUser appUser = userService.getAppUserByUsername(loginedUser.getUsername());
        boolean  error = false;
        if (!error && marketingGroupService.isCodeExisted(marketingGroup.getCode(), marketingGroup.getId())) {
            redirectAttributes.addFlashAttribute("flag","showAlertError");
            return "redirect:/employee/marketing-group";
        }
        marketingGroup.setMaker(appUser.getUserId());
        boolean done = marketingGroupService.createMarketingGroup(marketingGroup, aidList);
        redirectAttributes.addFlashAttribute("flag","showAlert");
        return "redirect:/employee/marketing-group";
    }

    @RequestMapping(value = "/marketing-group/update", method = RequestMethod.POST)
    public String editGroupMKT(Model model,
                               @Nullable @RequestParam("id") String id,
                               @Nullable @RequestParam("code") String code,
                               @Nullable @RequestParam("name") String name,
                               @Nullable @RequestParam("listSelected") List<Long> aidList,
                               @Nullable @RequestParam("description") String description,
                               Principal principal,
                               RedirectAttributes redirectAttributes
                               ) {
        User loginedUser = (User) ((Authentication) principal).getPrincipal();
        AppUser appUser = userService.getAppUserByUsername(loginedUser.getUsername());

        MarketingGroup marketingGroup1 = marketingGroupService.findMarketGroupById(id);
        boolean error = false;
        if (!error && marketingGroupService.isCodeExisted(code, Long.parseLong(id))) {
            redirectAttributes.addFlashAttribute("flag","showAlertError");
            return "redirect:/employee/marketing-group/update/" + Long.parseLong(id);
        }
        if (!error && marketingGroup1 == null) {
            redirectAttributes.addFlashAttribute("flag","showAlertError");
            return "redirect:/employee/marketing-group";
        }
        marketingGroup1.setLastModifier(appUser.getUserId());
        marketingGroup1.setId(Long.parseLong(id));
        marketingGroup1.setCode(code);
        marketingGroup1.setName(name);
        marketingGroup1.setMaker(appUser.getUserId());
        marketingGroup1.setNote(description);
        boolean done = marketingGroupService.updateMarketingGroup(marketingGroup1, aidList);
        redirectAttributes.addFlashAttribute("flag","showAlert");
        return "redirect:/employee/marketing-group/update/" + Long.parseLong(id);
    }

    @RequestMapping(value = "/marketing-group/delete", method = RequestMethod.POST)
    public String deleteGroupMKT(Model model,
                                 @Nullable @RequestParam("idDl") String id,
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {
        User loginedUser = (User) ((Authentication) principal).getPrincipal();
        AppUser appUser = userService.getAppUserByUsername(loginedUser.getUsername());

        MarketingGroup marketingGroupUseDelete = marketingGroupService.findMarketGroupById(id);

        if (id == null) {
            return "redirect:/employee/marketing-group";
        }

        marketingGroupUseDelete.setLastModifier(appUser.getUserId());
        boolean done = marketingGroupService.deleteMarketingGroup(marketingGroupUseDelete);
        redirectAttributes.addFlashAttribute("flag","showAlertDeleteSuccess");
        return "redirect:/employee/marketing-group";
    }

    @PostMapping({"/create"})
    public String create(Model model, @Nullable @ModelAttribute("employee") Employee employee,
                         @Nullable @RequestParam("pid") Long pid,
                         @Nullable @RequestParam("did") Long did){
        if (employee == null){
            return "redirect:/employee/home";
        }
        boolean done = employeeService.createEmployee(employee, pid, did);
        if (done){
            model.addAttribute("message", "Tạo mới nhân viên thành công");
        } else {
            model.addAttribute("error", "Có lỗi xảy ra! Tạo mới thất bại");
        }
        return "redirect:/employee/create";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String getEditPage(Model model, RedirectAttributes redirectAttributes,
                              @Nullable @RequestParam("eid") Long id) {
        if (id == null){
            redirectAttributes.addAttribute("error", "Không tìm thấy nhân viên được chọn");
            return "redirect:/employee/home";
        }
        Employee employee = employeeService.getEmployeeById(id);
        if (employee == null){
            redirectAttributes.addAttribute("error", "Không tìm thấy nhân viên được chọn");
            return "redirect:/employee/home";
        }
        List<Department> departments = departmentService.findAllDepartments();
        List<Position> positions = positionService.findAllPositions();
        model.addAttribute("departments", departments);
        model.addAttribute("positions", positions);
        model.addAttribute("employee", employee);
        return "employee/edit-employee-page-V2";
    }

    @RequestMapping(value = "/editGroup", method = RequestMethod.GET)
    public String goEditGroupPage(Model model) {
        return "employee/edit-group-employee-page-V2";
    }

    @PostMapping({"/edit"})
    public String edit(Model model, @Nullable @ModelAttribute("employee") Employee employee,
                       @Nullable @RequestParam("pid") Long pid,
                       @Nullable @RequestParam("did") Long did,
                       @Nullable @RequestParam("user-name") String username,
                       @Nullable @RequestParam("password") String password){
        if (employee == null){
            return "redirect:/employee/home";
        }
        boolean done = employeeService.updateEmployee(employee, pid, did, username, password);
        if (done){
            model.addAttribute("message", "Cập nhật nhân viên thành công");
        } else {
            model.addAttribute("error", "Đã có lỗi xảy ra! Cập nhật thất bại");
        }
        return "redirect:/employee/edit?eid=" + employee.getId();
    }

    @PostMapping({"/delete"})
    public String delete(Model model, @Nullable @RequestParam("eid") Long id){
        Employee employee = employeeService.getEmployeeById(id);
        if (employee == null || employee.getStatus().equals(EmployeeStatus.OFF_WORKING)){
            model.addAttribute("error", "Không tìm thấy nhân viên được chọn");
            return "redirect:/employee/home";
        }
        boolean done = employeeService.deleteEmployee(id);
        if (done){
            model.addAttribute("message", "Xóa nhân viên thành công");
        } else {
            model.addAttribute("error", "Đã có lỗi xảy ra! Xóa thất bại");
        }
        return "redirect:/employee/home";
    }

    @RequestMapping(value = "/marketing-group/search", method = RequestMethod.POST)
    public String search(Model model, @Nullable @ModelAttribute("marketingGroup") MarketingGroup searchForm) {
        List<Employee> employees = employeeService.getAllNotGroupedEmployees();
        List<MarketingGroup> marketingGroups = marketingGroupService.searchAllGroupMktByCode(searchForm);
        model.addAttribute("employees", employees);
        model.addAttribute("marketingGroups", marketingGroups);
        return HOME_GROUP_PAGE;
    }
}
