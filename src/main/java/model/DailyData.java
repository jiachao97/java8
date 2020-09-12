package model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * description：每日数据
 *
 * @author jiac
 * @date 2020/8/5 11:15
 */
@Data
@AllArgsConstructor
public class DailyData {

    /**
     * 日期
     */
    private LocalDate date;

    /**
     * 每日数据量
     */
    private BigDecimal data;
}

