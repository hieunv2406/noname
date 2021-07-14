package com.example.common.utils;

import com.google.common.base.Splitter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

public class DataUtil {

    public static List<String> splitDot(String input) {
        return Splitter.on(".").trimResults().omitEmptyStrings().splitToList(input);
    }

    public static List<String> splitCharE(String input) {
        return Splitter.on("E").trimResults().omitEmptyStrings().splitToList(input);
    }

    public static boolean isNullOrEmpty(Collection<?> collection) {
        if (collection == null || collection.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isNullOrEmpty(Map<?, ?> map) {
        if (map == null || map.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isNullOrEmpty(Object object) {
        if (object == null) {
            return true;
        }
        return false;
    }

    public static boolean isNullOrEmpty(Object[] array) {
        if (array == null || array.length == 0) {
            return true;
        }
        return false;
    }

    public static boolean isNullOrEmpty(String string) {
        if (string == null || string.trim().length() == 0) {
            return true;
        }
        return false;
    }

    public static String convertSqlLike(Object obj) {
        return "%" + obj + "%";
    }

    /**
    * chuyển Date sang String
    * @param date Date ngày cần chuyển
     * @return String yyyy-MM-dd hh:mm:ss
     **/
    public static String convertDateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return dateFormat.format(date);
    }

    /**
     * chuyển Date sang String
     * @param date Date ngày cần chuyển
     * @return String yyyy-MM-dd
     **/
    public static String dateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    /**
     * chuyển String sang Date
     * @param input String ngày cần chuyển
     * @return Date yyyy-MM-dd hh:mm:ss
     **/
    public static Date convertStringToDate(String input) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return dateFormat.parse(input);
    }

    /**
     * chuyển String sang Date dd/MM/yyyy
     * @param input String ngày cần chuyển
     * @return Date dd/MM/yyyy hh:mm:ss
     **/
    public static Date convertStringToDateddMMyyy(String input) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.parse(input);
    }

    /**
     * chuyển String sang Date
     * @param input String ngày cần chuyển
     * @return Date yyyy-MM-dd
     **/
    public static Date stringToDate(String input) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.parse(input);
    }

    /**
     * Kiểm tra định dạng Date
     * @param input String ngày cần kiểm tra
     * @return boolean valid
     **/
    public static boolean isValidDate(final String input) {
        boolean valid = false;
        try {
            // ResolverStyle.STRICT for 30, 31 days checking, and also leap year.
            LocalDate.parse(input,
                    DateTimeFormatter.ofPattern("uuuu-M-d")
                            .withResolverStyle(ResolverStyle.STRICT)
            );
            valid = true;
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            valid = false;
        }
        return valid;
    }

    public static String removeSeparator(String url) {
        int index = url.lastIndexOf("\\");
        return url.substring(index + 1, url.length());
    }

    public static String replaceSeparator(String url) {
        return url.replaceAll(Matcher.quoteReplacement("\\"), "/");
    }

    /**
     * Lấy ngày hiện tại dưới dạng dd_MM_yyyy_HH_mm_ss
     * @return String strCurTimeExp
     **/
    public static String getCurrentTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("dd/MM/yyy HH:mm:ss");
        String strCurTimeExp = dateFormat.format(new Date());
        strCurTimeExp = strCurTimeExp.replaceAll("/", "_");
        strCurTimeExp = strCurTimeExp.replaceAll(" ", "_");
        strCurTimeExp = strCurTimeExp.replaceAll(":", "_");
        return strCurTimeExp;
    }
}
