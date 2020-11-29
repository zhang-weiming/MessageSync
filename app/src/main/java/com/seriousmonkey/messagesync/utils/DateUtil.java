package com.seriousmonkey.messagesync.utils;

import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private static final SimpleDateFormat longDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    private static final SimpleDateFormat dayDateFormat = new SimpleDateFormat("yyyy/MM/dd");
    private static final SimpleDateFormat timeDateFormat = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat yearDateFormat = new SimpleDateFormat("yyyy");

    public static long parseStrToLong(String timeStr) {
        try {
            return longDateFormat.parse(timeStr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String parseLongToLongStr(Long timeLong) {
        try {
            return longDateFormat.format(new Date(timeLong));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String parseLongToDayStr(Long timeLong) {
        try {
            return dayDateFormat.format(new Date(timeLong));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String parseLongToTimeStr(Long timeLong) {
        try {
            return timeDateFormat.format(new Date(timeLong));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isToday(long longDate) {
        return dayDateFormat.format(longDate).equals(
                dayDateFormat.format(new Date())
        );
    }

    public static boolean isCurrYear(long longDate) {
        return yearDateFormat.format(longDate).equals(
                yearDateFormat.format(new Date())
        );
    }

}
