package org.example.springcrazybarbershop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HaircutCategoryDto {

    private Long id;

    private String name;

    private Float price;

}
