package com.loanconnect.api.repository;

import com.loanconnect.api.entity.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * CRUD access to loan_applications. JpaRepository already provides
 * save(), findAll(), findById(), deleteById(), etc. — no implementation needed.
 */
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {

    Optional<LoanApplication> findByApplicationRef(String applicationRef);

    boolean existsByEmailAndLoanType(String email, String loanType);
}
