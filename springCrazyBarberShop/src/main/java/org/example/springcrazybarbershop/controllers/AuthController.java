package org.example.springcrazybarbershop.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.springcrazybarbershop.dto.form.auth.SignUpForm;
import org.example.springcrazybarbershop.exceptions.UserAlreadyExistException;
import org.example.springcrazybarbershop.services.interfeces.IClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class AuthController {

    private final IClientService clientService;

    @GetMapping("/auth/signin")
    public String showLoginPage() {
        return "auth/login";
    }

    @GetMapping("/auth/signup")
    public String showRegistrationUser(Model model){
        SignUpForm signUpForm = new SignUpForm();
        model.addAttribute("signUpForm", signUpForm);
        return "auth/registration";
    }

    @PostMapping("/auth/signup")
    public String registerUserAccount(@ModelAttribute("signUpForm") @Valid SignUpForm signUpForm,
                                      BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            model.addAttribute("signUpForm", signUpForm);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "auth/registration";
        }
        try {
            clientService.registerNewClientAccount(signUpForm);
        } catch (UserAlreadyExistException uaeEx) {
            model.addAttribute("message", "An account for that username/email already exists.");
            return "auth/registration";
        }
        return "redirect:/auth/signin";
    }
} 