package reduce;

import model.DailyData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * description：reduce
 *
 * @author jiac
 * @date 2020/8/22 11:04
 */
public class ReduceDemo {

    public static void main(String[] args){
        List<DailyData> dataList = new ArrayList<>();
        dataList.add(new DailyData(LocalDate.now(), BigDecimal.TEN));
        dataList.add(new DailyData(LocalDate.now(), BigDecimal.ZERO));
        dataList.add(new DailyData(LocalDate.now().plusWeeks(1), BigDecimal.ONE));

        BigDecimal count1 = dataList.stream()
                .map(DailyData::getData)
                .reduce(BigDecimal.ZERO, (c1, c2) -> c1.add(c2));

        BigDecimal count2 = dataList.stream()
                .map(DailyData::getData)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("reduce：" + count2);
    }
}
