package org.example.springcrazybarbershop.controllers.admin;

import lombok.RequiredArgsConstructor;
import org.example.springcrazybarbershop.models.HaircutCategory;
import org.example.springcrazybarbershop.services.HaircutCategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/haircut-categories")
@RequiredArgsConstructor
public class AdminHaircutCategoryController {

    private final HaircutCategoryService haircutCategoryService;

    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", haircutCategoryService.findAll());
        return "admin/haircut-categories/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new HaircutCategory());
        return "admin/haircut-categories/form";
    }

    @PostMapping("/new")
    public String createCategory(@ModelAttribute HaircutCategory category) {
        haircutCategoryService.save(category);
        return "redirect:/admin/haircut-categories";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("category", haircutCategoryService.findById(id)
                .orElseThrow(() -> new RuntimeException("Категория не найдена")));
        return "admin/haircut-categories/form";
    }

    @PostMapping("/{id}/edit")
    public String updateCategory(@PathVariable Long id, @ModelAttribute HaircutCategory category) {
        category.setId(id);
        haircutCategoryService.save(category);
        return "redirect:/admin/haircut-categories";
    }

    @PostMapping("/{id}/delete")
    public String deleteCategory(@PathVariable Long id) {
        haircutCategoryService.deleteById(id);
        return "redirect:/admin/haircut-categories";
    }
} 