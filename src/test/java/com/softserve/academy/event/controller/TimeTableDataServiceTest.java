package com.softserve.academy.event.controller;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.DayOfWeek;

import static org.junit.Assert.assertTrue;

@RunWith(DataProviderRunner.class)
public class TimeTableDataServiceTest {


    @DataProvider
    public static Object[][] daysDataProvider() {
        return new Object[][]{
                {DayOfWeek.MONDAY, DayOfWeek.THURSDAY},
                {DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY},
                {DayOfWeek.SATURDAY, DayOfWeek.THURSDAY},
        };
    }


    @Test
    @UseDataProvider("daysDataProvider")
    public void testGetDays(DayOfWeek day,DayOfWeek day2) {
        System.out.println(day);
        assertTrue(true);
    }

}
