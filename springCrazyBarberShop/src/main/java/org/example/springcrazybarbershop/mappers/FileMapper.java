package org.example.springcrazybarbershop.mappers;

import jakarta.persistence.EntityNotFoundException;
import org.example.springcrazybarbershop.dto.form.FileForm;
import org.example.springcrazybarbershop.models.File;
import org.example.springcrazybarbershop.models.User;
import org.example.springcrazybarbershop.repositories.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class FileMapper {

    @Autowired
    protected UserRepository userRepository;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "storageFileName",
            source = "fileName",
            qualifiedByName = "storageName")
    @Mapping(target = "originalFileName", source = "fileName")
    @Mapping(target = "contentType", source = "contentType")
    @Mapping(target = "size", source = "size")
    @Mapping(target = "user", expression = "java(getUser(fileForm.getUserId()))")
    public abstract File fileFormToEntity(FileForm fileForm);

    @Named("storageName")
    protected String generateStorageFileName(String originalName) {
        if (originalName == null || originalName.isEmpty()) {
            return UUID.randomUUID().toString();
        }
        String extension = originalName.contains(".")
                ? originalName.substring(originalName.lastIndexOf("."))
                : "";
        return UUID.randomUUID() + extension;
    }

    protected User getUser(Long userId) {
        if (userId == null) {
            return null;
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с ID " + userId + " не найден"));
    }
}