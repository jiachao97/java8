package collectors;

import model.DailyData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * description：list / map合并
 *
 * @author jiac
 * @date 2020/9/12 16:28
 */
public class MergeDemo {

    public static void main(String[] args){
        Map<LocalDate, DailyData> mapA = new HashMap<>();
        mapA.put(LocalDate.now(), new DailyData(LocalDate.now(), BigDecimal.TEN));
        Map<LocalDate, DailyData> mapB = new HashMap<>();
        mapB.put(LocalDate.now(), new DailyData(LocalDate.now(), BigDecimal.ONE));
        mapB.put(LocalDate.now().plusWeeks(1), new DailyData(LocalDate.now().plusWeeks(1), BigDecimal.TEN));

        Map<LocalDate, DailyData> valueByMapB = Stream.of(mapA, mapB)
                .flatMap(c -> c.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue,
                        (c1, c2) -> new DailyData(c1.getDate(), c2.getData())));
        System.out.println("合并map后，重复key，value使用mapB的值：" + valueByMapB);

        Map<LocalDate, DailyData> valueReduce = Stream.of(mapA, mapB)
                .flatMap(c -> c.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue,
                        (c1, c2) -> new DailyData(c1.getDate(), c1.getData().add(c2.getData()))));
        System.out.println("合并map后，重复key，value累加：" + valueReduce);

        Map<LocalDate, List<DailyData>> valueByList = Stream.of(mapA, mapB)
                .flatMap(c -> c.entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
        System.out.println("合并map后，重复key，value用List收集：" + valueByList);

        long dupKeyCount = Stream.of(mapA, mapB)
                .flatMap(c -> c.entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.counting()))
                .values()
                .stream()
                .filter(c -> c > 1)
                .count();
        System.out.println("统计两个map有多少重复的key：" + dupKeyCount);
    }
}
