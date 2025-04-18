package com.excercises.swiftAPI.models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryDTO {
    private String countryISO2;
    private String countryName;
    private List<ReducedBankDTO> swiftCodes;
}
