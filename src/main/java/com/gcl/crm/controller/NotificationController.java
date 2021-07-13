package com.gcl.crm.controller;

import com.gcl.crm.entity.User;
import com.gcl.crm.entity.Notification;
import com.gcl.crm.service.NotificationService;
import com.gcl.crm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.security.Principal;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @Autowired
    UserService userService;

    @Autowired
    public JavaMailSender emailSender;

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String goCreatePage(Model model) {
        List<User> users = userService.getUserByEnabled();
        model.addAttribute("users", users);
        return "/notification/insert_notification_page_V2";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, params = "notification")
    public String create(Model model,
                         @ModelAttribute("notification") Notification notification,
                         @RequestParam("select2") List<Long> selectUser,
                         @RequestParam("topic") String topic,
                         @RequestParam("upload") String upload,
                         @RequestParam("describe") String describe,
                         Principal principal,
                         RedirectAttributes redirectAttributes
    ) {
        org.springframework.security.core.userdetails.User loginedUser = (org.springframework.security.core.userdetails.User) ((Authentication) principal).getPrincipal();
        User user = userService.getUserByUsername(loginedUser.getUsername());
        user.getUserId();
        notification.setUser(user);
        notification.setCreated_at(getCurrentDate());
        notification.setTopic(topic);
        notification.setMessage(describe);
        notificationService.createNotification(notification, selectUser);
        return "redirect:/notification/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, params = "sendemail")
    public String sendEmail(Model model, @ModelAttribute("notification") Notification notification,
                            @RequestParam("topic") String topic,
                            @RequestParam("describe") String describe,
                            @RequestParam("select2") List<Long> selectUser,
                             Principal principal,
                             RedirectAttributes redirectAttributes) throws MessagingException {
        MimeMessage messageMime = emailSender.createMimeMessage();
        boolean multipart = true;
        System.out.println(selectUser);
        MimeMessageHelper helper = new MimeMessageHelper(messageMime, multipart);
        List<User> users = userService.getUsersByIdList(selectUser);
        for (int i = 0; i < users.size(); i++) {
            helper.setTo(users.get(i).getEmployee().getCompanyEmail());
            helper.setSubject(topic);
            helper.setText(describe);

            String path = "/Users/mappe/Downloads/picture1.png";
            // Attachment 1
            FileSystemResource file1 = new FileSystemResource(new File(path));
            helper.addAttachment("picture1.png", file1);
            this.emailSender.send(messageMime);
        }
        return "redirect:/notification/create";
    }

    private Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }

}
