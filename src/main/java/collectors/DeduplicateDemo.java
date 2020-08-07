package collectors;

import model.DailyData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * description：Stream数据去重demo
 *
 * @author jiac
 * @date 2020/8/6 10:35
 */
public class DeduplicateDemo {

    public static void main(String[] args) {
        List<DailyData> dataList = new ArrayList<>();
        dataList.add(DailyData.builder().data(BigDecimal.ZERO).date(LocalDate.now().minusWeeks(1)).build());
        dataList.add(DailyData.builder().data(BigDecimal.TEN).date(LocalDate.now().minusWeeks(1)).build());
        dataList.add(DailyData.builder().data(BigDecimal.ZERO).date(LocalDate.now()).build());
        dataList.add(DailyData.builder().data(BigDecimal.TEN).date(LocalDate.now()).build());
        dataList.add(DailyData.builder().data(BigDecimal.ONE).date(LocalDate.now()).build());

        System.out.println("未去重：" + dataList);

        //filter过滤
        List<DailyData> dupByFilter = dataList.stream()
                .filter(c -> !BigDecimal.ZERO.equals(c.getData()))
                .collect(Collectors.toList());
        System.out.println("filter过滤数据量为0的：" + dupByFilter);

        //TreeSet去重
        Set<DailyData> set = new TreeSet<>(Comparator.comparing(DailyData::getDate));
        set.addAll(dataList);
        System.out.println("TreeSet根据日期去重：" + set);

        /**
         * Collectors.toCollection()：可以指定集合的类型
         *
         */
        Set<DailyData> dupByCollection = dataList.stream()
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(DailyData::getDate))));
        System.out.println("collection根据日期去重：" + dupByCollection);

        ArrayList<DailyData> list = dataList.stream()
                .collect(
                        Collectors.collectingAndThen(
                                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(DailyData::getDate))), ArrayList::new
                        )
                );
        System.out.println("collection根据日期去重再存入ArrayList：" + list);

        //多条件
        Set<DailyData> collect = dataList.stream()
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(DailyData::getData)
                        .thenComparing(DailyData::getDate))));
        System.out.println("多条件去重：" + collect);
    }
}
