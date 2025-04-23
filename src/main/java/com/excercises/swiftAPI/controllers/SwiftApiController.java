package com.excercises.swiftAPI.controllers;

import com.excercises.swiftAPI.models.BankEntity;
import com.excercises.swiftAPI.models.DTOs.BankDTO;
import com.excercises.swiftAPI.models.DTOs.CountryDTO;
import com.excercises.swiftAPI.services.SwiftApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/swift-codes")
public class SwiftApiController {

    private static final Logger logger = LoggerFactory.getLogger(SwiftApiController.class);

    @Autowired
    private SwiftApiService swiftApiService;

    @GetMapping("/{swift-code}")
    @ResponseBody
    public ResponseEntity<BankDTO> getDetails(@PathVariable("swift-code") String swiftCode) {
        logger.info("GET request received for SWIFT code: {}", swiftCode);
        BankDTO found = swiftApiService.getBankBySwiftCode(swiftCode.toUpperCase());
        return ResponseEntity.ok(found);
    }

    @GetMapping("/country/{countyISO2code}")
    @ResponseBody
    public ResponseEntity<CountryDTO> getBankEntitiesByCountryISO2code(@PathVariable("countyISO2code") String countyISO2code) {
        logger.info("GET request received for banks with country ISO2 code: {}", countyISO2code);
        CountryDTO foundCountryDTO = swiftApiService.findAllSwiftCodesWithDetailsByCountryISO2(countyISO2code.toUpperCase());
        return ResponseEntity.ok(foundCountryDTO);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Map<String, String>> addSwiftCode(@RequestBody BankEntity bankEntity) {
        logger.info("POST request to add SWIFT code for bank: {}", bankEntity.getSwiftCode());
        return swiftApiService.addBankEntityToDatabase(bankEntity);
    }

    @DeleteMapping("/{swift-code}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteSwiftCode(@PathVariable("swift-code") String swiftCode) {
        logger.info("DELETE request for SWIFT code: {}", swiftCode);
        Map<String, String> response = swiftApiService.deleteBankEntityFromDatabase(swiftCode.toUpperCase());
        return ResponseEntity.ok(response);
    }
}
