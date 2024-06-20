package com.w.lee.common.util;

import cn.hutool.core.date.DateException;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.w.lee.common.exception.BizException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CommonDateUtil {
    private static  final List<String> FORMATTERS = Arrays.asList(
            DatePattern.NORM_DATETIME_PATTERN,
            DatePattern.UTC_SIMPLE_PATTERN,
            DatePattern.NORM_DATE_PATTERN,
            DatePattern.UTC_PATTERN,
            DatePattern.NORM_MONTH_PATTERN,
            DatePattern.SIMPLE_MONTH_PATTERN
    );

    public static String formatDateStr(String dateStr) {
        if (StrUtil.isBlank(dateStr)){
            return null;
        }
        DateTime parse = cn.hutool.core.date.DateUtil.parse(dateStr);
        return cn.hutool.core.date.DateUtil.format(parse, DatePattern.NORM_DATETIME_PATTERN);
    }
    public static LocalDateTime parseLocalDateTime(String dateStr) {
        if (StrUtil.isBlank(dateStr)){
            return null;
        }
        DateTime parse = cn.hutool.core.date.DateUtil.parse(dateStr);
        return parse.toLocalDateTime();
    }
    public static LocalDate parseLocalDate(String dateStr) {
        if (StrUtil.isBlank(dateStr)){
            return null;
        }
        DateTime parse = cn.hutool.core.date.DateUtil.parse(dateStr);
        return parse.toLocalDateTime().toLocalDate();
    }

    public static LocalDateTime parse(String input) {

        for (String formatter : FORMATTERS) {
            Optional<LocalDateTime> dateTime = parseWithFormatter(input, formatter);
            if (dateTime.isPresent()) {
                return dateTime.get();
            }
        }

        throw new BizException("解析时间失败:" + input);
    }
    private static Optional<LocalDateTime> parseWithFormatter(String input, String formatter) {
        try {
            return Optional.of(cn.hutool.core.date.DateUtil.toLocalDateTime(cn.hutool.core.date.DateUtil.parseByPatterns(input, formatter)));
        } catch (DateException e) {
            return Optional.empty();
        }
    }


    /**
     * 获取昨天此时的时间
     * @param source
     * @return
     */
    public static LocalDateTime getYesterdaySameTime(LocalDateTime source) {
        return source.minusDays(1);
    }

    /**
     * 获取上周此时的时间
     * @param source
     * @return
     */
    public static LocalDateTime getLastWeekSameTime(LocalDateTime source) {
        return source.minusWeeks(1);
    }

    /**
     * 获取上个月此时的时间
     * @param source
     * @return
     */
    public static LocalDateTime getLastMonthSameTime(LocalDateTime source) {
        return source.minusMonths(1);
    }

    /**
     * 获取去年此时的时间
     * @param source
     * @return
     */
    public static LocalDateTime getLastYearSameTime(LocalDateTime source) {
        return source.minusYears(1);
    }
}
