package com.gabsw.tradingsimulator.service;

import com.gabsw.tradingsimulator.model.Company;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CompanyService {
    private final List<Company> companies = Arrays.asList(
            new Company("AAPL", "Apple Inc.", 1000),
            new Company("GOOG", "Alphabet Inc.", 500),
            new Company("TSLA", "Tesla Inc.", 700)
    );

    public List<Company> getAllCompanies() {
        return companies;
    }
}
