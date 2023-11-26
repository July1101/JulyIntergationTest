package com.thg.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by tanhuigen
 * Date 2022-09-24
 * Description
 */
@Slf4j
public class DateDeserializerUtils {


    private static final String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    private static final Pattern PARAM_PATTERN = Pattern.compile("(?<=\\()(.+?)(?=\\))");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");
    private static final Pattern LETTER_PATTERN = Pattern.compile("[a-zA-Z]");

    private static boolean isValidFormat(String originString){
        return originString.matches("(date|#)\\(.*\\)");
    }

    private static String defaultDateAtNow(){
        return new SimpleDateFormat(DEFAULT_DATE_FORMAT).format(new Date());
    }

    private static int getCalendarTypeBySingle(String type){
        switch (type){
            case "M":
                return Calendar.MONTH;
            case "y":
            case "Y":
                return Calendar.YEAR;
            case "H":
            case "h":
                return Calendar.HOUR;
            case "m":
                return Calendar.MINUTE;
            case "s":
            case "S":
                return Calendar.SECOND;
            default:
                return Calendar.DAY_OF_MONTH;
        }
    }

    private static int getCalendarType(String str){
        int calendarType = Calendar.DAY_OF_MONTH;
        Matcher matcher = LETTER_PATTERN.matcher(str);
        if(matcher.find()){
            calendarType = getCalendarTypeBySingle(matcher.group());
        }
        return calendarType;
    }

    private static int getNumber(String str){
        int number = 0;
        Matcher matcher = NUMBER_PATTERN.matcher(str);
        if(matcher.find()){
            number = Integer.parseInt(matcher.group());
        }
        if(str.startsWith("-")) return -number;
        return number;
    }

    private static String formatToStringByCalendar(Calendar calendar,String format){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        simpleDateFormat.setTimeZone(calendar.getTimeZone());
        return simpleDateFormat.format(calendar.getTime());
    }

    private static String getFormatStringFromDate(Calendar date,String format){
        switch (format){
            case "date":
            case "DATE":
                return formatToStringByCalendar(date,DEFAULT_DATE_FORMAT);
            case "time":
            case "TIME":
                return formatToStringByCalendar(date,DEFAULT_TIME_FORMAT);
            case "year":
            case "YEAR":
                return String.valueOf(date.get(Calendar.YEAR));
            case "month":
            case "MONTH":
                return String.valueOf(date.get(Calendar.MONTH)+1);
            case "day":
            case "DAY":
                return String.valueOf(date.get(Calendar.DAY_OF_MONTH));
            case "hour":
            case "HOUR":
                return String.valueOf(date.get(Calendar.HOUR_OF_DAY));
            case "minute":
            case "MINUTE":
                return String.valueOf(date.get(Calendar.MINUTE));
            case "second":
            case "SECOND":
                return String.valueOf(date.get(Calendar.SECOND));
            default:
                return formatToStringByCalendar(date,DEFAULT_DATE_FORMAT);
        }
    }

    private static String parseDateFormat(String[] args){
        if(args.length==0) return defaultDateAtNow();
        Calendar cal = Calendar.getInstance();
        for(int i=1;i<args.length;i++){
            if(args[i].startsWith("+")||args[i].startsWith("-")){
                int operatorNumber = getNumber(args[i]);
                int calendarType = getCalendarType(args[i]);
                cal.add(calendarType,operatorNumber);
            }else{
                cal.setTimeZone(TimeZone.getTimeZone(args[i]));
            }
        }
        return getFormatStringFromDate(cal,args[0]);
    }
    public static String parseDate(String originString){
        if(isValidFormat(originString)) {
            Matcher matcher = PARAM_PATTERN.matcher(originString);
            if (matcher.find()) {
                String matcherString = matcher.group();
                String[] args = matcherString.split(",", 0);
                return parseDateFormat(args);
            }
        }
        return defaultDateAtNow();
    }
}
