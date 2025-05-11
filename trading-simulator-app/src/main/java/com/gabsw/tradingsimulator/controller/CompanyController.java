package com.gabsw.tradingsimulator.controller;

import com.gabsw.tradingsimulator.dto.CompanyDTO;
import com.gabsw.tradingsimulator.mapper.CompanyMapper;
import com.gabsw.tradingsimulator.service.CompanyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;
    private final CompanyMapper companyMapper;

    public CompanyController(CompanyService companyService, CompanyMapper companyMapper) {
        this.companyService = companyService;
        this.companyMapper = companyMapper;
    }

    @GetMapping
    public List<CompanyDTO> getAllCompanies() {
        var companies = companyService.getAllCompanies();
        return companyMapper.toDtoList(companies);
    }
}
