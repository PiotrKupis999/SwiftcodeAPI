package com.excercises.swiftAPI.components.initializers;

import com.excercises.swiftAPI.components.importers.CsvDataImporter;
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
        logger.info("SWIFT Codes database loaded successfully");
    }

    private void LoadBanks() {
        csvDataImporter.uploadDatabaseFromLocalFile();
    }
}

