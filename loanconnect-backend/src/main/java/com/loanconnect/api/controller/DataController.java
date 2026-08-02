package com.loanconnect.api.controller;

import com.loanconnect.api.entity.LoanApplication;
import com.loanconnect.api.repository.LoanApplicationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/data")
@CrossOrigin(origins = "*")
public class DataController {

    private final LoanApplicationRepository repository;

    public DataController(LoanApplicationRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<LoanApplication> getAllApplications() {
        return repository.findAll();
    }
}