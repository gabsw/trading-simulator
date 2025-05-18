package com.gabsw.tradingsimulator.mapper;

import com.gabsw.tradingsimulator.dto.CompanyDTO;
import com.gabsw.tradingsimulator.domain.Company;

import java.util.List;

public interface CompanyMapper {
    List<CompanyDTO> toDtoList(List<Company> modelList);
}
