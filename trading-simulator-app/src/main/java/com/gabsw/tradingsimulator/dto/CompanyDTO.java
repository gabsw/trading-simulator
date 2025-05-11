package com.gabsw.tradingsimulator.dto;

import lombok.Data;

@Data
public class CompanyDTO {
    private String ticker;
    private String name;
    private int availableShares;
}
