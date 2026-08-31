package com.guardia;

import com.guardia.annotation.Email;
import com.guardia.core.GuardiaContext;
import com.guardia.annotation.MinLength;
import com.guardia.annotation.NotNull;
import com.guardia.annotation.Range;
import com.guardia.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Yeah, I love coding... right up until I get to writing tests.
class GuardiaTest {
    static class User {
        @NotNull
        @MinLength(value = 3, message = "Name too short just like your d-")
        String name;

        @NotNull
        @Email
        String email;

        @Range(min = 18, max = 100, message = "Age out of range")
        int age;
    }
    @Test
    void shouldFailWhenNameIsNull() {
        User user = new User();
        user.name = null;
        user.email = "arsam@gmail.com";
        user.age = 21;

        var context = Guardia.of(user).validate();
        assertFalse(context.isValid());
        assertTrue(context.getErrors().stream()
                .anyMatch(e -> e.getFieldName().equals("name")));
    }
    @Test
    void shouldPassWhenAllFieldsAreValid() {
        User user = new User();
        user.name = "Arsam";
        user.email = "arsam@gmail.com";
        user.age = 21;

        var context = Guardia.of(user).validate();
        assertTrue(context.isValid());
        assertTrue(context.getErrors().isEmpty());
    }
    @Test
    void shouldFailWhenNameIsTooShort() {
        User user = new User();
        user.name = "dz"; // less than 3
        user.email = "arsam@gmail.com";
        user.age = 21;

        var context = Guardia.of(user).validate();
        assertFalse(context.isValid());
        assertTrue(context.getErrors().stream()
                .anyMatch(e -> e.getFieldName().equals("name")
                        && e.getMessage().contains("too short")));
    }
    @Test
    void shouldFailWhenEmailIsInvalid() {
        User user = new User();
        user.name = "Arsam";
        user.email = "I-love-my-cat"; // def not an e-mail... but a true fact
        user.age = 21;

        var context = Guardia.of(user).validate();
        assertFalse(context.isValid());
        assertTrue(context.getErrors().stream()
                .anyMatch(e -> e.getFieldName().equals("email")));
    }
    @Test
    void shouldPassWhenEmailIsValid() {
        User user = new User();
        user.name = "Arsam";
        user.email = "arsam@gmail.com";
        user.age = 21;

        assertTrue(Guardia.of(user).validate().isValid());
    }
    @Test
    void shouldFailWhenAgeIsBelowRange() {
        User user = new User();
        user.name = "Arsam";
        user.email = "arsam@gmail.com";
        user.age = 15; // less than 18

        var context = Guardia.of(user).validate();
        assertFalse(context.isValid());
        assertTrue(context.getErrors().stream()
                .anyMatch(e -> e.getFieldName().equals("age")));
    }
    @Test
    void shouldFailWhenAgeIsAboveRange() {
        User user = new User();
        user.name = "Arsam";
        user.email = "arsam@gmail.com";
        user.age = 150; // how are you even alive?

        assertFalse(Guardia.of(user).validate().isValid());
    }
    @Test
    void shouldThrowWhenInvalid() {
        User user = new User();
        user.name = null;       // null
        user.email = null;      // pure nothingness
        user.age = 21;          // valid

        assertThrows(ValidationException.class, () ->
                Guardia.of(user).validate().throwIfInvalid()
        );
    }
    @Test
    void shouldCollectMultipleErrors() {
        User user = new User();
        user.name = null;        // err
        user.email = "fish";     // err..
        user.age = 15;           // guess what

        var context = Guardia.of(user).validate();
        assertFalse(context.isValid());
        assertTrue(context.getErrors().size() >= 3);
    }
    @Test
    void shouldThrowWhenAccessingResultBeforeValidation() {
        User user = new User();
        user.name = "Arsam";
        user.email = "arsam@gmail.com";
        user.age = 21;

        GuardiaContext<User> context = Guardia.of(user);
        assertThrows(IllegalStateException.class, context::isValid); // peak coding
    }
}