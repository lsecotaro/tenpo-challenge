package com.lsecotaro.tenpo.svcchallenge.util;

import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class DateHelper {
    public Date addMinutesToDate(Date date, int minutes) {
        Calendar newDate = Calendar.getInstance();
        newDate.setTime(date);
        newDate.add(Calendar.MINUTE, minutes);
        return newDate.getTime();
    }

    public long getDifferenceInSeconds(Date first, Date second) {
        return (first.getTime() - second.getTime())/1000;
    }
}
