package com.guardia.annotation;

import com.guardia.Guardia;
import com.guardia.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PatternValidatorTest {

    static class Model {
        @Pattern(regex = "^[A-Z]{2}\\d{4}$")
        String value;
    }

    @Test
    void shouldPassWhenValueMatchesPattern() {
        Model model = new Model();
        model.value = "AB1234";

        assertTrue(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldFailWhenValueDoesNotMatchPattern() {
        Model model = new Model();
        model.value = "ab1234";

        assertFalse(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldFailWhenValueHasWrongLength() {
        Model model = new Model();
        model.value = "ABC1234";

        assertFalse(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldPassWhenValueIsNull() {
        Model model = new Model();
        model.value = null;

        assertTrue(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldThrowWhenValueDoesNotMatchPattern() {
        Model model = new Model();
        model.value = "invalid";

        assertThrows(ValidationException.class, () ->
                Guardia.of(model).validate().throwIfInvalid()
        );
    }

    @Test
    void shouldUseCustomMessage() {
        class CustomModel {
            @Pattern(regex = "^\\d+$", message = "Only digits are allowed")
            String value;
        }

        CustomModel model = new CustomModel();
        model.value = "abc";

        var context = Guardia.of(model).validate();

        assertTrue(context.getErrors().stream()
                .anyMatch(error -> error.getMessage().equals("Only digits are allowed")));
    }

    @Test
    void shouldSupportDifferentRegexes() {
        class CustomModel {
            @Pattern(regex = "^[a-z]+$")
            String value;
        }

        CustomModel model = new CustomModel();
        model.value = "guardia";

        assertTrue(Guardia.of(model).validate().isValid());
    }
}
