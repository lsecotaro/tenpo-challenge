package com.lsecotaro.tenpo.svcchallenge.filter;

import com.lsecotaro.tenpo.svcchallenge.db.model.DBRequestsHistorical;
import com.lsecotaro.tenpo.svcchallenge.db.repository.RequestsHistoricalRepository;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Builder
@Slf4j
public class PersistHistoricalTask implements Runnable {
    private final DBRequestsHistorical requestsHistorical;
    private final RequestsHistoricalRepository historicalRepository;

    @Override
    @SneakyThrows
    public void run() {
        log.info("save request track");
        historicalRepository.save(requestsHistorical);
    }
}
