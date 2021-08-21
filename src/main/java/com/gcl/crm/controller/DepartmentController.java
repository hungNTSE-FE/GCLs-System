package com.gcl.crm.controller;

import com.gcl.crm.entity.Company;
import com.gcl.crm.entity.Department;
import com.gcl.crm.entity.Employee;
import com.gcl.crm.entity.User;
import com.gcl.crm.enums.Status;
import com.gcl.crm.service.DepartmentService;
import com.gcl.crm.service.EmployeeService;
import com.gcl.crm.service.UserService;
import com.gcl.crm.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/department")
public class DepartmentController {
    private final String HOME_PAGE = "department/home-department-page-v2";
    private final String EDIT_PAGE = "department/edit-department-page-v2";
    @Autowired
    EmployeeService employeeService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    UserService userService;

    @RequestMapping(value ="/home", method = RequestMethod.GET)
    public String home(Model model, Principal principal){
        User currentUser = userService.getUserByUsername(principal.getName());
        Department departmentForm = new Department();
        List<Department> departments = departmentService.findAllDepartments();
        List<Employee> employees = employeeService.getAllWorkingEmployees();
        model.addAttribute("employees", employees);
        model.addAttribute("departments", departments);
        model.addAttribute("departmentForm", departmentForm);
        model.addAttribute("userInfo", currentUser);
        return HOME_PAGE;
    }

    @RequestMapping(value = "/edit-v2", method = RequestMethod.GET)
    public String goEditV2Page(Model model, @Nullable @RequestParam("did") String id, Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        Department department = departmentService.findDepartmentById(id);
        if (department == null){
            return "redirect:/department/home";
        }
        model.addAttribute("department", department);
        model.addAttribute("userInfo", currentUser);
        return EDIT_PAGE;
    }

    @PostMapping({"/edit-v2"})
    public String editV2(Model model, @Nullable @RequestParam("did") String id,
                         @Nullable @RequestParam("dname") String name,
                         @Nullable @RequestParam("dnote") String note,
                         RedirectAttributes redirectAttributes){
        Department department = departmentService.findDepartmentById(id);
        if (departmentService.isNameExisted(name, department.getId())) {
            redirectAttributes.addFlashAttribute("flag", "showAlertDuplicateName");
            return "redirect:/department/edit-v2?did=" + department.getId();
        }
        if (department == null){
            model.addAttribute("error", "Không tìm thấy phòng ban đã chọn!");
            return "redirect:/department/home";
        }
        if (name == null){
            model.addAttribute("error", "Tên phòng ban không để trống.");
        }
        department.setName(name);
        department.setNote(note);
        boolean done = departmentService.updateDepartment(department);
        if (done){
            redirectAttributes.addFlashAttribute("flag", "showAlertSuccess");
        } else {
            redirectAttributes.addFlashAttribute("flag", "showAlertError");

        }
        return "redirect:/department/edit-v2?did=" + department.getId();
    }

    @RequestMapping(value ="/create", method = RequestMethod.POST)
    public String create(Model model, @ModelAttribute("departmentForm") Department department,Principal principal, RedirectAttributes redirectAttributes){
        if (departmentService.isNameExisted(department.getName(), department.getId())) {
            redirectAttributes.addFlashAttribute("flag", "showAlertDuplicateName");
            return "redirect:/department/home";
        }
        department.setStatus(Status.ACTIVE);
        boolean done = departmentService.createDepartment(department);
        redirectAttributes.addFlashAttribute("flag", "showAlertSuccess");
        return "redirect:/department/home";
    }

    @PostMapping({"/edit"})
    public String edit(Model model, @Nullable @ModelAttribute("department") Department department){
        if (department == null){
            return "redirect:/department/home";
        }
        boolean done = departmentService.updateDepartment(department);
        return "redirect:/department/edit?did=" + department.getId();
    }

    @PostMapping({"/delete"})
    public String delete(Model model, @Nullable @RequestParam("did") String id){
        if (id == null){
            return "redirect:/department/home";
        }
        boolean done = departmentService.deleteDepartment(id);
        return "redirect:/department/home";
    }
}
