package com.gcl.crm.controller;

import com.gcl.crm.entity.Permission;
import com.gcl.crm.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    PermissionService permissionService;

    @GetMapping({"/home"})
    public String home(Model model) {
        List<Permission> permissions = permissionService.findAllPermissions();
        model.addAttribute("permissions", permissions);
        return "permission/home-permission-page-v2";
    }

    @GetMapping({"/create"})
    public String goCreatePage(Model model){
        Permission permission = new Permission();
        model.addAttribute("permission", permission);
        return "permission/create-permission-page-v2";
    }

    @PostMapping({"/create"})
    public String create(Model model){

        return "permission/create-permission-page";
    }

    @GetMapping({"/edit"})
    public String goEditPage(Model model){

        return "permission/edit-permission-page-v2";
    }

    @PostMapping({"/edit"})
    public String edit(Model model){

        return "";
    }

    @GetMapping({"/decentralize"})
    public String goDecentralizePage(Model model){

        return "permission/decentralization-permission-page-v2";
    }

    @PostMapping({"/decentralize"})
    public String decentralize(Model model){

        return "permission/decentralization-permission-page";
    }
}
