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

@Service
public class LoanApplicationService {

    private final LoanApplicationRepository repository;

    public LoanApplicationService(LoanApplicationRepository repository) {
        this.repository = repository;
    }

    public LoanApplicationResponse submitApplication(LoanApplicationRequest request) {

        LoanApplication entity = new LoanApplication();

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

        // First save to get the auto-generated ID
        LoanApplication saved = repository.save(entity);

        // Generate unique application reference
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        saved.setApplicationRef(
                String.format("APP-%s-%04d", date, saved.getId())
        );

        // Save again with the reference
        saved = repository.save(saved);

        return LoanApplicationResponse.fromEntity(saved);
    }

    public List<LoanApplication> getAllApplications() {
        return repository.findAll();
    }

    public LoanApplication getApplicationById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Loan application not found with id: " + id));
    }

    public LoanApplication getApplicationByRef(String applicationRef) {
        return repository.findByApplicationRef(applicationRef)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Loan application not found with ref: " + applicationRef));
    }

    public void deleteApplication(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Loan application not found with id: " + id);
        }
        repository.deleteById(id);
    }
}