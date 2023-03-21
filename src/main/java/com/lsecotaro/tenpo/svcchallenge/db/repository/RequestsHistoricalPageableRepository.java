package com.lsecotaro.tenpo.svcchallenge.db.repository;

import com.lsecotaro.tenpo.svcchallenge.db.model.DBRequestsHistorical;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface RequestsHistoricalPageableRepository extends PagingAndSortingRepository<DBRequestsHistorical, Integer> {
}
