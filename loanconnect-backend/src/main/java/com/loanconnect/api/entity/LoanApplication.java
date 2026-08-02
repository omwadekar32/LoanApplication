package com.loanconnect.api.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Database row for a single loan application submitted through the
 * "Apply for a Loan" form on the LoanConnect website.
 */
@Entity
@Table(name = "loan_applications")
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Human-friendly reference shown to the applicant, e.g. APP-20260801-0001 */
    @Column(name = "application_ref", unique = true, length = 40)
    private String applicationRef;

    // ---------- Personal details ----------
    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, length = 10)
    private String mobile;

    @Column(nullable = false, length = 120)
    private String email;

    @Column(nullable = false)
    private LocalDate dob;

    @Column(nullable = false, length = 80)
    private String city;

    @Column(nullable = false, length = 40)
    private String loanType;

    // ---------- Employment details ----------
    @Column(nullable = false, length = 30)
    private String employmentType;

    @Column(nullable = false, length = 120)
    private String companyName;

    @Column(nullable = false)
    private BigDecimal monthlyIncome;

    // ---------- Loan details ----------
    @Column(nullable = false)
    private BigDecimal loanAmount;

    @Column(nullable = false)
    private Integer loanTenure;

    @Column(nullable = false, length = 1000)
    private String loanPurpose;

    // ---------- Document references ----------
    // Only file names are stored here; see README for wiring up real
    // multipart file storage (disk / S3) if you need the actual bytes.
    @Column(length = 255)
    private String aadhaarFileName;

    @Column(length = 255)
    private String panFileName;

    @Column(length = 255)
    private String salarySlipFileName;

    // ---------- Metadata ----------
    @Column(nullable = false)
    private Boolean termsAccepted;

    @Column(nullable = false, length = 20)
    private String status = "SUBMITTED";

    @Column(nullable = false)
    private LocalDateTime submittedAt;

    @PrePersist
    void onCreate() {
        if (submittedAt == null) {
            submittedAt = LocalDateTime.now();
        }
    }

    // ---------- Getters & Setters ----------

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getApplicationRef() { return applicationRef; }
    public void setApplicationRef(String applicationRef) { this.applicationRef = applicationRef; }

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

    public Boolean getTermsAccepted() { return termsAccepted; }
    public void setTermsAccepted(Boolean termsAccepted) { this.termsAccepted = termsAccepted; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
}
