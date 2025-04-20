package com.excercises.swiftAPI.components;

import com.excercises.swiftAPI.components.validators.CountryCodeValidator;
import com.excercises.swiftAPI.exceptions.InvalidDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.net.URL;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class CountryCodeValidatorTest {
    private CountryCodeValidator validator;

    @BeforeEach
    void setUp() throws Exception {
        validator = new CountryCodeValidator();

        URL resource = getClass().getClassLoader().getResource("test_ISO2DATA.csv");
        assertNotNull(resource, "Test resource not found");

        String path = Paths.get(resource.toURI()).toString();
        ReflectionTestUtils.setField(validator, "iso2dataPath", path);

        validator.init();
    }

    @Test
    void shouldPassValidationForCorrectCountryCodeAndName() {
        assertDoesNotThrow(() -> validator.validateCountryCodeAndName("PL", "Poland"));
        assertDoesNotThrow(() -> validator.validateCountryCodeAndName("de", "germany")); // case-insensitive
    }

    @Test
    void shouldThrowForUnknownCode() {
        InvalidDataException ex = assertThrows(
                InvalidDataException.class,
                () -> validator.validateCountryCodeAndName("ZZ", "Nowhere")
        );
        assertTrue(ex.getMessage().contains("Unknown country ISO2 code"));
    }

    @Test
    void shouldThrowForMismatchedName() {
        InvalidDataException ex = assertThrows(
                InvalidDataException.class,
                () -> validator.validateCountryCodeAndName("PL", "Germany")
        );
        assertTrue(ex.getMessage().contains("does not match ISO2 code"));
    }
}
