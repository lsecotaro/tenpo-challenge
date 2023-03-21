package com.lsecotaro.tenpo.svcchallenge.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PercentageResponse {
    private BigDecimal percentage;
    private Date lastUpdated;
}
