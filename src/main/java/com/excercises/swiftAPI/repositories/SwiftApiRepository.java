package com.excercises.swiftAPI.repositories;

import com.excercises.swiftAPI.models.BankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SwiftApiRepository extends JpaRepository<BankEntity, String> {
}
