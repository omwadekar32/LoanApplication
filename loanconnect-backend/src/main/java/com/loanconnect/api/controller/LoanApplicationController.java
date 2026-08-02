package com.loanconnect.api.controller;

import com.loanconnect.api.dto.LoanApplicationRequest;
import com.loanconnect.api.dto.LoanApplicationResponse;
import com.loanconnect.api.entity.LoanApplication;
import com.loanconnect.api.service.LoanApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST endpoints backing the "Apply for a Loan" form on the LoanConnect
 * website. This is the controller referenced in script.js's fetch() call.
 *
 * Base path: /api/loan-applications
 */
@RestController
@RequestMapping("/api/loan-applications")
public class LoanApplicationController {

    private final LoanApplicationService service;

    public LoanApplicationController(LoanApplicationService service) {
        this.service = service;
    }

    /**
     * Called when the person clicks "Submit Application" on the frontend.
     * Validates the payload, saves it to the database, and returns a
     * reference number the frontend can show in its success modal.
     *
     * POST /api/loan-applications
     */
    @PostMapping
    public ResponseEntity<LoanApplicationResponse> submitApplication(
            @Valid @RequestBody LoanApplicationRequest request) {

        LoanApplicationResponse response = service.submitApplication(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Admin/back-office use — list every submitted application.
     * GET /api/loan-applications
     */
    @GetMapping
    public ResponseEntity<List<LoanApplication>> getAllApplications() {
        return ResponseEntity.ok(service.getAllApplications());
    }

    /**
     * Fetch a single application by its database id.
     * GET /api/loan-applications/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<LoanApplication> getApplicationById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getApplicationById(id));
    }

    /**
     * Fetch a single application by its human-friendly reference number,
     * e.g. GET /api/loan-applications/ref/APP-20260801-0001
     */
    @GetMapping("/ref/{applicationRef}")
    public ResponseEntity<LoanApplication> getApplicationByRef(@PathVariable String applicationRef) {
        return ResponseEntity.ok(service.getApplicationByRef(applicationRef));
    }

    /**
     * Admin/back-office use — remove an application.
     * DELETE /api/loan-applications/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        service.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }
}

