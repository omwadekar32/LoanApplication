package com.loanconnect.api.dto;

import com.loanconnect.api.entity.LoanApplication;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * What the API sends back to the frontend — enough to show a confirmation
 * and reference number, without echoing every internal field.
 */
public class LoanApplicationResponse {

    private Long id;
    private String applicationRef;
    private String fullName;
    private String email;
    private String loanType;
    private BigDecimal loanAmount;
    private String status;
    private LocalDateTime submittedAt;

    public static LoanApplicationResponse fromEntity(LoanApplication entity) {
        LoanApplicationResponse res = new LoanApplicationResponse();
        res.id = entity.getId();
        res.applicationRef = entity.getApplicationRef();
        res.fullName = entity.getFullName();
        res.email = entity.getEmail();
        res.loanType = entity.getLoanType();
        res.loanAmount = entity.getLoanAmount();
        res.status = entity.getStatus();
        res.submittedAt = entity.getSubmittedAt();
        return res;
    }

    // ---------- Getters & Setters ----------

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getApplicationRef() { return applicationRef; }
    public void setApplicationRef(String applicationRef) { this.applicationRef = applicationRef; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLoanType() { return loanType; }
    public void setLoanType(String loanType) { this.loanType = loanType; }

    public BigDecimal getLoanAmount() { return loanAmount; }
    public void setLoanAmount(BigDecimal loanAmount) { this.loanAmount = loanAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
}
