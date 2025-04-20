package com.excercises.swiftAPI.components.validators;

import com.excercises.swiftAPI.exceptions.InvalidDataException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CountryCodeValidator {

    @Value("${file.iso2data.path}")
    private String iso2dataPath;

    private final Map<String, String> countryCodeToName = new HashMap<>();

    @PostConstruct
    public void init() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(iso2dataPath))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    countryCodeToName.put(parts[1].trim().toUpperCase(), parts[0].trim().toUpperCase());
                }
            }
        }
    }

    public void validateCountryCodeAndName(String iso2, String name) {
        String expectedName = countryCodeToName.get(iso2.toUpperCase());
        if (expectedName == null) {
            throw new InvalidDataException("Unknown country ISO2 code: " + iso2);
        }
        if (!expectedName.equalsIgnoreCase(name.trim())) {
            throw new InvalidDataException("Country name '" + name + "' does not match ISO2 code '" + iso2 +
                    "'. Expected country name: " + expectedName);
        }
    }
}
