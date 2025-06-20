package org.example.springcrazybarbershop.controllers.admin;

import lombok.RequiredArgsConstructor;
import org.example.springcrazybarbershop.models.*;
import org.example.springcrazybarbershop.services.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/appointments")
@RequiredArgsConstructor
public class AdminAppointmentController {

    private final AppointmentService appointmentService;
    private final ClientService clientService;
    private final EmployeeService employeeService;
    private final TimeSlotService timeSlotService;
    private final HaircutCategoryService haircutCategoryService;

    @GetMapping
    public String listAppointments(Model model,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(defaultValue = "desc") String sort) {
        Page<Appointment> appointmentPage = appointmentService.findAllPaginated(page, size, sort);
        model.addAttribute("appointments", appointmentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", appointmentPage.getTotalPages());
        model.addAttribute("totalItems", appointmentPage.getTotalElements());
        model.addAttribute("sortDirection", sort);
        return "admin/appointments/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("clients", clientService.getAllClients());
        model.addAttribute("employees", employeeService.findAll());
        model.addAttribute("timeSlots", timeSlotService.findAll());
        model.addAttribute("haircutCategories", haircutCategoryService.findAll());
        model.addAttribute("statuses", AppointmentStatus.values());
        return "admin/appointments/new";
    }

    @PostMapping("/new")
    public String createAppointment(@ModelAttribute Appointment appointment) {
        appointmentService.save(appointment);
        return "redirect:/admin/appointments";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Appointment appointment = appointmentService.findById(id)
                .orElseThrow(() -> new RuntimeException("Запись не найдена"));
        
        model.addAttribute("appointment", appointment);
        model.addAttribute("clients", clientService.getAllClients());
        model.addAttribute("employees", employeeService.findAll());
        model.addAttribute("timeSlots", timeSlotService.findAll());
        model.addAttribute("haircutCategories", haircutCategoryService.findAll());
        model.addAttribute("statuses", AppointmentStatus.values());
        return "admin/appointments/edit";
    }

    @PostMapping("/{id}/edit")
    public String updateAppointment(@PathVariable Long id, @ModelAttribute Appointment appointment) {
        appointment.setId(id);
        appointmentService.save(appointment);
        return "redirect:/admin/appointments";
    }

    @PostMapping("/{id}/delete")
    public String deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteById(id);
        return "redirect:/admin/appointments";
    }
} 