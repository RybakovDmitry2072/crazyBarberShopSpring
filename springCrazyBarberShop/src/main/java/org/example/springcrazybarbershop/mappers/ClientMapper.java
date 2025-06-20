package org.example.springcrazybarbershop.mappers;

import org.example.springcrazybarbershop.dto.ClientDto;
import org.example.springcrazybarbershop.dto.UsersImagesDto;
import org.example.springcrazybarbershop.models.Client;
import org.example.springcrazybarbershop.models.File;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ClientMapper {

    private final FileMapper fileMapper;

    public ClientDto toClientDto(Client client) {
        if (client == null) {
            return null;
        }

        UsersImagesDto imageDto = null;
        if (client.getImage() != null) {
            imageDto = UsersImagesDto.builder()
                    .id(client.getImage().getId())
                    .storageFileName(client.getImage().getStorageFileName())
                    .originalFileName(client.getImage().getOriginalFileName())
                    .contentType(client.getImage().getContentType())
                    .size(client.getImage().getSize())
                    .userId(client.getId())
                    .build();
        }

        return ClientDto.builder()
                .id(client.getId())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .email(client.getEmail())
                .phone(client.getPhone())
                .createdAt(client.getCreatedAt())
                .roles(client.getRoles())
                .image(imageDto)
                .build();
    }

    public Client toClient(ClientDto dto) {
        if (dto == null) {
            return null;
        }

        File image = null;
        if (dto.getImage() != null) {
            image = new File();
            image.setId(dto.getImage().getId());
            image.setStorageFileName(dto.getImage().getStorageFileName());
            image.setOriginalFileName(dto.getImage().getOriginalFileName());
            image.setContentType(dto.getImage().getContentType());
            image.setSize(dto.getImage().getSize());
        }

        return Client.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .createdAt(dto.getCreatedAt())
                .image(image)
                .build();
    }
}