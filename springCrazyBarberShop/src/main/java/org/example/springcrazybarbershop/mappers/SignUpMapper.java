package org.example.springcrazybarbershop.mappers;

import org.example.springcrazybarbershop.dto.form.auth.SignUpForm;
import org.example.springcrazybarbershop.models.Client;
import org.example.springcrazybarbershop.models.Role;
import org.example.springcrazybarbershop.models.UserRole;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.MappingTarget;
import org.mapstruct.AfterMapping;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Mapper(componentModel = "spring", imports = {Role.class, Set.class})
public interface SignUpMapper {

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "state", constant = "CONFIRMED")
    @Mapping(target = "password", expression = "java(encodePassword(signUpForm.getPassword(), passwordEncoder))")
    Client toClient(SignUpForm signUpForm, @Context PasswordEncoder passwordEncoder);

    @AfterMapping
    default void setDefaultRoles(@MappingTarget Client client) {
        client.setRoles(getDefaultRoles());
    }

    @Named("encodePassword")
    default String encodePassword(String rawPassword, @Context PasswordEncoder passwordEncoder) {
        return passwordEncoder.encode(rawPassword);
    }

    default Set<Role> getDefaultRoles() {
        return Set.of(Role.USER, Role.CLIENT);
    }
}
