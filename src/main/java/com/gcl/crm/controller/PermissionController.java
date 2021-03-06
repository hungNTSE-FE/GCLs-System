package com.gcl.crm.controller;

import com.gcl.crm.entity.*;
import com.gcl.crm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/permission")
public class PermissionController {
    private static final String HOME_PAGE = "permission/home-page";
    private static final String HOME_PAGE_V2 = "permission/home-permission-page-V2";
    private static final String CREATE_PAGE = "permission/create-permission-page-v2";
    private static final String EDIT_PAGE = "permission/edit-permission-page-v2";
    private static final String DECENENTRALIZE_PAGE = "permission/decentralization-permission-page-v2";

    @Autowired
    PrivilegeService privilegeService;

    @Autowired
    RoleService roleService;

    @Autowired
    ModuleService moduleService;

    @Autowired
    UserService userService;

    @Autowired
    EmployeeService employeeService;

    @GetMapping({"/home"})
    public String home(Model model, Principal principal,
                       @Nullable @RequestParam("k") String keyword) {
        if (keyword == null){
            List<Role> roles = roleService.getAllRoles();
            model.addAttribute("roles", roles);
        }
        User currentUser = userService.getUserByUsername(principal.getName());
        model.addAttribute("userName", principal.getName());
        model.addAttribute("userInfo", currentUser);
        return HOME_PAGE;
    }

    @GetMapping("/search")
    public String search(RedirectAttributes redirectAttributes,
                         @Nullable @RequestParam("k") String keyword){
        List<Role> roles = roleService.search(keyword);
        redirectAttributes.addFlashAttribute("roles", roles);
        redirectAttributes.addAttribute("k", keyword);
        return "redirect:/permission/home";
    }

    @GetMapping({"/create"})
    public String goCreatePage(Model model, Principal principal){
        User currentUser = userService.getUserByUsername(principal.getName());
        Role role = new Role();
        List<Module> modules = this.getModules();
        model.addAttribute("role", role);
        model.addAttribute("modules", modules);
        model.addAttribute("userName", principal.getName());
        model.addAttribute("userInfo", currentUser);
        return CREATE_PAGE;
    }

    @GetMapping({"/edit"})
    public String goEditPage(Model model, @Nullable @RequestParam("rid") Long roleId, Principal principal){
        Role role = roleService.getRoleById(roleId);
        User currentUser = userService.getUserByUsername(principal.getName());
        if (role == null){
            model.addAttribute("error", "Kh??ng t??m th???y nh??m quy???n th??ch h???p");
            return "redirect:/permission/home";
        }
        List<Module> modules = this.getModules();
        model.addAttribute("modules", modules);
        model.addAttribute("role", role);
        model.addAttribute("userName", principal.getName());
        model.addAttribute("userInfo", currentUser);
        return EDIT_PAGE;
    }

    @GetMapping({"/decentralize"})
    public String goDecentralizePage(Model model, @Nullable @RequestParam("rid") Long roleId, Principal principal){
        if (roleId == null){
            return "redirect:/permission/home";
        }
        User currentUser = userService.getUserByUsername(principal.getName());
        List<Employee> employees = employeeService.getAllWorkingEmployeesWithUserNotNull();
        Role role = roleService.getRoleById(roleId);
        List<Long> employeesOfRole = new ArrayList<>();
        for (UserRole userRole : role.getUserRoles()){
            employeesOfRole.add(userRole.getUser().getEmployee().getId());
        }
        model.addAttribute("employeesOfRole", employeesOfRole);
        model.addAttribute("employees", employees);
        model.addAttribute("role", role);
        model.addAttribute("userName", principal.getName());
        model.addAttribute("userInfo", currentUser);
        return DECENENTRALIZE_PAGE;
    }

    @PostMapping({"/edit"})
    public String edit(RedirectAttributes redirectAttributes, @Nullable @ModelAttribute("role") Role role,
                       @Nullable @RequestParam("privilege-id") List<Long> privilegeIdList,
                       Principal principal){
        if (role == null){
            redirectAttributes.addFlashAttribute("error", "Kh??ng t??m th???y nh??m quy???n th??ch h???p");
            return "redirect:/permission/home";
        }
        User currentUser = userService.getUserByUsername(principal.getName());
        boolean done = roleService.updateRole(role, privilegeIdList, currentUser);
        if (done){
            redirectAttributes.addFlashAttribute("flag","showAlertSuccess");
        } else {
            redirectAttributes.addFlashAttribute("flag","showAlertError");
        }
        return "redirect:/permission/edit?rid=" + role.getId();
    }

    @PostMapping({"/create"})
    public String create( @Nullable @ModelAttribute("role") Role role,
                          @Nullable @RequestParam("privilege-id") List<Long> privilegeIdList,
                          RedirectAttributes redirectAttributes, Principal principal){
        if (role == null){
            redirectAttributes.addFlashAttribute("error", "Kh??ng t??m th???y th??ng tin ph??ng ban");
            return "redirect:/permission/home";
        }
        User currentUser = userService.getUserByUsername(principal.getName());
        boolean done = roleService.createRole(role, privilegeIdList, currentUser);
        if (!done){
            return "redirect:/permission/home";
        }
        redirectAttributes.addFlashAttribute("flag","showAlert");
        return "redirect:/permission/create";
    }

    @PostMapping({"/enable"})
    public String enable(RedirectAttributes redirectAttributes, @Nullable @RequestParam("role-id") Long roleId,
                         Principal principal){
        if (roleId == null){
            return "redirect:/permission/home";
        }
        User currentUser = userService.getUserByUsername(principal.getName());
        boolean done = roleService.enableRole(roleId, currentUser);
        if (done){
            redirectAttributes.addFlashAttribute("flag","showAlertSuccess");
        } else {
            redirectAttributes.addFlashAttribute("flag","showAlertError");
        }
        return "redirect:/permission/edit?rid=" + roleId;
    }

    @PostMapping({"/disable"})
    public String disable(RedirectAttributes redirectAttributes, @Nullable @RequestParam("role-id") Long roleId,
                         Principal principal){
        if (roleId == null){
            return "redirect:/permission/home";
        }
        User currentUser = userService.getUserByUsername(principal.getName());
        boolean done = roleService.disableRole(roleId, currentUser);
        if (done){
            redirectAttributes.addFlashAttribute("flag","showAlertSuccess");
        } else {
            redirectAttributes.addFlashAttribute("flag","showAlertError");
        }
        return "redirect:/permission/edit?rid=" + roleId;
    }

    @PostMapping({"/decentralize"})
    public String decentralize(RedirectAttributes redirectAttributes,
                               @Nullable @RequestParam("role-id") Long roleId,
                               @Nullable @RequestParam("user-id") List<Long> userIdList,
                               Principal principal){
        if (roleId == null){
            return "redirect:/permission/home";
        }
        if (userIdList == null){
            return "redirect:/permission/decentralize?rid=" + roleId;
        }
        User currentUser = userService.getUserByUsername(principal.getName());
        boolean done = roleService.decentralizeRole(roleId, userIdList, currentUser);
        if (done){
            redirectAttributes.addFlashAttribute("flag","showAlert");
        } else {
            redirectAttributes.addFlashAttribute("flag","showAlertError");
        }
        return "redirect:/permission/decentralize?rid=" + roleId;
    }

    private List<Module> getModules(){
        List<Module> modules = moduleService.getAllModules();
        for (Module module : modules){
            module.setPrivileges(privilegeService.getPrivilegesByModule(module));
        }
        return modules;
    }
}
