package com.lsecotaro.tenpo.svcchallenge.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SumRequest {
    private List<BigDecimal> valuesToOperate;
}
