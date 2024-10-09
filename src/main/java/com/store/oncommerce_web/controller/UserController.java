package com.store.oncommerce_web.controller;
import com.store.oncommerce_web.model.User;
import com.store.oncommerce_web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @ModelAttribute
    public void addAttributes(Model model) {
        User user = userService.getAuthenticatedUser();
        if (user != null) {
            model.addAttribute("firstName", user.getFirstName());
            model.addAttribute("lastName", user.getLastName());
            model.addAttribute("email", user.getEmail());
        }
    }
    @GetMapping("/profile")
    public String userProfile(Model model) {
        model.addAttribute("content" ,"profile");
        return "layout";
    }



    }

