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
            redirectAttributes.addFlashAttribute("notMatch", "Nh???p l???i m???t kh???u kh??ng kh???p");
            return "redirect:/user/change-password";
        }
        if (!encoder.matches(passwordForm.getOldPassword(), currentUser.getEncryptedPassword())){
            redirectAttributes.addFlashAttribute("wrongPass", "M???t kh???u c?? kh??ng ch??nh x??c");
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
            model.addAttribute("notFound", "Kh??ng t??m th???y t??i kho???n ???????c li??n k???t v???i email n??y");
            return FORGOT_PAGE;
        }
        String newPassword = WebUtils.generateRandomString(8);
        if (userService.changePassword(employee.getUser(), newPassword)){
            StringBuilder content = new StringBuilder();
            content.append("Ch??o " + employee.getName() + ",\n\n");
            content.append("????y l?? m???t kh???u m???i c???a b???n t???i CRM-Gia C??t L???i:\n\n");
            content.append("T??i kho???n: " + employee.getUser().getUserName() + "\n\n");
            content.append("M???t kh???u: " + newPassword + "\n\n");
            content.append("L??U ??: N???u b???n kh??ng ph???i ng?????i th???c hi???n thao t??c n??y, h??y th??ng b??o cho qu???n l?? c???a b???n.");
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(MyConstants.MY_EMAIL);
            message.setTo(employee.getCompanyEmail());
            message.setSubject("L???y l???i m???t kh???u t???i CRM-Gia C??t L???i");
            message.setText(content.toString());
            try {
                javaMailSender.send(message);
                model.addAttribute("flag", "showAlert");
                //model.addAttribute("message", "M???t kh???u m???i ???? ???????c g???i ?????n " + email);
            } catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }
        return FORGOT_PAGE;
    }
}
