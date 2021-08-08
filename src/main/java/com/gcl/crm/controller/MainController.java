package com.gcl.crm.controller;

import com.gcl.crm.entity.Department;
import com.gcl.crm.entity.Employee;
import com.gcl.crm.entity.User;
import com.gcl.crm.service.DepartmentService;
import com.gcl.crm.service.EmployeeService;
import com.gcl.crm.service.UserService;
import com.gcl.crm.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.gcl.crm.constants.MyConstants;
import org.springframework.lang.Nullable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {
    private static final String LOGIN_PAGE = "loginPage";
    private static final String PAGE_ERROR_403 = "error/error-403";
    private static final String DEPARTMENT_PAGE = "/department/home-department-page-V2";

    @Autowired
    EmployeeService employeeService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    UserService userService;

    @RequestMapping(value = {"/welcome", "/"}, method = RequestMethod.GET)
    public String welcomePage(Model model, Principal principal) {
        if (principal == null) {
            return "loginPage";
        }
        User currentUser = userService.getUserByUsername(principal.getName());
        Department departmentForm = new Department();
        List<Department> departments = departmentService.findAllDepartments();
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        model.addAttribute("departments", departments);
        model.addAttribute("departmentForm", departmentForm);
        model.addAttribute("userName", principal.getName());
        model.addAttribute("userInfo", currentUser);
        return DEPARTMENT_PAGE;
    }


    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public String getLoginPage(Model model) {
        return LOGIN_PAGE;
    }



    @GetMapping(value = {"/403"})
    public String accessDenied() {
        return PAGE_ERROR_403;
    }
}
