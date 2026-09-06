package com.guardia.annotation;

import com.guardia.Guardia;
import com.guardia.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link com.guardia.annotation.MinLength} annotation.
 * Verifies minimum length enforcement, null tolerance, and custom messages.
 */
class MinLengthValidatorTest {

    static class Model {
        @MinLength(value = 3)
        String value;
    }

    // ==================== Fail cases ====================

    @Test
    void shouldFailWhenValueIsTooShort() {
        Model model = new Model();
        model.value = "ab";

        var context = Guardia.of(model).validate();

        assertFalse(context.isValid());
    }

    @Test
    void shouldFailWhenValueIsEmpty() {
        Model model = new Model();
        model.value = "";

        var context = Guardia.of(model).validate();

        assertFalse(context.isValid());
    }

    @Test
    void shouldContainCorrectFieldName() {
        Model model = new Model();
        model.value = "ab";

        var context = Guardia.of(model).validate();

        assertTrue(context.getErrors().stream()
                .anyMatch(e -> e.getFieldName().equals("value")));
    }

    @Test
    void shouldThrowWhenValueIsTooShort() {
        Model model = new Model();
        model.value = "ab";

        assertThrows(ValidationException.class, () ->
                Guardia.of(model).validate().throwIfInvalid()
        );
    }

    // ==================== Pass cases ====================

    @Test
    void shouldPassWhenValueIsExactMinLength() {
        Model model = new Model();
        model.value = "abc";    // دقیقاً ۳

        assertTrue(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldPassWhenValueIsLongerThanMin() {
        Model model = new Model();
        model.value = "Arsam";

        assertTrue(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldPassWhenValueIsNull() {
        Model model = new Model();
        model.value = null;     // null چک @NotNull کنه

        assertTrue(Guardia.of(model).validate().isValid());
    }

    // ==================== Custom message ====================

    @Test
    void shouldUseCustomMessage() {
        class CustomModel {
            @MinLength(value = 5, message = "too short!")
            String value;
        }

        CustomModel model = new CustomModel();
        model.value = "ab";

        var context = Guardia.of(model).validate();

        assertTrue(context.getErrors().stream()
                .anyMatch(e -> e.getMessage().contains("too short!")));
    }
}