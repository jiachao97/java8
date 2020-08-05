package sorted;

import model.DailyData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * description：排序demo
 * Stream<T> sorted(); //自然排序，需要实现Comparable接口
 * Stream<T> sorted(Comparator<? super T> comparator); //定制排序，Comparator可以使用lambda表达式初始化
 *
 * @author jiac
 * @date 2020/8/5 15:12
 */
public class SortedDemo {

    public static void main(String[] args){
        List<DailyData> dataList = new ArrayList<>();
        dataList.add(DailyData.builder().data(20).date(LocalDate.now()).build());
        dataList.add(DailyData.builder().data(20).date(LocalDate.now().minusWeeks(1)).build());
        dataList.add(DailyData.builder().data(10).date(LocalDate.now()).build());
        dataList.add(DailyData.builder().data(null).date(LocalDate.now().plusWeeks(1)).build());

        System.out.println("未排序=" + dataList);

        //按日期排序
        List<DailyData> sortByDate = dataList.stream()
                .sorted(Comparator.comparing(DailyData::getDate))
                .collect(Collectors.toList());
        System.out.println("按日期排序=" + sortByDate);

        //按日期排序后，逆序
        List<DailyData> reversedByDate = dataList.stream()
                .sorted(Comparator.comparing(DailyData::getDate).reversed())
                .collect(Collectors.toList());
        System.out.println("按日期排序后，逆序=" + reversedByDate);

        //按日期排序后，再按数据量排序
        List<DailyData> sortByDateThenData = dataList.stream()
                .sorted(Comparator.comparing(DailyData::getDate)
                .thenComparing(DailyData::getData))
                .collect(Collectors.toList());
        System.out.println("按日期排序后，再按数据量排序=" + sortByDateThenData);

        //按数据量排序，排序时忽略Null值（如果排序的字段为null，NPE）
        List<DailyData> sortByDataIgnoreNull = dataList.stream()
                .filter(c -> Objects.nonNull(c.getData()))
                .sorted(Comparator.comparing(DailyData::getData))
                .collect(Collectors.toList());
        System.out.println("按数据量排序，忽略Null值=" + sortByDataIgnoreNull);

        //按数据量排序，Null值放在最前面
        List<DailyData> sortByDataFirstNull = dataList.stream()
                .sorted(Comparator.comparing(DailyData::getData, Comparator.nullsFirst(Integer::compareTo)))
                .collect(Collectors.toList());
        System.out.println("按数据量排序，Null值放在最前面=" + sortByDataFirstNull);

        //按数据量排序，Null值放在最后面
        List<DailyData> sortByDataLastNull = dataList.stream()
                .sorted(Comparator.comparing(DailyData::getData, Comparator.nullsLast(Integer::compareTo)))
                .collect(Collectors.toList());
        System.out.println("按数据量排序，Null值放在最后面=" + sortByDataLastNull);
    }
}
