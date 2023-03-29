/***********************************************************************
 * Copyright 2023 Turker Ozturk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***********************************************************************/


package com.turkerozturk.library.fat32.helpers;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author u
 */
public class MsDosDateTime {

    private int dateWordHiByte;
    private int dateWordLoByte;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;
    private int timeWordHiByte;
    private int timeWordLoByte;
    private int milliSecondByteAsTenth;

    public Date Parse() {
        Date date = null;
        if (calculateMsDoSDateAsValues() == true) {

            if (calculateMsDoSTimeAsValues() == true) {
                //DT = new Date (new Date(Year, Month, Day, Hour, Minute, Second)); 
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month - 1, day, hour, minute, second);
                date = calendar.getTime();

            }

            return date;

        }

        return date;

    }

    private boolean calculateMsDoSDateAsValues() {
        if (dateWordHiByte == -1 | dateWordLoByte == -1) {
            return false;
        }
        int rawDate = dateWordHiByte * 256 + dateWordLoByte;
        int rawYear = ((rawDate & 0xFE00) >> 9);
        if (rawYear < 0 | rawYear > 127) {
            return false;
        }
        year = rawYear + 1980;
        month = (rawDate & 0x1E0) >> 5;
        if (month < 1 | month > 12) {
            return false;
        }
        day = rawDate & 0x1F;
        int dayLimit = 28;
        switch (month) {
            case 2:

                // Date leapYear = new Date(Year, 2, 1);
                if (isLeapYear(year)) {
                    dayLimit = 29;
                } else {
                    dayLimit = 28;
                }

                break;
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                dayLimit = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                dayLimit = 30;
                break;

        }
        if (day < 1 | day > dayLimit) {
            return false;
        }

        return true;
    }

    private boolean calculateMsDoSTimeAsValues() {

        int rawTime = timeWordHiByte * 256 + timeWordLoByte;
        hour = (rawTime & 0xF800) >> 11;
        minute = (rawTime & 0x7E0) >> 5;
        int rawSecond = rawTime & 0x1F;
        second = rawSecond * 2;

        if (hour < 0 | hour > 23) {
            return false;
        }
        if (minute < 0 | minute > 59) {
            return false;
        }
        if (rawSecond < 0 | rawSecond > 29) {
            return false;
        }

        if (milliSecondByteAsTenth < 0 | milliSecondByteAsTenth > 199) {
            return false;
        } else {
            second += (milliSecondByteAsTenth / 100);
        }

        return true;
    }

    //https://stackoverflow.com/questions/1021324/java-code-for-calculating-leap-year/1021373#1021373
    public static boolean isLeapYear(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        return calendar.getActualMaximum(Calendar.DAY_OF_YEAR) > 365;
    }

    public int getDateWordHiByte() {
        return dateWordHiByte;
    }

    public void setDateWordHiByte(int dateWordHiByte) {
        this.dateWordHiByte = dateWordHiByte;
    }

    public int getDateWordLoByte() {
        return dateWordLoByte;
    }

    public void setDateWordLoByte(int dateWordLoByte) {
        this.dateWordLoByte = dateWordLoByte;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getTimeWordHiByte() {
        return timeWordHiByte;
    }

    public void setTimeWordHiByte(int timeWordHiByte) {
        this.timeWordHiByte = timeWordHiByte;
    }

    public int getTimeWordLoByte() {
        return timeWordLoByte;
    }

    public void setTimeWordLoByte(int timeWordLoByte) {
        this.timeWordLoByte = timeWordLoByte;
    }

    public int getMilliSecondByteAsTenth() {
        return milliSecondByteAsTenth;
    }

    public void setMilliSecondByteAsTenth(int milliSecondByteAsTenth) {
        this.milliSecondByteAsTenth = milliSecondByteAsTenth;
    }

    
    
}
