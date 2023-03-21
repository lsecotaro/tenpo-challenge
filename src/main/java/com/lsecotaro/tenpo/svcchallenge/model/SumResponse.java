package com.lsecotaro.tenpo.svcchallenge.model;

import com.lsecotaro.tenpo.svcchallenge.db.model.DBRequestsHistorical;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SumResponse {
    private BigDecimal result;
}
