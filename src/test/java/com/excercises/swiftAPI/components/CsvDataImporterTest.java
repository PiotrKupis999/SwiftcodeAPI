package com.excercises.swiftAPI.components;

import com.excercises.swiftAPI.components.importers.CsvDataImporter;
import com.excercises.swiftAPI.models.BankEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CsvDataImporterTest {

    @Autowired
    private CsvDataImporter csvDataImporter;

    @Test
    public void DataServiceTest_UploadDatabaseFromLocalFile() {
        List<BankEntity> savedBanks = csvDataImporter.uploadDatabaseFromLocalFile();
        Assertions.assertNotNull(savedBanks);
        Assertions.assertFalse(savedBanks.isEmpty(), "Database upload returned an empty list!");
    }
}
