package collectors;

import model.DailyData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * description：Stream转Map(toMap / groupingBy)
 *
 * @author jiac
 * @date 2020/8/5 16:20
 */
public class ToMapDemo {

    public static void main(String[] args){
        List<DailyData> dataList = new ArrayList<>();
        dataList.add(DailyData.builder().data(BigDecimal.TEN).date(LocalDate.now()).build());
        dataList.add(DailyData.builder().data(BigDecimal.TEN).date(LocalDate.now().plusWeeks(1)).build());

        /**
         * public static <T, K, U>
         *     Collector<T, ?, Map<K,U>> toMap(Function<? super T, ? extends K> keyMapper,
         *                                     Function<? super T, ? extends U> valueMapper) {
         *         return toMap(keyMapper, valueMapper, throwingMerger(), HashMap::new);
         *     }
         * 默认创建的是HashMap，当key重复时，直接抛出异常
         */
        Map<LocalDate, DailyData> dateMap = dataList.stream()
                .collect(Collectors.toMap(DailyData::getDate, Function.identity()));
        System.out.println("dateMap：" + dateMap);

        //此处put一个重复的key
        dataList.add(DailyData.builder().data(BigDecimal.ONE).date(LocalDate.now()).build());

        /**
         * 第三个参数是一个BinaryOperator函数式接口，通过执行lambda表达式来指定，当key重复时的操作
         * 重复key，保留旧值
         */
        Map<LocalDate, DailyData> oldDateMap = dataList.stream()
                .collect(Collectors.toMap(DailyData::getDate, Function.identity(), (c1, c2) -> c1));
        System.out.println("重复key，保留旧值：" + oldDateMap);

        /**
         * 第四个参数用于自定义map的返回类型
         * 返回TreeMap
         */
        TreeMap<LocalDate, DailyData> dateTreeMap = dataList.stream()
                .collect(Collectors.toMap(DailyData::getDate, Function.identity(), (c1, c2) -> c1, TreeMap::new));
        System.out.println("返回TreeMap：" + dateTreeMap);

        /**
         * public static <T, K> Collector<T, ?, Map<K, List<T>>>
         *     groupingBy(Function<? super T, ? extends K> classifier) {
         *         return groupingBy(classifier, toList());
         *     }
         * public static <T, K, A, D>
         *     Collector<T, ?, Map<K, D>> groupingBy(Function<? super T, ? extends K> classifier,
         *                                           Collector<? super T, A, D> downstream) {
         *         return groupingBy(classifier, HashMap::new, downstream);
         *     }
         * 默认返回的是HashMap
         * 按日期分组
         */
        Map<LocalDate, List<DailyData>> groupByDate = dataList.stream()
                .collect(Collectors.groupingBy(DailyData::getDate));
        System.out.println("按日期分组：" + groupByDate);

        /**
         * 此重载方法的第二个参数用来指定分组后，map的返回类型
         * 按日期分组，返回LinkedHashMap
         */
        LinkedHashMap<LocalDate, List<DailyData>> dateLinkedHashMap = dataList.stream()
                .collect(Collectors.groupingBy(DailyData::getDate, LinkedHashMap::new, Collectors.toList()));
        System.out.println("按日期分组，返回LinkedHashMap：" + dateLinkedHashMap);

        /**
         * Collectors.mapping的第一个参数是一个Function接口，用来对流中的元素做进一步属性转换；第二个参数是一个收集器
         * 按日期分组，返回每组数据量
         */
        Map<LocalDate, List<BigDecimal>> dataMap = dataList.stream()
                .collect(Collectors.groupingBy(DailyData::getDate, Collectors.mapping(DailyData::getData, Collectors.toList())));
        //output：{2020-08-13=[10], 2020-08-06=[10, 1]}
        System.out.println("按日期分组，返回每组数据量：" + dataMap);
    }
}
