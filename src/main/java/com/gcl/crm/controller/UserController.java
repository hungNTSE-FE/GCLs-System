package com.gcl.crm.controller;

import com.gcl.crm.constants.MyConstants;
import com.gcl.crm.entity.Employee;
import com.gcl.crm.entity.User;
import com.gcl.crm.form.ChangePasswordForm;
import com.gcl.crm.service.EmployeeService;
import com.gcl.crm.service.UserService;
import com.gcl.crm.utils.EncryptedPasswordUtils;
import com.gcl.crm.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.gcl.crm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping(value = "/user")
public class UserController {
    private static final String CHANGE_PASS_PAGE = "/profile-user/change-password";
    private static final String ACCOUNT_PROFILE_PAGE = "/profile-user/account-profile";
    private static final String FORGOT_PAGE = "forgot-password";

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/change-password", method = RequestMethod.GET)
    public String goChangePass(Model model, Principal principal) {
        ChangePasswordForm changePasswordForm = new ChangePasswordForm();
        model.addAttribute("passwordForm", changePasswordForm);
        User currentUser = userService.getUserByUsername(principal.getName());
        model.addAttribute("userName", principal.getName());
        model.addAttribute("userInfo", currentUser);
        return CHANGE_PASS_PAGE;
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String goAccountProfile(Model model, Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        model.addAttribute("userName", principal.getName());
        model.addAttribute("userInfo", currentUser);
        return ACCOUNT_PROFILE_PAGE;
    }

    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    public String changePassword(RedirectAttributes redirectAttributes, Principal principal,
                                     @ModelAttribute("passwordForm") ChangePasswordForm passwordForm) {
        User currentUser = userService.getUserByUsername(principal.getName());
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!passwordForm.getNewPassword().equals(passwordForm.getConfirmPassword())){
            redirectAttributes.addFlashAttribute("notMatch", "Nhập lại mật khẩu không khớp");
            return "redirect:/user/change-password";
        }
        if (!encoder.matches(passwordForm.getOldPassword(), currentUser.getEncryptedPassword())){
            redirectAttributes.addFlashAttribute("wrongPass", "Mật khẩu cũ không chính xác");
            return "redirect:/user/change-password";
        }
        if (userService.changePassword(currentUser, passwordForm.getNewPassword())){
            redirectAttributes.addFlashAttribute("flag", "showAlert");
        }
        return "redirect:/user/change-password";
    }

    @RequestMapping(value = "/forgot-password", method = RequestMethod.GET)
    public String getForgotPassword() {
        return FORGOT_PAGE;
    }

    @PostMapping("/forgot-password")
    public String sendNewPasswordEmail(Model model,@Nullable @RequestParam("email") String email){
        Employee employee = employeeService.getEmployeeByEmail(email);
        if (employee == null){
            model.addAttribute("email", email);
            model.addAttribute("notFound", "Không tìm thấy tài khoản được liên kết với email này");
            return FORGOT_PAGE;
        }
        String newPassword = WebUtils.generateRandomString(8);
        if (userService.changePassword(employee.getUser(), newPassword)){
            StringBuilder content = new StringBuilder();
            content.append("Chào " + employee.getName() + ",\n\n");
            content.append("Đây là mật khẩu mới của bạn tại CRM-Gia Cát Lợi:\n\n");
            content.append("Tài khoản: " + employee.getUser().getUserName() + "\n\n");
            content.append("Mật khẩu: " + newPassword + "\n\n");
            content.append("LƯU Ý: Nếu bạn không phải người thực hiện thao tác này, hãy thông báo cho quản lý của bạn.");
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(MyConstants.MY_EMAIL);
            message.setTo(employee.getCompanyEmail());
            message.setSubject("Lấy lại mật khẩu tại CRM-Gia Cát Lợi");
            message.setText(content.toString());
            try {
                javaMailSender.send(message);
                model.addAttribute("flag", "showAlert");
                //model.addAttribute("message", "Mật khẩu mới đã được gửi đến " + email);
            } catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }
        return FORGOT_PAGE;
    }
}
