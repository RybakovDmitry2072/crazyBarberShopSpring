package org.example.springcrazybarbershop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.springcrazybarbershop.models.Role;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private LocalDateTime createdAt;

    private UsersImagesDto image;

    private Set<Role> roles;
}



