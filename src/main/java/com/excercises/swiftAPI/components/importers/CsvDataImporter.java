package com.excercises.swiftAPI.components.importers;

import com.excercises.swiftAPI.exceptions.CsvProcessingException;
import com.excercises.swiftAPI.models.BankEntity;
import com.excercises.swiftAPI.services.SwiftApiService;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

@Component
public class CsvDataImporter {
    @Autowired
    SwiftApiService swiftApiService;

    @Value("${file.swift.path}")
    private String swiftFilePath;

    public List<BankEntity> uploadDatabaseFromLocalFile() {
        List<BankEntity> listOfBankEntities = allBankEntitiesFromCsvToList(swiftFilePath);
        return swiftApiService.saveAllBankEntitiesFromListToDatabase(listOfBankEntities);
    }

    private List<BankEntity> allBankEntitiesFromCsvToList(String csvFilePath) {
        try {
            return new CsvToBeanBuilder<BankEntity>(new FileReader(csvFilePath))
                    .withType(BankEntity.class)
                    .build()
                    .parse();
        } catch (FileNotFoundException e) {
            throw new CsvProcessingException("CSV file not found at: " + csvFilePath, e);
        } catch (Exception e) {
            throw new CsvProcessingException("Unexpected error processing CSV file (" + csvFilePath + ").", e);
        }
    }
}