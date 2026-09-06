package com.guardia.annotation;

import com.guardia.Guardia;
import com.guardia.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link com.guardia.annotation.NotNull} annotation.
 * Verifies null rejection, field name reporting, and custom messages.
 */
class NotNullValidatorTest {

    static class Model {
        @NotNull
        String value;
    }

    // ==================== Fail cases ====================

    @Test
    void shouldFailWhenValueIsNull() {
        Model model = new Model();
        model.value = null;

        var context = Guardia.of(model).validate();

        assertFalse(context.isValid());
    }

    @Test
    void shouldContainCorrectFieldNameWhenNull() {
        Model model = new Model();
        model.value = null;

        var context = Guardia.of(model).validate();

        assertTrue(context.getErrors().stream()
                .anyMatch(e -> e.getFieldName().equals("value")));
    }

    @Test
    void shouldThrowWhenValueIsNull() {
        Model model = new Model();
        model.value = null;

        assertThrows(ValidationException.class, () ->
                Guardia.of(model).validate().throwIfInvalid()
        );
    }

    // ==================== Pass cases ====================

    @Test
    void shouldPassWhenValueIsNotNull() {
        Model model = new Model();
        model.value = "Arsam";

        var context = Guardia.of(model).validate();

        assertTrue(context.isValid());
        assertTrue(context.getErrors().isEmpty());
    }

    // ==================== Custom message ====================

    @Test
    void shouldUseCustomMessage() {
        class CustomModel {
            @NotNull(message = "custom error")
            String value;
        }

        CustomModel model = new CustomModel();
        model.value = null;

        var context = Guardia.of(model).validate();

        assertTrue(context.getErrors().stream()
                .anyMatch(e -> e.getMessage().equals("custom error")));
    }
}