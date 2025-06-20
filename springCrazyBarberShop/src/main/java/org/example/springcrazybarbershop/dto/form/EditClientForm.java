package org.example.springcrazybarbershop.dto.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.springcrazybarbershop.models.Role;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditClientForm {

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private Set<Role> roles;

    private MultipartFile imageFile;

    private boolean removeImage;
}
