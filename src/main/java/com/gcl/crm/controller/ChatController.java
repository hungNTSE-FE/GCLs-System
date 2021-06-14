package com.gcl.crm.controller;

import com.gcl.crm.entity.AppUser;
import com.gcl.crm.entity.ChatMessage;
import com.gcl.crm.repository.UserRepository2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class ChatController {

    @Autowired
    UserRepository2 userRepository2;

    @GetMapping("/chat-socket-v2")
    public String goChatV2(Model model, Principal principal) {
        User loginedUser = (User) ((Authentication) principal).getPrincipal();
        AppUser appUser = userRepository2.findAppUserByUserName(loginedUser.getUsername());
        String appUserName = appUser.getEmployee().getName();
        System.out.println("name: " + appUserName);
        model.addAttribute("username", appUserName);
        return "/message/chat-socket-page-V2";
    }

    @GetMapping("/messagechat")
    public String goChat(Model model, Principal principal) {
        User loginedUser = (User) ((Authentication) principal).getPrincipal();
        AppUser appUser = userRepository2.findAppUserByUserName(loginedUser.getUsername());
        String appUserName = appUser.getEmployee().getName();
        System.out.println("name: " + appUserName);
        model.addAttribute("username", appUserName);
        return "/message/websocket-chat";
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}
