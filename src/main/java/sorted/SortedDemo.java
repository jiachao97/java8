package sorted;

import model.DailyData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * description：排序demo
 * 使用Stream对List和Map排序
 *
 * @author jiac
 * @date 2020/8/5 15:12
 */
public class SortedDemo {

    public static void main(String[] args){
        //list排序
        List<DailyData> dataList = new ArrayList<>();
        dataList.add(new DailyData(LocalDate.now(), BigDecimal.TEN));
        dataList.add(new DailyData(LocalDate.now().minusWeeks(1), BigDecimal.TEN));
        dataList.add(new DailyData(LocalDate.now(), BigDecimal.ONE));
        dataList.add(new DailyData(LocalDate.now().plusWeeks(1), null));

        System.out.println("未排序：" + dataList);

        //按日期排序，Comparator是一个函数式接口，可以使用lambda表达式实现
        List<DailyData> sortByDate = dataList.stream()
                .sorted(Comparator.comparing(DailyData::getDate))
                .collect(Collectors.toList());
        System.out.println("按日期排序：" + sortByDate);

        //按日期排序后，逆序
        List<DailyData> reversedByDate = dataList.stream()
                .sorted(Comparator.comparing(DailyData::getDate).reversed())
                .collect(Collectors.toList());
        System.out.println("按日期排序后，逆序：" + reversedByDate);

        //按日期排序后，再按数据量排序
        List<DailyData> sortByDateThenData = dataList.stream()
                .sorted(Comparator.comparing(DailyData::getDate)
                .thenComparing(DailyData::getData))
                .collect(Collectors.toList());
        System.out.println("按日期排序后，再按数据量排序：" + sortByDateThenData);

        //按数据量排序，排序时忽略Null值（如果排序的字段为null，NPE）
        List<DailyData> sortByDataIgnoreNull = dataList.stream()
                .filter(c -> Objects.nonNull(c.getData()))
                .sorted(Comparator.comparing(DailyData::getData))
                .collect(Collectors.toList());
        System.out.println("按数据量排序，忽略Null值：" + sortByDataIgnoreNull);

        /**
         * 按数据量排序，Null值放在最前面
         * 注意Comparator.nullsFirst的方法引用中，比较的字段是BigDecimal类型的，如果前后类型不一致，会报错：Non-static method cannot be referenced from a static context
         */
        List<DailyData> sortByDataFirstNull = dataList.stream()
                .sorted(Comparator.comparing(DailyData::getData, Comparator.nullsFirst(BigDecimal::compareTo)))
                .collect(Collectors.toList());
        //output：[DailyData(data=null, date=2020-08-13), DailyData(data=1, date=2020-08-06), DailyData(data=10, date=2020-08-06), DailyData(data=10, date=2020-07-30)]
        System.out.println("按数据量排序，Null值放在最前面：" + sortByDataFirstNull);

        //按数据量排序，Null值放在最后面
        List<DailyData> sortByDataLastNull = dataList.stream()
                .sorted(Comparator.comparing(DailyData::getData, Comparator.nullsLast(BigDecimal::compareTo)))
                .collect(Collectors.toList());
        System.out.println("按数据量排序，Null值放在最后面：" + sortByDataLastNull);

        //Map排序
        Map<LocalDate, BigDecimal> map = new HashMap<>();
        map.put(LocalDate.now(), BigDecimal.TEN);
        map.put(LocalDate.now().minusWeeks(1), BigDecimal.ZERO);
        map.put(LocalDate.now().plusWeeks(1), BigDecimal.ONE);

        System.out.println("未排序：" + map);

        /**
         * public static <K extends Comparable<? super K>, V> Comparator<Map.Entry<K,V>> comparingByKey() {
         *     return (Comparator<Map.Entry<K, V>> & Serializable)
         *          (c1, c2) -> c1.getKey().compareTo(c2.getKey());
         * }
         * 对任意的c1, c2进行比较，然后将结果强制转换成一个可序列化的Comparator<Map.Entry<K, V>>
         *
         * public static <T, K, U, M extends Map<K, U>>
         * Collector<T, ?, M> toMap(Function<? super T, ? extends K> keyMapper,
         *                          Function<? super T, ? extends U> valueMapper,
         *                          BinaryOperator<U> mergeFunction,
         *                          Supplier<M> mapSupplier) {}
         * 前两个参数是一个Function函数式接口，用来指定生成key和value的策略；
         * 第三个参数用来指定当key重复时的操作，这里key重复保留旧值；
         * 第四个参数用来指定生成Map的类型，这里要保证排序后的顺序，用LinkedHashMap
         */
        LinkedHashMap<LocalDate, BigDecimal> sortedByKey = map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (c1, c2) -> c1, LinkedHashMap::new));
        System.out.println("按key排序：" + sortedByKey);

        //按key排序逆序
        LinkedHashMap<LocalDate, BigDecimal> reverseOrderByKey = map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (c1, c2) -> c1, LinkedHashMap::new));
        System.out.println("按key排序逆序：" + reverseOrderByKey);

        //按value排序
        LinkedHashMap<LocalDate, BigDecimal> sortedByValue = map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (c1, c2) -> c1, LinkedHashMap::new));
        System.out.println("按value排序：" + sortedByValue);

        //按value排序逆序
        LinkedHashMap<LocalDate, BigDecimal> reversedByValue = map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (c1, c2) -> c1, LinkedHashMap::new));
        System.out.println("按value排序逆序：" + reversedByValue);
    }
}
