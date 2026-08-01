package com.loanconnect.api.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Shape of the JSON body the frontend "Apply for a Loan" form sends to
 * POST /api/loan-applications. Field names intentionally mirror the
 * `id` attributes used in index.html / script.js so the mapping stays obvious.
 */
public class LoanApplicationRequest {

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be exactly 10 digits")
    private String mobile;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    private String email;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Loan type is required")
    private String loanType;

    @NotBlank(message = "Employment type is required")
    private String employmentType;

    @NotBlank(message = "Company / business name is required")
    private String companyName;

    @NotNull(message = "Monthly income is required")
    @DecimalMin(value = "1", message = "Monthly income must be greater than zero")
    private BigDecimal monthlyIncome;

    @NotNull(message = "Loan amount is required")
    @DecimalMin(value = "1", message = "Loan amount must be greater than zero")
    private BigDecimal loanAmount;

    @NotNull(message = "Loan tenure is required")
    @Min(value = 1, message = "Loan tenure must be at least 1 year")
    private Integer loanTenure;

    @NotBlank(message = "Purpose of loan is required")
    @Size(min = 5, max = 1000, message = "Please describe the purpose of the loan")
    private String loanPurpose;

    // File inputs arrive as plain file-name strings from the frontend
    // (see README for switching this endpoint to multipart/form-data).
    private String aadhaarFileName;
    private String panFileName;
    private String salarySlipFileName;

    @AssertTrue(message = "You must agree to the Terms & Conditions")
    private boolean termsAccepted;

    // ---------- Getters & Setters ----------

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getLoanType() { return loanType; }
    public void setLoanType(String loanType) { this.loanType = loanType; }

    public String getEmploymentType() { return employmentType; }
    public void setEmploymentType(String employmentType) { this.employmentType = employmentType; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public BigDecimal getMonthlyIncome() { return monthlyIncome; }
    public void setMonthlyIncome(BigDecimal monthlyIncome) { this.monthlyIncome = monthlyIncome; }

    public BigDecimal getLoanAmount() { return loanAmount; }
    public void setLoanAmount(BigDecimal loanAmount) { this.loanAmount = loanAmount; }

    public Integer getLoanTenure() { return loanTenure; }
    public void setLoanTenure(Integer loanTenure) { this.loanTenure = loanTenure; }

    public String getLoanPurpose() { return loanPurpose; }
    public void setLoanPurpose(String loanPurpose) { this.loanPurpose = loanPurpose; }

    public String getAadhaarFileName() { return aadhaarFileName; }
    public void setAadhaarFileName(String aadhaarFileName) { this.aadhaarFileName = aadhaarFileName; }

    public String getPanFileName() { return panFileName; }
    public void setPanFileName(String panFileName) { this.panFileName = panFileName; }

    public String getSalarySlipFileName() { return salarySlipFileName; }
    public void setSalarySlipFileName(String salarySlipFileName) { this.salarySlipFileName = salarySlipFileName; }

    public boolean isTermsAccepted() { return termsAccepted; }
    public void setTermsAccepted(boolean termsAccepted) { this.termsAccepted = termsAccepted; }
}
