package com.excercises.swiftAPI.repositories;

import com.excercises.swiftAPI.models.BankEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class SwiftApiRepositoryTest {

    @Autowired
    private SwiftApiRepository swiftApiRepository;

    @Test
    public void SwiftCodeRepositoryTest_SaveSwiftCode() {

        BankEntity bankEntity1 = BankEntity.builder()
                .address("Cracow")
                .bankName("Royal Bank")
                .countryISO2("PL")
                .countryName("POLAND")
                //.isHeadquarter(false)
                .swiftCode("12345678ABC")
                .branches(null)
                .build();

        BankEntity savedSwiftCode = swiftApiRepository.save(bankEntity1);

        Assertions.assertNotNull(savedSwiftCode);
        Assertions.assertNotNull(savedSwiftCode.getSwiftCode());
        Assertions.assertFalse(savedSwiftCode.isHeadquarter());
    }

    @Test
    public void SwiftCodeRepositoryTest_DeleteSwiftCode() {

        BankEntity bankEntity1 = BankEntity.builder()
                .address("Cracow")
                .bankName("Royal Bank")
                .countryISO2("PL")
                .countryName("POLAND")
                .swiftCode("12345678ABC")
                .branches(null)
                .build();

        BankEntity savedSwiftCode = swiftApiRepository.save(bankEntity1);
        Assertions.assertTrue(swiftApiRepository.findById(savedSwiftCode.getSwiftCode()).isPresent());

        swiftApiRepository.delete(savedSwiftCode);
        Assertions.assertFalse(swiftApiRepository.findById(savedSwiftCode.getSwiftCode()).isPresent());
    }

    @Test
    public void SwiftCodeRepositoryTest_FindFirstBankByCountryISO2() {

        BankEntity bankEntity1 = BankEntity.builder()
                .swiftCode("15935745XXX")
                .address("Berlin")
                .bankName("Bank A")
                .countryISO2("GE")
                .countryName("GERMANY")
                .build();

        BankEntity bankEntity2 = BankEntity.builder()
                .swiftCode("12345678XXX")
                .address("Warsaw")
                .bankName("Bank B")
                .countryISO2("PL")
                .countryName("POLAND")
                .build();

        BankEntity bankEntity3 = BankEntity.builder()
                .swiftCode("87654321BBB")
                .address("Cracow")
                .bankName("Bank C")
                .countryISO2("PL")
                .countryName("POLAND")
                .build();

        swiftApiRepository.save(bankEntity1);
        swiftApiRepository.save(bankEntity2);
        swiftApiRepository.save(bankEntity3);

        Optional<BankEntity> result = swiftApiRepository.findFirstByCountryISO2("PL");

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("12345678XXX", result.get().getSwiftCode());
    }

    @Test
    public void SwiftCodeRepositoryTest_FindAllBanksByCountryISO2() {

        BankEntity bankEntity1 = BankEntity.builder()
                .swiftCode("15935745XXX")
                .address("Berlin")
                .bankName("Bank A")
                .countryISO2("DE")
                .countryName("GERMANY")
                .build();

        BankEntity bankEntity2 = BankEntity.builder()
                .swiftCode("12345678AAA")
                .address("Warsaw")
                .bankName("Bank B")
                .countryISO2("PL")
                .countryName("POLAND")
                .build();

        BankEntity bankEntity3 = BankEntity.builder()
                .swiftCode("87654321BBB")
                .address("Cracow")
                .bankName("Bank C")
                .countryISO2("PL")
                .countryName("POLAND")
                .build();

        swiftApiRepository.save(bankEntity1);
        swiftApiRepository.save(bankEntity2);
        swiftApiRepository.save(bankEntity3);

        List<BankEntity> result = swiftApiRepository.findAllBanksByCountryISO2("PL");

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.stream().anyMatch(bank -> bank.getSwiftCode().equals("12345678AAA")));
        Assertions.assertTrue(result.stream().anyMatch(bank -> bank.getSwiftCode().equals("87654321BBB")));
    }

    @Test
    public void SwiftCodeRepositoryTest_FindAll() {

        BankEntity bankEntity1 = BankEntity.builder()
                .swiftCode("15935745XXX")
                .address("Berlin")
                .bankName("Bank A")
                .countryISO2("DE")
                .countryName("GERMANY")
                .build();

        BankEntity bankEntity2 = BankEntity.builder()
                .swiftCode("12345678AAA")
                .address("Warsaw")
                .bankName("Bank B")
                .countryISO2("PL")
                .countryName("POLAND")
                .build();

        BankEntity bankEntity3 = BankEntity.builder()
                .swiftCode("87654321BBB")
                .address("Cracow")
                .bankName("Bank C")
                .countryISO2("PL")
                .countryName("POLAND")
                .build();

        swiftApiRepository.save(bankEntity1);
        swiftApiRepository.save(bankEntity2);
        swiftApiRepository.save(bankEntity3);

        List<BankEntity> result = swiftApiRepository.findAll();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(3, result.size());
        Assertions.assertTrue(result.stream().anyMatch(bank -> bank.getSwiftCode().equals("12345678AAA")));
        Assertions.assertTrue(result.stream().anyMatch(bank -> bank.getSwiftCode().equals("87654321BBB")));
        Assertions.assertTrue(result.stream().anyMatch(bank -> bank.getSwiftCode().equals("15935745XXX")));
    }

    @Test
    public void SwiftCodeRepositoryTest_FindById() {

        BankEntity bankEntity1 = BankEntity.builder()
                .swiftCode("15935745XXX")
                .address("Berlin")
                .bankName("Bank A")
                .countryISO2("DE")
                .countryName("GERMANY")
                .build();

        BankEntity bankEntity2 = BankEntity.builder()
                .swiftCode("12345678AAA")
                .address("Warsaw")
                .bankName("Bank B")
                .countryISO2("PL")
                .countryName("POLAND")
                .build();

        BankEntity bankEntity3 = BankEntity.builder()
                .swiftCode("87654321BBB")
                .address("Cracow")
                .bankName("Bank C")
                .countryISO2("PL")
                .countryName("POLAND")
                .build();

        swiftApiRepository.save(bankEntity1);
        swiftApiRepository.save(bankEntity2);
        swiftApiRepository.save(bankEntity3);

        Optional<BankEntity> result = swiftApiRepository.findById("12345678AAA");

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("12345678AAA", result.get().getSwiftCode());
    }

    @Test
    public void testExistsBySwiftCode_ExistingSwiftCode() {

        BankEntity bankEntity = BankEntity.builder()
                .swiftCode("12345678ABC")
                .address("Warsaw")
                .bankName("Bank B")
                .countryISO2("PL")
                .countryName("POLAND")
                .build();

        swiftApiRepository.save(bankEntity);

        boolean exists = swiftApiRepository.existsBySwiftCode("12345678ABC");

        Assertions.assertTrue(exists, "Bank with the provided SWIFT code should exist.");
    }

    @Test
    public void testExistsBySwiftCode_NonExistingSwiftCode() {
        boolean exists = swiftApiRepository.existsBySwiftCode("00000000XXX");
        Assertions.assertFalse(exists, "Bank with the provided SWIFT code should not exist.");
    }
}
