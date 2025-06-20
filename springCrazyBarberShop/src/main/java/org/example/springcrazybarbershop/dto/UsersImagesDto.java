package org.example.springcrazybarbershop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UsersImagesDto {

    private Long id;

    private String storageFileName;

    private String originalFileName;

    private String contentType;

    private Long size;

    private String mimeType;

    private Long userId;
} 