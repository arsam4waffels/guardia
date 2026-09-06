package com.guardia.annotation;

import com.guardia.Guardia;
import com.guardia.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link com.guardia.annotation.Email} annotation.
 * Verifies email format validation, null tolerance, and custom messages.
 */
class EmailValidatorTest {

    static class Model {
        @Email
        String value;
    }

    // ==================== Fail cases ====================

    @Test
    void shouldFailWhenEmailHasNoAtSign() {
        Model model = new Model();
        model.value = "arsam.gmail.com";

        assertFalse(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldFailWhenEmailHasNoDomain() {
        Model model = new Model();
        model.value = "arsam@";

        assertFalse(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldFailWhenEmailHasNoDot() {
        Model model = new Model();
        model.value = "arsam@gmail";

        assertFalse(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldFailWhenEmailIsBlank() {
        Model model = new Model();
        model.value = "";

        assertFalse(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldThrowWhenEmailIsInvalid() {
        Model model = new Model();
        model.value = "not-an-email";

        assertThrows(ValidationException.class, () ->
                Guardia.of(model).validate().throwIfInvalid()
        );
    }

    // ==================== Pass cases ====================

    @Test
    void shouldPassWhenEmailIsValid() {
        Model model = new Model();
        model.value = "arsam@gmail.com";

        assertTrue(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldPassWhenEmailHasSubdomain() {
        Model model = new Model();
        model.value = "arsam@mail.gmail.com";

        assertTrue(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldPassWhenValueIsNull() {
        Model model = new Model();
        model.value = null;

        assertTrue(Guardia.of(model).validate().isValid());
    }

    // ==================== Custom message ====================

    @Test
    void shouldUseCustomMessage() {
        class CustomModel {
            @Email(message = "invalid email!")
            String value;
        }

        CustomModel model = new CustomModel();
        model.value = "bad-email";

        var context = Guardia.of(model).validate();

        assertTrue(context.getErrors().stream()
                .anyMatch(e -> e.getMessage().equals("invalid email!")));
    }
}