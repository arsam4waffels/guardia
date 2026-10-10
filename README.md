# Guardia

A lightweight, annotation-based Java validation library built from scratch.  
Guardia lets you validate any object using simple annotations — no configuration, no runtime dependencies.

```java
Guardia.of(user)
       .validate()
       .throwIfInvalid();
```

---

## Project Structure

```
src/main/java/com/guardia/
│   Guardia.java
│
├── annotation/
│   ├── NotNull.java
│   ├── NotBlank.java
│   ├── NotEmpty.java
│   ├── MinLength.java
│   ├── MaxLength.java
│   ├── Size.java
│   ├── Positive.java
│   ├── PositiveOrZero.java
│   ├── Email.java
│   ├── Range.java
│   └── Pattern.java
│
├── core/
│   ├── Constraint.java
│   ├── ConstraintValidator.java
│   ├── GuardiaContext.java
│   └── GuardiaEngine.java
│
├── exception/
│   ├── ValidationException.java
│   └── ValidationError.java
│
└── validator/
    ├── NotNullValidator.java
    ├── NotBlankValidator.java
    ├── NotEmptyValidator.java
    ├── MinLengthValidator.java
    ├── MaxLengthValidator.java
    ├── SizeValidator.java
    ├── PositiveValidator.java
    ├── PositiveOrZeroValidator.java
    ├── EmailValidator.java
    ├── RangeValidator.java
    └── PatternValidator.java
```

---

## Quick Start

**1. Annotate your model:**

```java
public class User {

    @NotBlank
    @Size(min = 3, max = 50, message = "Name length is invalid")
    private String name;

    @NotBlank
    @Email
    private String email;

    @Pattern(regex = "^[A-Z]{2}\\d{4}$", message = "Code must match the required format")
    private String code;

    @PositiveOrZero(message = "Balance cannot be negative")
    private double balance;

    @Range(min = 18, max = 100, message = "Age must be between 18 and 100")
    private int age;
}
```

`@NotEmpty` rejects `null` and empty strings, while `@NotBlank` also rejects whitespace-only strings. `@Size` validates the size of strings, collections, maps, and arrays. `@Positive` requires a value greater than zero; `@PositiveOrZero` allows zero as well. `@Email` validates the email format, `@Range` validates numeric bounds, and `@Pattern` validates strings against a regular expression.

**2. Validate:**

```java
User user = new User();
user.setName("Arsam");
user.setEmail("arsam@waffels.com");
user.setAge(21);
user.setBalance(0);

Guardia.of(user)
       .validate()
       .throwIfInvalid();

// Or inspect validation errors
var context = Guardia.of(user).validate();

if (!context.isValid()) {
    context.getErrors().forEach(error ->
        System.out.println("[FAIL] " + error.getFieldName() + ": " + error.getMessage())
    );
}
```

---

## Built-in Annotations

All built-in constraints target fields and are retained at runtime for reflection-based validation.

| Annotation | Value | Description |
|---|---|---|
| `@NotNull` | — | Field must not be null |
| `@NotBlank` | — | String must not be null, empty, or whitespace-only |
| `@NotEmpty` | — | String must not be null or empty; whitespace-only strings are allowed |
| `@MinLength(value)` | `int` | String length must be at least `value` |
| `@MaxLength(value)` | `int` | String length must be at most `value` |
| `@Size(min, max)` | `int` | Size must be within inclusive bounds for strings, collections, maps, and arrays |
| `@Email` | — | String must be a valid email address |
| `@Range(min, max)` | `long` | Number must be within the inclusive range |
| `@Positive` | — | Number must be greater than zero |
| `@PositiveOrZero` | — | Number must be zero or greater |
| `@Pattern(regex)` | `String` | String must match the supplied regular expression |

### Size validation

`@Size` supports `CharSequence`, `Collection`, `Map`, and array fields. The bounds are inclusive; `min` defaults to `0`, and `max` defaults to `Integer.MAX_VALUE`.

```java
@Size(min = 1, max = 10)
private List<String> tags;

@Size(min = 2, max = 5)
private String code;
```

A `null` value is considered valid by `@Size`; combine it with `@NotNull` or another presence constraint when the field is required. Invalid bounds (`min < 0` or `max < min`) are rejected.

### Null handling

`@MinLength`, `@MaxLength`, `@Size`, `@Positive`, `@PositiveOrZero`, and `@Pattern` treat `null` as valid. Use `@NotNull`, `@NotEmpty`, or `@NotBlank` when a value is required.

For example:

```java
@NotNull
@PositiveOrZero
private Integer retryCount;
```

This separates two rules: the value must exist, and when present, it must not be negative.

---

## Adding a Custom Annotation

Guardia is designed to be extended. Adding a new constraint does not require modifying the validation engine.

**1. Create the annotation:**

```java
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EvenValidator.class)
public @interface Even {
    String message() default "Value must be even";
}
```

**2. Create the validator:**

```java
public class EvenValidator implements ConstraintValidator<Even, Number> {

    private String message;

    @Override
    public void initialize(Even annotation) {
        this.message = annotation.message();
    }

    @Override
    public boolean isValid(Number value) {
        if (value == null) return true;
        return value.longValue() % 2 == 0;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
```

**3. Use it:**

```java
@Even
private int quantity;
```

No validation engine changes are needed.

---

## How It Works

```
Built-in annotations
        ↓
   @Constraint
        ↓
Links annotation to validator
        ↓
   GuardiaEngine
        ↓
Scans fields via Reflection
        ↓
ConstraintValidator<A,T>
        ↓
  ValidationError
        ↓
ValidationException (if invalid)
```

Each built-in annotation is linked to its validator through `@Constraint`. The engine discovers annotated fields at runtime, initializes the corresponding validator with the annotation, and collects validation failures as `ValidationError` objects.

---

## API

```java
Guardia.of(object)          // Create a validation context
       .validate()          // Run validation
       .isValid()           // true / false
       .getErrors()         // List<ValidationError>
       .throwIfInvalid()    // Throws ValidationException if invalid
       .get()               // Returns the original object
```

---

## Tech

- **Java 17+**
- **Zero runtime dependencies**

---

## What's Next

Guardia is in early beta. Possible future directions:

- Nested object validation
- Collection element validation
- Custom error message templates
- More built-in constraints
