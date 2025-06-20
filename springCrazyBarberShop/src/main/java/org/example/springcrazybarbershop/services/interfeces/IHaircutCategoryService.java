package org.example.springcrazybarbershop.services.interfeces;

import org.example.springcrazybarbershop.dto.HaircutCategoryDto;
import org.example.springcrazybarbershop.models.HaircutCategory;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IHaircutCategoryService {

    Page<HaircutCategoryDto> getAllHaircutCategory(int page, int size);
    
    // CRUD operations
    List<HaircutCategory> findAll();
    HaircutCategory save(HaircutCategory category);
    void deleteById(Long id);
    Optional<HaircutCategory> findById(Long id);
}
