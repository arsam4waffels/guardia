package com.guardia;

import com.guardia.annotation.*;

public class Main {
    public static void main(String[] args) {
        User user = new User();
        user.name = "Arsam";
        user.email = "not-an-email";
        user.random = 123;
        user.positiveInteger = 5;

        var context = Guardia.of(user).validate();
        if (!context.isValid()) {
            context.getErrors().forEach(e ->
                    System.out.println("[FAIL] " + e.getFieldName() + ": " + e.getMessage())
            );
        }
    }
}
class User {

    @NotNull
    @MinLength(value = 3, message = "Name too short")
    String name;

    @NotNull
    @Email
    String email;

    @Range(min = 0, max = 100, message = "Number must be between 0 and 100")
    int random;

    @Positive
    int positiveInteger;
}