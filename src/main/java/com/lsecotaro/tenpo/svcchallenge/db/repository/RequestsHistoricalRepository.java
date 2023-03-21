package com.lsecotaro.tenpo.svcchallenge.db.repository;

import com.lsecotaro.tenpo.svcchallenge.db.model.DBRequestsHistorical;
import org.springframework.data.repository.CrudRepository;

public interface RequestsHistoricalRepository extends CrudRepository<DBRequestsHistorical, Integer> {
}
