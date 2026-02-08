package com.billguardian.cfo.domain.repository;

import com.billguardian.cfo.domain.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
      boolean existsByHashIdentifier(String hashIdentifier);
}