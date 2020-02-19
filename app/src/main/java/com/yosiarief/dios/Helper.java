package com.yosiarief.dios;


import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Locale;

public class Helper {

    public static float kelvinToCelcius(float curr){
        float celcius = curr - 273.15F;
        return celcius;
    }

    static String timestampToDate(long timeStamp){
//        long timestamp = curr.getDt(); //Example -> in ms
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timeStamp * 1000L);
        String date = DateFormat.format("E, dd MMM yy", cal).toString();
        return date;
    }
}
