package com.guardia.annotation;

import com.guardia.Guardia;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SizeValidatorTest {

    static class Model {
        @Size(min = 2, max = 5)
        String name;

        @Size(min = 1, max = 3)
        List<String> tags;

        @Size(min = 1, max = 2)
        Map<String, Integer> scores;

        @Size(min = 1, max = 3)
        int[] values;
    }

    @Test
    void shouldPassWhenStringLengthIsWithinBounds() {
        Model model = new Model();
        model.name = "Arsam";

        assertTrue(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldFailWhenStringIsTooShort() {
        Model model = new Model();
        model.name = "A";

        assertFalse(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldFailWhenStringIsTooLong() {
        Model model = new Model();
        model.name = "Arsams";
        
        assertFalse(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldValidateCollectionSize() {
        Model model = new Model();
        model.name = "OK";
        model.tags = List.of("java", "spring");

        assertTrue(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldValidateMapSize() {
        Model model = new Model();
        model.name = "OK";
        model.tags = List.of("java");
        model.scores = Map.of("math", 90);

        assertTrue(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldValidateArrayLength() {
        Model model = new Model();
        model.name = "OK";
        model.tags = List.of("java");
        model.scores = Map.of("math", 90);
        model.values = new int[] {1, 2};

        assertTrue(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldAllowNull() {
        Model model = new Model();
        model.name = null;

        assertTrue(Guardia.of(model).validate().isValid());
    }

    @Test
    void shouldUseCustomMessage() {
        class CustomModel {
            @Size(min = 2, max = 4, message = "name length is invalid")
            String name = "A";
        }

        var context = Guardia.of(new CustomModel()).validate();

        assertTrue(context.getErrors().stream()
                .anyMatch(error -> error.getMessage().startsWith("name length is invalid")));
    }

    @Test
    void shouldRejectInvalidBounds() {
        class InvalidModel {
            @Size(min = 5, max = 2)
            String value = "test";
        }

        assertThrows(RuntimeException.class, () ->
                Guardia.of(new InvalidModel()).validate()
        );
    }
}
