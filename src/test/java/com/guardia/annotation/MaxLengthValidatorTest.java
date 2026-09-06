package com.guardia.annotation;

import com.guardia.Guardia;
import com.guardia.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link com.guardia.annotation.MaxLength} annotation.
 * Verifies maximum length enforcement, null tolerance, and custom messages.
 */
class MaxLengthValidatorTest {

    static class Model {
        @MaxLength(value = 5)
        String value;
    }

    // ==================== Fail cases ====================

    @Test
    void shouldFailWhenValueExceedsMax() {
        Model model = new Model();
        model.value = "toolongstring";

        assertFalse(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldFailWhenValueIsOneCharOverMax() {
        Model model = new Model();
        model.value = "123456";

        assertFalse(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldContainCorrectFieldName() {
        Model model = new Model();
        model.value = "toolong";

        var context = Guardia.of(model).validate();

        assertTrue(context.getErrors().stream()
                .anyMatch(e -> e.getFieldName().equals("value")));
    }

    @Test
    void shouldThrowWhenValueExceedsMax() {
        Model model = new Model();
        model.value = "toolong";

        assertThrows(ValidationException.class, () ->
                Guardia.of(model).validate().throwIfInvalid()
        );
    }

    // ==================== Pass cases ====================

    @Test
    void shouldPassWhenValueIsExactMaxLength() {
        Model model = new Model();
        model.value = "12345";

        assertTrue(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldPassWhenValueIsShorterThanMax() {
        Model model = new Model();
        model.value = "hi";

        assertTrue(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldPassWhenValueIsNull() {
        Model model = new Model();
        model.value = null;

        assertTrue(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldPassWhenValueIsEmpty() {
        Model model = new Model();
        model.value = "";

        assertTrue(Guardia.of(model).validate().isValid());
    }

    // ==================== Custom message ====================

    @Test
    void shouldUseCustomMessage() {
        class CustomModel {
            @MaxLength(value = 3, message = "too long!")
            String value;
        }

        CustomModel model = new CustomModel();
        model.value = "toolong";

        var context = Guardia.of(model).validate();

        assertTrue(context.getErrors().stream()
                .anyMatch(e -> e.getMessage().contains("too long!")));
    }
}