package com.example.footballmanager.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CareerStartDateValidatorTest {
    private Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidPastDate_Ok() {
        LocalDate validDate = LocalDate.now().minusDays(1);

        TestClass testObject = new TestClass(validDate);
        Set<ConstraintViolation<TestClass>> violations = validator.validate(testObject);

        assertTrue(violations.isEmpty());
    }

    @Test
    public void testValidToday_Ok() {
        LocalDate validDate = LocalDate.now();

        TestClass testObject = new TestClass(validDate);
        Set<ConstraintViolation<TestClass>> violations = validator.validate(testObject);

        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidFutureDate_NotOk() {
        LocalDate futureDate = LocalDate.now().plusDays(1);

        TestClass testObject = new TestClass(futureDate);
        Set<ConstraintViolation<TestClass>> violations = validator.validate(testObject);

        assertFalse(violations.isEmpty());
    }

    @Test
    public void testInvalidNullDate_NotOk() {
        TestClass testObject = new TestClass(null);
        Set<ConstraintViolation<TestClass>> violations = validator.validate(testObject);

        assertFalse(violations.isEmpty());
    }

    private record TestClass(@PastOrPresent LocalDate startDate) {
    }
}
