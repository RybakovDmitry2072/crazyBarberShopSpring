package org.example.springcrazybarbershop.controllers.admin;

import lombok.RequiredArgsConstructor;
import org.example.springcrazybarbershop.dto.form.auth.SignUpForm;
import org.example.springcrazybarbershop.dto.form.EditClientForm;
import org.example.springcrazybarbershop.exceptions.UserAlreadyExistException;
import org.example.springcrazybarbershop.services.interfeces.IClientService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/clients")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminClientController {

    private final IClientService clientService;

    @GetMapping
    public String listClients(Model model) {
        model.addAttribute("clients", clientService.getAllClients());
        return "admin/clients/list";
    }

    @GetMapping("/new")
    public String newClient(Model model) {
        model.addAttribute("signUpForm", new SignUpForm());
        return "admin/clients/new";
    }

    @PostMapping("/new")
    public String createClient(@ModelAttribute SignUpForm signUpForm, 
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        try {
            clientService.registerNewClientAccount(signUpForm);
            redirectAttributes.addFlashAttribute("successMessage", "Клиент успешно создан");
            return "redirect:/admin/clients";
        } catch (UserAlreadyExistException e) {
            bindingResult.rejectValue("email", "error.user", "Пользователь с таким email уже существует");
            return "admin/clients/new";
        }
    }

    @GetMapping("/{id}")
    public String viewClient(@PathVariable Long id, Model model) {
        model.addAttribute("clientForm", clientService.getClientById(id));
        model.addAttribute("clientId", id);
        return "admin/clients/details";
    }

    @GetMapping("/{id}/edit")
    public String editClient(@PathVariable Long id, Model model) {
        model.addAttribute("client", clientService.getClientById(id));
        return "admin/clients/edit";
    }

    @PostMapping("/{id}/edit")
    public String updateClient(@PathVariable Long id, 
                             @ModelAttribute EditClientForm form,
                             RedirectAttributes redirectAttributes) {
        clientService.updateClient(id, form);
        redirectAttributes.addFlashAttribute("successMessage", "Клиент успешно обновлен");
        return "redirect:/admin/clients";
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        model.addAttribute("totalClients", clientService.getClientCount());
        return "admin/dashboard";
    }

} 