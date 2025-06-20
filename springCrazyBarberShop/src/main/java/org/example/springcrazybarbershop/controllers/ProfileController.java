package org.example.springcrazybarbershop.controllers;

import lombok.RequiredArgsConstructor;
import org.example.springcrazybarbershop.models.Client;
import org.example.springcrazybarbershop.models.User;
import org.example.springcrazybarbershop.services.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final AuthService authService;

    @GetMapping
    public String showProfile(Model model){
        Client user = authService.getCurrentUser();
        model.addAttribute("user", user);
        return "profile";
    }
}
