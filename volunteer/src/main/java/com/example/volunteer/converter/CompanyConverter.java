package com.example.volunteer.converter;

import com.example.volunteer.DTO.CompanyDTO;
import com.example.volunteer.model.Company;

public class CompanyConverter {

    public static Company dtoToEntity(CompanyDTO dto) {
        if(dto == null) return null;
        Company company = new Company();
        company.setId(dto.getId());
        company.setDescription(dto.getDescription());
        company.setEmail(dto.getEmail());
        company.setAverageRating(dto.getAverageRating());
        return company;
    }

    public static CompanyDTO entityToDto(Company company) {
        if(company == null) return null;
        CompanyDTO dto = new CompanyDTO();
        dto.setId(company.getId());
        dto.setDescription(company.getDescription());
        dto.setEmail(company.getEmail());
        dto.setAverageRating(company.getAverageRating());
        return dto;
    }
}