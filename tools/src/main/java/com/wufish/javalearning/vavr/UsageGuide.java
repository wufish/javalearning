package com.wufish.javalearning.vavr;

import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Option;

/**
 * @Author wzj
 * @Create time: 2019/04/25 18:20
 * @Description:
 */
public class UsageGuide {

    private static void testTuple() {
        // 通过静态工厂方法Tuple.of()创建一个元组
        Tuple2<String, Integer> java8 = Tuple.of("java", 8);
        // 元素访问
        System.out.println(java8._1);

        // 分量方式的Map计算元组中每个元素的函数，返回另一个元组。
        Tuple2<String, Integer> that = java8.map(s -> s.substring(2) + "vr", i -> i / 8);

        // 还可以使用一个映射函数映射元组。
        java8.map((s, i) -> Tuple.of(s.substring(2) + "vr", i / 8));
        // Transform根据元组的内容创建一个新类型。
        java8.apply((s, i) -> Tuple.of(s.substring(2) + "vr", i / 8));
    }

    private static void testFunctions() {
        // Function0 ... Function8
        // sum.apply(1, 2) = 3
       /* Function2<Integer, Integer, Integer> sum = (a, b) -> a + b;
        Function2<Integer, Integer, Integer> sum = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer a, Integer b) {
                return a + b;
            }
        };
        Function3<String, String, String, String> function3 =
                Function3.of(this::methodWhichAccepts3Parameters);*/
        // Composition g(f(x))
        Function1<Integer, Integer> plusOne = a -> a + 1;
        Function1<Integer, Integer> multiplyByTwo = a -> a * 2;

        Function1<Integer, Integer> add1AndMultiplyBy2 = plusOne.andThen(multiplyByTwo);

        add1AndMultiplyBy2.apply(2);

        // lifting 提升
        Function2<Integer, Integer, Integer> divide = (a, b) -> a / b;
        // 我们使用lift将divide转换为为所有输入定义的总函数。

        Function2<Integer, Integer, Option<Integer>> safeDivide = Function2.lift(divide);
        // = None
        Option<Integer> i1 = safeDivide.apply(1, 0); //(1)
        // = Some(2)
        Option<Integer> i2 = safeDivide.apply(4, 2); //(2)

        //
    }

    public static void main(String[] args) {
        //testTuple();
    }
}
