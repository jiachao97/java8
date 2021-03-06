package collectors;

import model.DailyData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * description：数据去重demo(distinct / filter / TreeSet)
 *
 * @author jiac
 * @date 2020/8/6 10:35
 */
public class DeduplicateDemo {

    public static void main(String[] args) {
        List<DailyData> dataList = new ArrayList<>();
        dataList.add(new DailyData(LocalDate.now(), BigDecimal.ZERO));
        dataList.add(new DailyData(LocalDate.now(), BigDecimal.ZERO));
        dataList.add(new DailyData(LocalDate.now(), BigDecimal.TEN));
        dataList.add(new DailyData(LocalDate.now().plusWeeks(1), BigDecimal.TEN));
        dataList.add(new DailyData(LocalDate.now().plusWeeks(1), BigDecimal.ONE));

        System.out.println("未去重：" + dataList);

        //distinct去重，依赖于hashCode()和equals()
        List<DailyData> dupByDistinct = dataList.stream()
                .distinct()
                .collect(Collectors.toList());
        System.out.println("distinct去重：" + dupByDistinct);

        //filter过滤
        List<DailyData> dupByFilter = dataList.stream()
                .filter(c -> !BigDecimal.ZERO.equals(c.getData()))
                .collect(Collectors.toList());
        System.out.println("filter过滤数据量为0的：" + dupByFilter);

        //TreeSet去重
        TreeSet<DailyData> set = new TreeSet<>(Comparator.comparing(DailyData::getDate));
        set.addAll(dataList);
        System.out.println("TreeSet根据日期去重：" + set);

        /**
         * Collectors.toCollection()：可以指定集合的类型
         * 指定为实现了Comparator的TreeSet，Comparator是一个函数式接口，可以使用lambda表达式实现
         * 根据日期去重
         */
        TreeSet<DailyData> dupByCollection = dataList.stream()
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(DailyData::getDate))));
        System.out.println("根据日期去重：" + dupByCollection);

        /**
         * 多条件去重，根据日期+数据量去重
         * 这里使用 ; 将日期和数据量拼接成字符串，作为TreeMap的key进行比较
         */
        TreeSet<DailyData> dupByDateAndData = dataList.stream()
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(c -> c.getDate() + ";" + c.getData()))));
        System.out.println("多条件去重：" + dupByDateAndData);

        /**
         * public static<T,A,R,RR> Collector<T,A,RR> collectingAndThen(Collector<T,A,R> downstream,
         *                                                                 Function<R,RR> finisher) {}
         * Collectors.collectingAndThen：第一个参数是一个收集器，第二个参数是一个Function接口，用来将结果再最终处理一下
         * 根据日期去重后，将收集到TreeSet中的结果再放入ArrayList中
         */
        ArrayList<DailyData> arrayList = dataList.stream()
                .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(DailyData::getDate))), ArrayList::new));
        System.out.println("根据日期去重后，结果再放入ArrayList中：" + arrayList);
    }
}
