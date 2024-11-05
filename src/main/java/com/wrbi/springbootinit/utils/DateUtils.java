package com.wrbi.springbootinit.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import java.time.LocalDate;

public class DateUtils {
    private final static int[] monthOfThirtyOne = {1, 3, 5, 7, 8, 10, 12};
    private final static int[] monthOfThirty = {4, 6, 9, 11};

    /**
     * 判断是否为闰年
     * @param year
     * @return
     */
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }

    /**
     * 是否是31天
     * @return
     */
    public static boolean isMonthOfThirtyOne(int month) {
        for (int i : monthOfThirtyOne) {
            if (i == month){
                return true;
            }
        }
        return false;
    }

    /**
     * 是否是30天
     * @return
     */
    public static boolean isMonthOfThirty(int month) {
        for (int i : monthOfThirty) {
            if (i == month){
                return true;
            }
        }
        return false;
    }

    public static int getYesterday(int month, int day) {
        DateTime yesterday = DateUtil.yesterday();
        return yesterday.dayOfMonth();
    }
}
