package com.gcl.crm.controller;

import com.gcl.crm.entity.Department;
import com.gcl.crm.entity.Permission;
import com.gcl.crm.service.DepartmentService;
import com.gcl.crm.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/department")
public class DepartmentController {

    private final int PAGE_SIZE = 10;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    PermissionService permissionService;

    @GetMapping({"/{page}", ""})
    public String viewAll(Model model, @Nullable @PathVariable("page") Integer page) {
        if (page == null) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        List<Department> departments = departmentService.findAllDepartments(pageable);
        model.addAttribute("departments", departments);
        return "department/department";
    }

    @GetMapping({"/search/{page}", "/search"})
    public String searchByName(Model model, @RequestParam("k") String keyword,
                               @Nullable @PathVariable("page") Integer page) {
        if (page == null) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        List<Department> departments = departmentService.findDepartmentsByName(keyword, pageable);
        model.addAttribute("keyword", keyword);
        model.addAttribute("departments", departments);
        return "department/department";
    }

    @GetMapping({"/create"})
    public String goCreatePage(Model model){
        Department department = new Department();
        List<Permission> permissions = permissionService.findAllPermissions();
        model.addAttribute("department", department);
        model.addAttribute("permissions", permissions);
        return "department/create-department";
    }

    @PostMapping({"/create"})
    public String create(Model model, @ModelAttribute("department") Department department,
                         @Nullable @RequestParam("permission-list") List<Long> permissionIds){
        List<Permission> permissions = permissionService.findPermissionsByIdList(permissionIds);
        if (permissions != null){
            department.setPermissions(permissions);
        }
        departmentService.createDepartment(department);
        return "redirect:/department";
    }
}
