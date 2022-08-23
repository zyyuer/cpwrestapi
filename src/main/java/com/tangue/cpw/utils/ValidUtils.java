package com.tangue.cpw.utils;

import com.tangue.cpw.exception.CustomException;
import com.tangue.cpw.exception.CustomExceptionType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ValidUtils {
    public static final String FORMAT_SECONDS = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_DAY = "yyyy-MM-dd";

    /**
     * 判断字符串是否为合法的日期格式yyyy-MM-dd
     *
     * @param dateStr 待判断的字符串
     * @return
     */
    public static boolean isValidDate(String dateStr) {
        //判断结果 默认为true
        boolean judgeresult = true;
        //1、首先使用SimpleDateFormat初步进行判断，过滤掉注入 yyyy-01-32 或yyyy-00-0x等格式
        //此处可根据实际需求进行调整，如需判断yyyy/MM/dd格式将参数改掉即可
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_DAY);
        try {
            //增加强判断条件，否则 诸如2022-02-29也可判断出去
            format.setLenient(false);
            Date date = format.parse(dateStr);
        } catch (Exception e) {
            judgeresult = false;
        }
        //由于上述方法只能验证正常的日期格式，像诸如 0001-01-01、11-01-01，10001-01-01等无法校验，此处再添加校验年费是否合法
        String yearStr = dateStr.split("-")[0];
        if (yearStr.startsWith("0") || yearStr.length() != 4) {
            judgeresult = false;
        }
        return judgeresult;
    }

    /**
     * 校验时间格式是否为 yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static Boolean isValidDatetime(String date) {
        //用于指定 日期/时间 模式
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(FORMAT_SECONDS);
        boolean flag = true;
        try {
            //Java 8 新添API 用于解析日期和时间
            LocalDateTime.parse(date, dtf);
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 日期风格字符串转换为日期类型
     * @param strDate 字符串
     * @param format 样式
     * @return
     */
    public static Date strToDate(String strDate, String format) {
        if (isValidDate(strDate)) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            try {
                Date date = sdf.parse(strDate);
                return date;
            } catch (ParseException e) {
                throw new CustomException(CustomExceptionType.SYSTEM_ERROR,
                        "日期格式转换失败：" + e.getMessage());
            }
        } else {
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR,
                    "服务器日期格式验证失败");
        }
    }

    /**
     * 日期按指定风格转换为字符串
     * @param date 日期
     * @param format 样式
     * @return
     */
    public static String DateToStr(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String strDate = sdf.format(date);
        return strDate;

    }
}