package org.example.springcrazybarbershop.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springcrazybarbershop.services.NotificationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/admin/notifications")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminNotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public String showNotificationPage() {
        return "admin/notifications/send";
    }

    @PostMapping("/send")
    public String sendNotification(@RequestParam String subject,
                                 @RequestParam String message,
                                 RedirectAttributes redirectAttributes) {
        try {
            notificationService.sendBulkNotification(subject, message);
            redirectAttributes.addFlashAttribute("success", 
                "Рассылка уведомлений успешно начата");
        } catch (Exception e) {
            log.error("Ошибка при отправке уведомлений: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", 
                "Произошла ошибка при отправке уведомлений");
        }
        return "redirect:/admin/notifications";
    }
} 