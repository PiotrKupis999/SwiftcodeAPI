package com.excercises.swiftAPI.components.initializers;

import com.excercises.swiftAPI.components.importers.CsvDataImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {

    private final CsvDataImporter csvDataImporter;

    @Autowired
    public DatabaseInitializer(CsvDataImporter csvDataImporter) {
        this.csvDataImporter = csvDataImporter;
        LoadBanks();
    }

    private void LoadBanks() {
        csvDataImporter.uploadDatabaseFromLocalFile();
    }
}

