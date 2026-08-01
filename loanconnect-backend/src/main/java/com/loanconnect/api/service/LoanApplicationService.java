package com.loanconnect.api.service;

import com.loanconnect.api.dto.LoanApplicationRequest;
import com.loanconnect.api.dto.LoanApplicationResponse;
import com.loanconnect.api.entity.LoanApplication;
import com.loanconnect.api.exception.ResourceNotFoundException;
import com.loanconnect.api.repository.LoanApplicationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class LoanApplicationService {

    private final LoanApplicationRepository repository;

    // Simple in-memory counter to make same-day reference numbers readable
    // (e.g. APP-20260801-0001). Fine for a single instance; for multiple
    // instances/replicas, generate the ref from a DB sequence instead.
    private final AtomicInteger dailyCounter = new AtomicInteger(1);

    public LoanApplicationService(LoanApplicationRepository repository) {
        this.repository = repository;
    }

    /** Maps the incoming request to an entity, saves it, and returns a response DTO. */
    public LoanApplicationResponse submitApplication(LoanApplicationRequest request) {
        LoanApplication entity = new LoanApplication();

        entity.setApplicationRef(generateApplicationRef());
        entity.setFullName(request.getFullName());
        entity.setMobile(request.getMobile());
        entity.setEmail(request.getEmail());
        entity.setDob(request.getDob());
        entity.setCity(request.getCity());
        entity.setLoanType(request.getLoanType());
        entity.setEmploymentType(request.getEmploymentType());
        entity.setCompanyName(request.getCompanyName());
        entity.setMonthlyIncome(request.getMonthlyIncome());
        entity.setLoanAmount(request.getLoanAmount());
        entity.setLoanTenure(request.getLoanTenure());
        entity.setLoanPurpose(request.getLoanPurpose());
        entity.setAadhaarFileName(request.getAadhaarFileName());
        entity.setPanFileName(request.getPanFileName());
        entity.setSalarySlipFileName(request.getSalarySlipFileName());
        entity.setTermsAccepted(request.isTermsAccepted());
        entity.setStatus("SUBMITTED");

        LoanApplication saved = repository.save(entity);
        return LoanApplicationResponse.fromEntity(saved);
    }

    public List<LoanApplication> getAllApplications() {
        return repository.findAll();
    }

    public LoanApplication getApplicationById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan application not found with id: " + id));
    }

    public LoanApplication getApplicationByRef(String applicationRef) {
        return repository.findByApplicationRef(applicationRef)
                .orElseThrow(() -> new ResourceNotFoundException("Loan application not found with ref: " + applicationRef));
    }

    public void deleteApplication(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Loan application not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private String generateApplicationRef() {
        String datePart = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE); // e.g. 20260801
        int seq = dailyCounter.getAndIncrement();
        return String.format("APP-%s-%04d", datePart, seq);
    }
}
