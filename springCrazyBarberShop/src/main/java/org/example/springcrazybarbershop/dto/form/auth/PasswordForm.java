package org.example.springcrazybarbershop.dto.form.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PasswordForm {

    private String newPassword;

    private String oldPassword;

    private String token;

}
