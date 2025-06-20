package org.example.springcrazybarbershop.exceptions;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class UserAlreadyExistException extends Throwable {
    public UserAlreadyExistException(@NotNull @NotEmpty String s) {
    }
}
