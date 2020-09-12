package iterate;

import model.DailyData;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * description：报表-补全每日数据
 * 使用无限流补全日期
 *
 * @author jiac
 * @date 2020/8/5 11:16
 */
public class CompleteDailyData {

    public static void main(String[] args){
        List<DailyData> dataList = new ArrayList<>();
        LocalDate startDay = LocalDate.now().minusWeeks(1);
        LocalDate endDay = LocalDate.now();
        dataList.add(new DailyData(startDay, BigDecimal.ONE));
        dataList.add(new DailyData(endDay, BigDecimal.TEN));
        //补全过去一周的每日数据
        List<DailyData> newList = complete(startDay, endDay, dataList);
        newList.forEach(System.out::println);
    }

    /**
     * 补全每日数据
     * @param startDay 开始日期
     * @param endDay 截止日期
     * @param oldList
     * @return
     */
    private static List<DailyData> complete(LocalDate startDay, LocalDate endDay, List<DailyData> oldList) {
        List<DailyData> newList = new ArrayList<>();
        if (CollectionUtils.isEmpty(oldList)) {
            return newList;
        }
        List<LocalDate> dates = getRangeDays(startDay, endDay);
        Map<LocalDate, DailyData> oldMap = oldList.stream().collect(Collectors.toMap(DailyData::getDate, Function.identity()));
        dates.forEach(c -> {
            if (oldMap.containsKey(c)) {
                newList.add(oldMap.get(c));
            } else {
                //没有这一天的数据，默认补0
                newList.add(new DailyData(c, BigDecimal.ZERO));
            }
        });
        return newList;
    }

    /**
     * 获取间隔的日期
     * @param startDay 开始日期
     * @param endDay 截止日期
     * @return
     */
    private static List<LocalDate> getRangeDays(LocalDate startDay, LocalDate endDay) {
        List<LocalDate> dates = new ArrayList<>();
        //获取间隔的天数
        long between = ChronoUnit.DAYS.between(startDay, endDay);
        if (between < 1) {
            //开始日期<=截止日期
            return dates;
        }
        //从开始日期创建一个每次加一天的无限流，限制到截止日期为止
        Stream.iterate(startDay, c -> c.plusDays(1))
                .limit(between + 1)
                .forEach(dates::add);
        return dates;
    }
}
