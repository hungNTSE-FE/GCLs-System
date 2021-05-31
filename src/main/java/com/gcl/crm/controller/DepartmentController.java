package com.gcl.crm.controller;

import com.gcl.crm.entity.Company;
import com.gcl.crm.entity.Department;
import com.gcl.crm.enums.Status;
import com.gcl.crm.service.CompanyService;
import com.gcl.crm.service.DepartmentService;
import com.gcl.crm.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    CompanyService companyService;

    @Autowired
    DepartmentService departmentService;

    @RequestMapping(value ="/home", method = RequestMethod.GET)
    public String home(Model model, Principal principal){
        // Sau khi user login thanh cong se co principal
        String userName = principal.getName();
        System.out.println("User Name: " + userName);

        User loginedUser = (User) ((Authentication) principal).getPrincipal();

        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);

        List<Company> companies = companyService.findAllCompanies();
        model.addAttribute("companies", companies);
        return "department/home-department-page-v2";
    }

    @GetMapping({"/create"})
    public String goCreatePage(Model model){
        Department department = new Department();
        department.setCompany(new Company());
        List<Company> companies = companyService.findAllCompanies();
        model.addAttribute("companies", companies);
        model.addAttribute("department", department);
        return "department/create-department-page";
    }

    @RequestMapping(value = "/edit-v2", method = RequestMethod.GET)
    public String goEditV2Page(Model model) {
        return "department/edit-department-page-v2";
    }

    @PostMapping({"/create"})
    public String create(Model model, @ModelAttribute("department") Department department){
        Company company = companyService.findCompanyById(department.getCompany().getId());
        department.setCompany(company);
        department.setStatus(Status.ACTIVE);
        boolean done = departmentService.createDepartment(department);
        List<Company> companies = companyService.findAllCompanies();
        model.addAttribute("companies", companies);
        return "department/create-department-page";
    }

    @GetMapping({"/edit"})
    public String goEditPage(Model model,@Nullable @RequestParam("did") String id){
        Department department = departmentService.findDepartmentById(id);
        if (department == null){
            System.out.println(id);
            return "redirect:/department/home";
        }

        List<Company> companies = companyService.findAllCompanies();
        model.addAttribute("companies", companies);
        model.addAttribute("department", department);
        return "department/edit-department-page";
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
