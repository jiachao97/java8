package iterate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * description：无限流demo
 * 流是惰性的，在执行终止操作之前，流的遍历不会开始，利用这一特性就可以创建无限流，使用无限流时，需要正确的做限制(limit)
 * 当前后元素间维持着某种恒定的关系时，可以考虑使用无限流
 *
 * @author jiac
 * @date 2020/8/5 13:48
 */
public class IterateDemo {

    public static void main(String[] args) {
        //创建一个从0开始，每次加2的无限流，并限制收集前十个元素
        List<Integer> integers = Stream.iterate(0, c -> c + 2)
                .limit(10)
                .collect(Collectors.toList());
        System.out.println("integers：" + integers);

        //generate接收一个Supplier函数式接口
        List<Double> randoms = Stream.generate(Math::random)
                .limit(10)
                .collect(Collectors.toList());
        System.out.println("randoms：" + randoms);

        //斐波那契数列，从[0, 1]开始，下一个数是前两个数之和，并限制收集前十个数组中的首元素
        List<Integer> fbnc = Stream.iterate(new Integer[]{0, 1}, c -> new Integer[]{c[1], c[0] + c[1]})
                .map(c -> c[0])
                .limit(10)
                .collect(Collectors.toList());
        System.out.println("fbnc：" + fbnc);
    }
}
