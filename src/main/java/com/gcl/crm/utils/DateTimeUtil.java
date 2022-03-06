package com.gcl.crm.utils;

import com.gcl.crm.config.AppConst;
import org.apache.poi.ss.usermodel.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {
    public static final String PATTERN_YY_MM_DD = "yyyy/MM/dd HH:mm:ss";

    public static Date convertStringToDate(String input, String pattern) throws ParseException {
        if (ValidateUtil.isNotNullOrEmpty(input)) {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.parse(input);
        }
        return null;
    }

    public static String convertDateToString(Date input, String pattern) {
        if (ValidateUtil.isNotNullOrEmpty(input)) {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(input);
        }
        return null;
    }

    public static String convertCellValueToDate(Object input, String pattern) throws ParseException {
        Date date;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        if (input instanceof Double) {
            date = DateUtil.getJavaDate((Double) input);
        } else {
            date = sdf.parse((String) input);
        }
        return sdf.format(date);
    }

    public static Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }
}
