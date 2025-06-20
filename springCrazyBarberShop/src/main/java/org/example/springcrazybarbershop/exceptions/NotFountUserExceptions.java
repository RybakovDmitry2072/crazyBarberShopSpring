package org.example.springcrazybarbershop.exceptions;

public class NotFountUserExceptions  extends NotFoundException {
    public NotFountUserExceptions() {
        super("No found employee");
    }
}
