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
        dataList.add(new DailyData(LocalDate.now(), BigDecimal.TEN));
        dataList.add(new DailyData(LocalDate.now().plusWeeks(1), BigDecimal.TEN));

        /**
         * 两个参数的Collectors.toMap，默认创建的是HashMap，当key重复时，直接抛出异常
         * public static <T, K, U>
         *     Collector<T, ?, Map<K,U>> toMap(Function<? super T, ? extends K> keyMapper,
         *                                     Function<? super T, ? extends U> valueMapper) {
         *         return toMap(keyMapper, valueMapper, throwingMerger(), HashMap::new);
         *     }
         */
        Map<LocalDate, DailyData> dateMap = dataList.stream()
                .collect(Collectors.toMap(DailyData::getDate, Function.identity()));
        System.out.println("dateMap：" + dateMap);

        //此处put一个重复的key
        dataList.add(new DailyData(LocalDate.now(), BigDecimal.ONE));

        /**
         * 三个参数的Collectors.toMap，第三个参数是一个BinaryOperator函数式接口，用来指定当key重复时的操作
         * public static <T, K, U>
         * Collector<T, ?, Map<K,U>> toMap(Function<? super T, ? extends K> keyMapper,
         *                                 Function<? super T, ? extends U> valueMapper,
         *                                 BinaryOperator<U> mergeFunction) {
         *     return toMap(keyMapper, valueMapper, mergeFunction, HashMap::new);
         * }
         */
        Map<LocalDate, DailyData> oldDateMap = dataList.stream()
                .collect(Collectors.toMap(DailyData::getDate, Function.identity(), (c1, c2) -> c1));
        System.out.println("重复key，保留旧值：" + oldDateMap);

        /**
         * 四个参数的Collectors.toMap，第四个参数是一个Supplier函数式接口，生产者，指定生成Map的类型
         * public static <T, K, U, M extends Map<K, U>>
         * Collector<T, ?, M> toMap(Function<? super T, ? extends K> keyMapper,
         *                          Function<? super T, ? extends U> valueMapper,
         *                          BinaryOperator<U> mergeFunction,
         *                          Supplier<M> mapSupplier) {}
         */
        TreeMap<LocalDate, DailyData> dateTreeMap = dataList.stream()
                .collect(Collectors.toMap(DailyData::getDate, Function.identity(), (c1, c2) -> c1, TreeMap::new));
        System.out.println("返回TreeMap：" + dateTreeMap);

        /**
         * 一个参数的Collectors.groupingBy()，用ArrayList收集，默认创建的是HashMap
         * public static <T, K> Collector<T, ?, Map<K, List<T>>>
         *     groupingBy(Function<? super T, ? extends K> classifier) {
         *         return groupingBy(classifier, toList());
         *     }
         * public static <T, K, A, D>
         *     Collector<T, ?, Map<K, D>> groupingBy(Function<? super T, ? extends K> classifier,
         *                                           Collector<? super T, A, D> downstream) {
         *         return groupingBy(classifier, HashMap::new, downstream);
         *     }
         */
        Map<LocalDate, List<DailyData>> groupByDate = dataList.stream()
                .collect(Collectors.groupingBy(DailyData::getDate));
        System.out.println("按日期分组：" + groupByDate);

        /**
         * 两个参数的Collectors.groupingBy()，第二个参数是一个收集器，默认创建的是HashMap
         * public static <T, K, A, D>
         * Collector<T, ?, Map<K, D>> groupingBy(Function<? super T, ? extends K> classifier,
         *                                       Collector<? super T, A, D> downstream) {
         *     return groupingBy(classifier, HashMap::new, downstream);
         * }
         */
        Map<LocalDate, Long> countMap = dataList.stream()
                .collect(Collectors.groupingBy(DailyData::getDate, Collectors.counting()));
        //output：{2020-08-27=1, 2020-08-20=2}
        System.out.println("按日期分组后，统计每组数据个数：" + countMap);

        /**
         * Collectors.mapping()：第一个参数是一个Function接口，用来对流中的元素做进一步属性转换；第二个参数是一个收集器
         * public static <T, U, A, R>
         * Collector<T, ?, R> mapping(Function<? super T, ? extends U> mapper,
         *                            Collector<? super U, A, R> downstream) {}
         */
        Map<LocalDate, List<BigDecimal>> dataMap = dataList.stream()
                .collect(Collectors.groupingBy(DailyData::getDate, Collectors.mapping(DailyData::getData, Collectors.toList())));
        //output：{2020-08-27=[10], 2020-08-20=[10, 1]}
        System.out.println("按日期分组后，返回每组的数据量：" + dataMap);

        Map<LocalDate, BigDecimal> dataReduceMap = dataList.stream()
                .collect(Collectors.groupingBy(DailyData::getDate, Collectors.mapping(DailyData::getData, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));
        //output：{2020-08-27=10, 2020-08-20=11}
        System.out.println("按日期分组后，返回每组的数据量总和：" + dataReduceMap);

        /**
         * 三个参数的Collectors.groupingBy() ，第二个参数是一个Supplier函数式接口，生产者，用来指定生成Map的类型
         * public static <T, K, D, A, M extends Map<K, D>>
         * Collector<T, ?, M> groupingBy(Function<? super T, ? extends K> classifier,
         *                               Supplier<M> mapFactory,
         *                               Collector<? super T, A, D> downstream) {}
         */
        LinkedHashMap<LocalDate, List<DailyData>> dateLinkedHashMap = dataList.stream()
                .collect(Collectors.groupingBy(DailyData::getDate, LinkedHashMap::new, Collectors.toList()));
        System.out.println("按日期分组，返回LinkedHashMap：" + dateLinkedHashMap);
    }
}
