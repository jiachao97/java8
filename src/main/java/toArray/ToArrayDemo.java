package toArray;

import model.DailyData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * description：Stream.toArray()
 *
 * @author jiac
 * @date 2020/8/22 10:31
 */
public class ToArrayDemo {

    public static void main(String[] args){
        List<DailyData> dataList = new ArrayList<>();
        dataList.add(DailyData.builder().data(BigDecimal.TEN).date(LocalDate.now()).build());
        dataList.add(DailyData.builder().data(BigDecimal.ZERO).date(LocalDate.now()).build());
        dataList.add(DailyData.builder().data(BigDecimal.TEN).date(LocalDate.now().plusWeeks(1)).build());

        DailyData[] array1 = dataList.stream()
                .toArray(c -> new DailyData[dataList.size()]);

        DailyData[] array2 = dataList.stream()
                .toArray(DailyData[]::new);
    }
}
