package com.bdqn.esdemo.utils;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * ClassName: {@link DateTimeUtil}
 * create by:  xyf
 * description: 时间转换工具类
 * create time: 2020/1/8 12:32
 */
public class DateTimeUtil {


    public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";


    /**
     * description:  //str->Date
     * create time: 2020/1/8 12:32
     * [dateTimeStr, formatStr]
     *
     * @return java.util.Date
     */
    public static Date strToDate(String dateTimeStr, String formatStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatStr);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    /**
     * description:   //Date->str
     * create time: 2020/1/8 12:32
     * [date, formatStr]
     *
     * @return java.lang.String
     */
    public static String dateToStr(Date date, String formatStr) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(formatStr);
    }

    /**
     * description:  //joda-time    //str->Date
     * create time: 2020/1/8 12:33
     * [dateTimeStr]
     *
     * @return java.util.Date
     */
    public static Date strToDate(String dateTimeStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    /**
     * description:  //joda-time     //Date->str
     * create time: 2020/1/8 12:34
     * [date]
     *
     * @return java.lang.String
     */
    public static String dateToStr(Date date) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD_FORMAT);
    }


    public static void main(String[] args) {
//        System.out.println(DateTimeUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
//        System.out.println(DateTimeUtil.strToDate("2010-01-01 11:11:11", "yyyy-MM-dd HH:mm:ss"));
        System.out.println(DateTimeUtil.strToDate("Tue Apr 03 14:07:31 CST 1990"));
    }


}
