package com.riotfallen.moviedirectory.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public String dateToString(String date, String oldFormat, String newFormat){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(oldFormat, Locale.US);
            Date newDate = dateFormat.parse(date);
            dateFormat = new SimpleDateFormat(newFormat, Locale.US);
            return dateFormat.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return " ";
        }
    }

}
