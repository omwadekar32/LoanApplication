package com.loanconnect.api.repository;

import com.loanconnect.api.entity.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {

    Optional<LoanApplication> findByApplicationRef(String applicationRef);

    boolean existsByEmailAndLoanType(String email, String loanType);

    long countBySubmittedAtBetween(LocalDateTime start, LocalDateTime end);
}