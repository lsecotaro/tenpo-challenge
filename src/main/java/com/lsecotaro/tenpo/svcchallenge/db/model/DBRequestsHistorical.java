package com.lsecotaro.tenpo.svcchallenge.db.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Builder
@Entity(name = "endpoints_request_historical")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DBRequestsHistorical {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "endpoint")
    private String endpoint;
    @Column(name = "params")
    private String params;
    @Column(name = "response")
    private String response;
    @Column(name = "date_time")
    private Date date;

}
