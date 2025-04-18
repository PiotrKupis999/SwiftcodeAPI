package com.excercises.swiftAPI.services;

import com.excercises.swiftAPI.exceptions.BankNotFoundException;
import com.excercises.swiftAPI.exceptions.CountryNotFoundException;
import com.excercises.swiftAPI.models.BankEntity;
import com.excercises.swiftAPI.models.DTOs.BankDTO;
import com.excercises.swiftAPI.models.DTOs.CountryDTO;
import com.excercises.swiftAPI.models.DTOs.MapperToDTO;
import com.excercises.swiftAPI.repositories.SwiftApiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SwiftApiService {
    @Autowired
    SwiftApiRepository repository;
    @Autowired
    MapperToDTO mapper;

    public BankDTO getBankBySwiftCode(String swiftCode) {
        BankEntity foundBank = repository.findById(ifEightDigitSwiftToHeadquartersSwift(swiftCode))
                .orElseThrow(() -> new BankNotFoundException("No bank found with SWIFT code: " + swiftCode));
        foundBank.setBranches(findAllBranchesByHeadquartersSwiftCode(swiftCode));
        return mapper.toBankDTO(foundBank);
    }

    public CountryDTO findAllSwiftCodesWithDetailsByCountryISO2(String countryISO2) {
        List<BankEntity> foundBankEntities = findBankEntitiesByCountryISO2(countryISO2);
        if (foundBankEntities.isEmpty()) {
            throw new CountryNotFoundException("CountryDTO with " + countryISO2 + " ISO2 code - not found.");
        }
        return mapper.toCountryDTO(countryISO2, countryIso2ToName(countryISO2), foundBankEntities);
    }

    // If the provided SWIFT code is 8 characters (main SWIFT code), append "XXX" to return the headquarters SWIFT code.
    private String ifEightDigitSwiftToHeadquartersSwift(String swiftCode) {
        if(swiftCode.length() == 8) {
            swiftCode += "XXX";
        }
        return swiftCode;
    }

    private List<BankEntity> findAllBranchesByHeadquartersSwiftCode(String swiftCode) {
        String mainSwiftCodeOfHQ = swiftCode.substring(0, 8);
        return repository
                .findAll()
                .stream()
                .filter(bank -> bank.getSwiftCode().startsWith(mainSwiftCodeOfHQ) && !isBranchAHeadquarters(bank.getSwiftCode()))
                .collect(Collectors.toList());
    }

    private boolean isBranchAHeadquarters(String swiftCode) {
        if (swiftCode != null && swiftCode.length() >= 3) {
            String suffix = swiftCode.substring(swiftCode.length() - 3);
            return "XXX".equals(suffix);
        }
        return false;
    }

    public List<BankEntity> saveAllBankEntitiesFromListToDatabase(List<BankEntity> bankEntities) {
        return bankEntities.stream()
                .peek(this::prepareBankEntityBeforeSave)
                .map(repository::save)
                .collect(Collectors.toList());
    }

    private void prepareBankEntityBeforeSave(BankEntity bankEntity) {
        uppercaseSwiftISO2CountryOfBankEntity(bankEntity);
        trimWhiteSpaces(bankEntity);
        bankEntity.setHeadquarter(isBranchAHeadquarters(bankEntity.getSwiftCode()));
    }

    private void uppercaseSwiftISO2CountryOfBankEntity(BankEntity bankEntity) {
        bankEntity.setSwiftCode(bankEntity.getSwiftCode().toUpperCase());
        bankEntity.setCountryName(bankEntity.getCountryName().toUpperCase());
        bankEntity.setCountryISO2(bankEntity.getCountryISO2().toUpperCase());
    }

    private void trimWhiteSpaces(BankEntity bankEntity) {
        if (bankEntity.getAddress() != null) {
            bankEntity.setAddress(bankEntity.getAddress().trim());
        }
    }

    private List<BankEntity> findBankEntitiesByCountryISO2(String countryISO2) {
        return repository.findAllBanksByCountryISO2(countryISO2);
    }

    private String countryIso2ToName(String countryIso2) {
        return repository.findFirstByCountryISO2(countryIso2)
                .map(BankEntity::getCountryName)
                .orElse(null);
    }
}
