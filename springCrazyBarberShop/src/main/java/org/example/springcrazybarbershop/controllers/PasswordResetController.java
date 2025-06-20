package org.example.springcrazybarbershop.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.springcrazybarbershop.dto.form.auth.PasswordForm;
import org.example.springcrazybarbershop.models.Client;
import org.example.springcrazybarbershop.services.NotificationService;
import org.example.springcrazybarbershop.services.PasswordResetService;
import org.example.springcrazybarbershop.services.interfeces.IClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class PasswordResetController {

    private final IClientService clientService;
    private final PasswordResetService passwordResetService;
    private final NotificationService notificationService;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, 
                                      RedirectAttributes redirectAttributes) {
        try {
            Client client = clientService.findByEmail(email);
            if (client == null) {
                redirectAttributes.addFlashAttribute("error", 
                    "Email не найден в системе");
                return "redirect:/forgot-password";
            }

            String token = UUID.randomUUID().toString();
            clientService.createPasswordResetTokenForUser(client, token);

            String resetUrl = "http://localhost:8080/reset-password?token=" + token;
            String emailContent = String.format(
                """
                <h2>Восстановление пароля</h2>
                <p>Здравствуйте, %s!</p>
                <p>Вы запросили восстановление пароля. Чтобы установить новый пароль, перейдите по ссылке:</p>
                <p><a href="%s">Сбросить пароль</a></p>
                <p>Если вы не запрашивали восстановление пароля, просто проигнорируйте это письмо.</p>
                <p>Ссылка действительна в течение 24 часов.</p>
                """,
                client.getFirstName(),
                resetUrl
            );

            notificationService.sendEmail(client.getEmail(), 
                "Восстановление пароля - Crazy Barber Shop", emailContent);

            redirectAttributes.addFlashAttribute("success", 
                "Инструкции по восстановлению пароля отправлены на ваш email");
            return "redirect:/auth/signin";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Произошла ошибка при отправке инструкций. Пожалуйста, попробуйте позже.");
            return "redirect:/forgot-password";
        }
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, 
                                      Model model,
                                      RedirectAttributes redirectAttributes) {
        String result = passwordResetService.validatePasswordResetToken(token);
        if (result != null) {
            redirectAttributes.addFlashAttribute("error", 
                "Ссылка для восстановления пароля недействительна или устарела");
            return "redirect:/auth/signin";
        }
        
        model.addAttribute("token", token);
        return "auth/reset-password";
    }

    @PostMapping("/reset-password")
    public String handlePasswordReset(@Valid PasswordForm form, 
                                    RedirectAttributes redirectAttributes) {
        String result = passwordResetService.validatePasswordResetToken(form.getToken());
        if (result != null) {
            redirectAttributes.addFlashAttribute("error", 
                "Ссылка для восстановления пароля недействительна или устарела");
            return "redirect:/auth/signin";
        }

        try {
            Client client = clientService.getUserByPasswordResetToken(form.getToken());
            clientService.changeUserPassword(client, form.getNewPassword());
            passwordResetService.deletePasswordResetToken(form.getToken());
            redirectAttributes.addFlashAttribute("success", 
                "Пароль успешно изменен. Теперь вы можете войти с новым паролем.");
            return "redirect:/auth/signin";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Произошла ошибка при смене пароля. Пожалуйста, попробуйте позже.");
            return "redirect:/reset-password?token=" + form.getToken();
        }
    }
}
