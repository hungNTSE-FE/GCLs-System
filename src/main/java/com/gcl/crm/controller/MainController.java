package com.gcl.crm.controller;

import com.gcl.crm.entity.User;
import com.gcl.crm.service.DepartmentService;
import com.gcl.crm.service.EmployeeService;
import com.gcl.crm.service.PotentialService;
import com.gcl.crm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
public class MainController {
    private static final String LOGIN_PAGE = "loginPage";
    private static final String PAGE_ERROR_403 = "error/error-403";
    private static final String REDIRECT_DEPARTMENT_PAGE = "redirect:/department/home";

    @Autowired
    EmployeeService employeeService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    UserService userService;

    @Autowired
    PotentialService potentialService;

    @RequestMapping(value = {"/welcome", "/"}, method = RequestMethod.GET)
    public String welcomePage(Principal principal) {
        if (principal == null) {
            return LOGIN_PAGE;
        }
        User user = userService.getUserByUsername(principal.getName());
        String roleEmployee = potentialService.getDepartmentByUserName(user);

        if ("SALE".equals(roleEmployee)) {
            return "redirect:/salesman/potential/home";
        } else if ("MARKETING".equals(roleEmployee)) {
            return "redirect:/potential/home";
        }
        return REDIRECT_DEPARTMENT_PAGE;
    }


    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public String getLoginPage() {
        return LOGIN_PAGE;
    }



    @GetMapping(value = {"/403"})
    public String accessDenied() {
        return PAGE_ERROR_403;
    }
}
