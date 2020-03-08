package com.nightcoder.ilahianz.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Time {
    public static String covertTimeToText(double dataDate) {

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        String time = dateFormat1.format(dataDate);

        String timeAgo = null;

        String suffix = "ago";

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
            Date pasTime = dateFormat.parse(time);

            Date nowTime = new Date();
            assert pasTime != null;
            long dateDiff = nowTime.getTime() - pasTime.getTime();

            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (second < 60) {
                timeAgo = second + " seconds " + suffix;
            } else if (minute < 60) {
                timeAgo = minute + " minutes " + suffix;
            } else if (hour < 24) {
                timeAgo = hour + " hours " + suffix;
            } else if (day >= 7) {
                if (day > 360) {
                    timeAgo = (day / 30) + " years " + suffix;
                } else if (day > 30) {
                    timeAgo = (day / 360) + " months " + suffix;
                } else {
                    timeAgo = (day / 7) + " week " + suffix;
                }
            } else {
                timeAgo = day + " days " + suffix;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeAgo;
    }

    public static String getTextShortTime(double dataDate) {

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        String time = dateFormat1.format(dataDate);

        String timeAgo = null;


        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
            Date pasTime = dateFormat.parse(time);

            Date nowTime = new Date();
            assert pasTime != null;
            long dateDiff = nowTime.getTime() - pasTime.getTime();

            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (second < 60) {
                timeAgo = second + "s ";
            } else if (minute < 60) {
                timeAgo = minute + "m ";
            } else if (hour < 24) {
                timeAgo = hour + "hr ";
            } else if (day >= 7) {
                if (day > 360) {
                    timeAgo = (day / 30) + "y ";
                } else if (day > 30) {
                    timeAgo = (day / 360) + "M ";
                } else {
                    timeAgo = (day / 7) + "W ";
                }
            } else {
                timeAgo = day + "d ";
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeAgo;
    }
}
