package com.excercises.swiftAPI.services;

import com.excercises.swiftAPI.components.mappers.MapperToDTO;
import com.excercises.swiftAPI.components.validators.CountryCodeValidator;
import com.excercises.swiftAPI.exceptions.BankNotFoundException;
import com.excercises.swiftAPI.exceptions.CountryNotFoundException;
import com.excercises.swiftAPI.exceptions.InvalidDataException;
import com.excercises.swiftAPI.exceptions.ResourceAlreadyExistsException;
import com.excercises.swiftAPI.models.BankEntity;
import com.excercises.swiftAPI.models.DTOs.BankDTO;
import com.excercises.swiftAPI.models.DTOs.CountryDTO;
import com.excercises.swiftAPI.repositories.SwiftApiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SwiftApiService {
    @Autowired
    SwiftApiRepository repository;
    @Autowired
    MapperToDTO mapper;
    @Autowired
    CountryCodeValidator countryCodeValidator;

    public BankDTO getBankBySwiftCode(String swiftCode) {
        BankEntity foundBank = repository.findById(ifEightDigitSwiftToHeadquartersSwift(swiftCode))
                .orElseThrow(() -> new BankNotFoundException("No bank found with SWIFT code: " + swiftCode));
        foundBank.setBranches(findAllBranchesByHeadquartersSwiftCode(swiftCode));
        return mapper.toBankDTO(foundBank);
    }

    public CountryDTO findAllSwiftCodesWithDetailsByCountryISO2(String countryISO2) {
        if (countryISO2.length() > 2) {
            countryISO2 = countryCodeValidator.countryNametoISO2(countryISO2);
        }
        List<BankEntity> foundBankEntities = findBankEntitiesByCountryISO2(countryISO2);
        if (foundBankEntities.isEmpty()) {
            throw new CountryNotFoundException("Country with " + countryISO2 + " ISO2 code - not found.");
        }
        return mapper.toCountryDTO(countryISO2, countryIso2ToName(countryISO2), foundBankEntities);
    }

    public ResponseEntity<Map<String, String>> addBankEntityToDatabase(BankEntity bankEntity) {
        try {
            uppercaseSwiftISO2CountryOfBankEntity(bankEntity);
            countryCodeValidator.validateCountryCodeAndName(bankEntity.getCountryISO2(), bankEntity.getCountryName());
            swiftCodeValidationOfBank(bankEntity);
            repository.save(bankEntity);
            return ResponseEntity.ok(Map.of("message", "SWIFT Code added successfully"));
        } catch (Exception e) {
            throw new InvalidDataException(e.getMessage());
        }
    }

    public Map<String, String> deleteBankEntityFromDatabase(String swiftCode) {
        StringBuilder message = new StringBuilder();
        String normalizedSwift = ifEightDigitSwiftToHeadquartersSwift(swiftCode);
        BankEntity foundBank = repository.findById(normalizedSwift)
                .orElseThrow(() -> new BankNotFoundException("No bank found with SWIFT code: " + swiftCode));
        if (swiftCode.length() == 8) {
            message.append("Inserted code was eight digit so deleted SWIFT Code is ")
                    .append(swiftCode).append("XXX. ");
        }
        repository.delete(foundBank);
        if (foundBank.isHeadquarter()) {
            message.append("Deleted headquarter ").append(foundBank.getSwiftCode());
            List<BankEntity> associatedBranches = findAssociatedBranches(foundBank);
            deleteBranches(associatedBranches);
            appendBranchDeletionMessage(associatedBranches, message);
        } else {
            message.append(foundBank.getSwiftCode()).append(" branch SWIFT code has been deleted successfully");
        }
        return Map.of("message", message.toString());
    }

    private List<BankEntity> findAssociatedBranches(BankEntity headquarters) {
        String swiftPrefix = headquarters.getSwiftCode().substring(0, 8);
        return repository.findBySwiftCodeStartingWith(swiftPrefix).stream()
                .filter(branch -> !branch.getSwiftCode().equals(headquarters.getSwiftCode()))
                .toList();
    }

    private void deleteBranches(List<BankEntity> branches) {
        for (BankEntity branch : branches) {
            repository.delete(branch);
        }
    }

    private void appendBranchDeletionMessage(List<BankEntity> branches, StringBuilder message) {
        if (!branches.isEmpty()) {
            message.append(" and associated branches: ");
            for (BankEntity branch : branches) {
                message.append(branch.getSwiftCode().substring(8, 11)).append(", ");
            }
        }
    }

    // If the provided SWIFT code is 8 characters (main SWIFT code), append "XXX" to return the headquarters SWIFT code.
    private String ifEightDigitSwiftToHeadquartersSwift(String swiftCode) {
        if (swiftCode.length() == 8) {
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

    public void swiftCodeValidationOfBank(BankEntity bankEntity) {
        String swiftCode = bankEntity.getSwiftCode();
        if (repository.existsBySwiftCode(swiftCode)) {
            throw new ResourceAlreadyExistsException("Invalid SWIFT code: already exists.");
        }
        if ((swiftCode.length() != 11 && swiftCode.length() != 8) || !swiftCode.matches("[A-Z0-9]+")) {
            throw new InvalidDataException("Invalid SWIFT code: must be 8 or 11 alphanumeric characters.");
        }
        if (swiftCode.length() == 8) {
            if (bankEntity.isHeadquarter()) {
                bankEntity.setSwiftCode(ifEightDigitSwiftToHeadquartersSwift(swiftCode));
            } else {
                throw new InvalidDataException("Invalid branch's SWIFT code: must be 11 alphanumeric characters.");
            }
        }
        if (bankEntity.isHeadquarter() ^ isBranchAHeadquarters(bankEntity.getSwiftCode())) {
            throw new InvalidDataException("isHeadquarter input field is " + bankEntity.isHeadquarter()
                    + " while SWIFT code indicates it is " + isBranchAHeadquarters(bankEntity.getSwiftCode()));
        }
        if (!isBranchAHeadquarters(bankEntity.getSwiftCode())) {
            String headquartersCode = swiftCode.substring(0, 8) + "XXX";
            if (!repository.existsBySwiftCode(headquartersCode)) {
                throw new InvalidDataException("Cannot add Branch with code " + bankEntity.getSwiftCode()
                        + ". There is no Headquarters with code " + headquartersCode
                        + ". Branch must be associated to exact Headquarters");
            }
        }
    }
}
