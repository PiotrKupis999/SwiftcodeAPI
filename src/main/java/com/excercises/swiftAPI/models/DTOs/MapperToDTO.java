package com.excercises.swiftAPI.models.DTOs;

import com.excercises.swiftAPI.models.BankEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MapperToDTO {
    public ReducedBankDTO toReducedBankDTO(BankEntity bankEntity) {
        return ReducedBankDTO.builder()
                .swiftCode(bankEntity.getSwiftCode())
                .address(bankEntity.getAddress())
                .bankName(bankEntity.getBankName())
                .countryISO2(bankEntity.getCountryISO2())
                .isHeadquarter(bankEntity.isHeadquarter())
                .build();
    }

    public BankDTO toBankDTO(BankEntity bankEntity) {
        List<ReducedBankDTO> reducedBranches = bankEntity.getBranches().stream()
                .map(this::toReducedBankDTO)
                .toList();

        return BankDTO.builder()
                .address(bankEntity.getAddress())
                .bankName(bankEntity.getBankName())
                .countryISO2(bankEntity.getCountryISO2())
                .countryName(bankEntity.getCountryName())
                .isHeadquarter(bankEntity.isHeadquarter())
                .swiftCode(bankEntity.getSwiftCode())
                .branches(bankEntity.isHeadquarter() ? reducedBranches : null)
                .build();
    }
}
