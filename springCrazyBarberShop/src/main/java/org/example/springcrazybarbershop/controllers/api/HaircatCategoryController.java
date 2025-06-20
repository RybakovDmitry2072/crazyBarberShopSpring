package org.example.springcrazybarbershop.controllers.api;

import lombok.RequiredArgsConstructor;
import org.example.springcrazybarbershop.dto.HaircutCategoryDto;
import org.example.springcrazybarbershop.services.interfeces.IHaircutCategoryService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/haircut-categories")
public class HaircatCategoryController {

    private final IHaircutCategoryService haircutCategoryService;

    @GetMapping
    public Page<HaircutCategoryDto> getAllCategories(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        return haircutCategoryService.getAllHaircutCategory(page, size);
    }
}
