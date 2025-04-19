package com.example.volunteer.controller;

import com.example.volunteer.converter.CompanyConverter;
import com.example.volunteer.DTO.CompanyDTO;
import com.example.volunteer.model.Company;
import com.example.volunteer.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    @Autowired
    private CompanyRepository companyRepository;

    @PostMapping
    public ResponseEntity<CompanyDTO> createCompany(@RequestBody CompanyDTO companyDto) {
        Company company = CompanyConverter.dtoToEntity(companyDto);
        Company saved = companyRepository.save(company);
        return ResponseEntity.ok(CompanyConverter.entityToDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyDTO> updateCompany(@PathVariable Long id, @RequestBody CompanyDTO companyDto) {
        return companyRepository.findById(id)
                .map(existing -> {
                    existing.setDescription(companyDto.getDescription());
                    existing.setEmail(companyDto.getEmail());
                    existing.setAverageRating(companyDto.getAverageRating());
                    Company updated = companyRepository.save(existing);
                    return ResponseEntity.ok(CompanyConverter.entityToDto(updated));
                }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> getCompany(@PathVariable Long id) {
        return companyRepository.findById(id)
                .map(company -> ResponseEntity.ok(CompanyConverter.entityToDto(company)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<CompanyDTO>> getAllCompanies() {
        List<CompanyDTO> companies = companyRepository.findAll()
                .stream().map(CompanyConverter::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(companies);
    }
}