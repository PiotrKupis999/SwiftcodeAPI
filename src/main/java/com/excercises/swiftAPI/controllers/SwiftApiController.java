package com.excercises.swiftAPI.controllers;

import com.excercises.swiftAPI.models.DTOs.BankDTO;
import com.excercises.swiftAPI.services.SwiftApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/v1/swift-codes")
public class SwiftApiController {
    @Autowired
    private SwiftApiService swiftApiService;

    @GetMapping("/{swift-code}")
    @ResponseBody
    public ResponseEntity<BankDTO> getDetails(@PathVariable("swift-code") String swiftCode) {
        BankDTO found = swiftApiService.getBankBySwiftCode(swiftCode.toUpperCase());
        return ResponseEntity.ok(found);
    }
}
