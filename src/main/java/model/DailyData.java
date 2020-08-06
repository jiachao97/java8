package model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * description：每日数据
 *
 * @author jiac
 * @date 2020/8/5 11:15
 */
@Getter
@Builder
@ToString
public class DailyData {

    /**
     * 每日数据量
     */
    private BigDecimal data;

    /**
     * 日期
     */
    private LocalDate date;
}

