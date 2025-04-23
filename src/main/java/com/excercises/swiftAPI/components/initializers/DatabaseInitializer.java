package com.excercises.swiftAPI.components.initializers;

import com.excercises.swiftAPI.components.importers.CsvDataImporter;
import com.excercises.swiftAPI.exceptions.CsvProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {

    private final CsvDataImporter csvDataImporter;

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    @Autowired
    public DatabaseInitializer(CsvDataImporter csvDataImporter) {
        this.csvDataImporter = csvDataImporter;
        LoadBanks();
    }

    private void LoadBanks() {
        try {
            csvDataImporter.uploadDatabaseFromLocalFile();
            logger.info("Bank data loaded successfully from CSV.");
        } catch (CsvProcessingException e) {
            logger.error("!!! Could not load bank data from CSV: {}", e.getMessage());
        }
    }
}

