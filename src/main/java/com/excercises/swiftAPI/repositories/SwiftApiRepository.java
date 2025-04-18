package com.excercises.swiftAPI.repositories;

import com.excercises.swiftAPI.models.BankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SwiftApiRepository extends JpaRepository<BankEntity, String> {
    List<BankEntity> findAllBanksByCountryISO2(String countryISO2);
    Optional<BankEntity> findFirstByCountryISO2(String countryISO2);
}
