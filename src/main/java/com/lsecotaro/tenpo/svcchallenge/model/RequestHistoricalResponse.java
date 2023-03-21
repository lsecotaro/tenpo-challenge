package com.lsecotaro.tenpo.svcchallenge.model;

import com.lsecotaro.tenpo.svcchallenge.db.model.DBRequestsHistorical;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RequestHistoricalResponse {
    private List<DBRequestsHistorical> historical;
    @Getter(AccessLevel.NONE) private boolean hasMore;
    public boolean hasMore() {
        return hasMore;
    }
}
