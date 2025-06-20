package org.example.springcrazybarbershop.mappers;

import org.example.springcrazybarbershop.dto.HaircutCategoryDto;
import org.example.springcrazybarbershop.models.HaircutCategory;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface HaircutCategoryMapper {

    HaircutCategoryDto toHaircutCategoryDto(HaircutCategory haircutCategory);

    HaircutCategory toHaircutCategory(HaircutCategoryDto haircutCategoryDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateHaircutCategoryFromDto(HaircutCategoryDto haircutCategoryDto, @MappingTarget HaircutCategory haircutCategory);
}
