package org.example.springcrazybarbershop.dto.form.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInForm {

    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @NotEmpty
    private String password;
}
