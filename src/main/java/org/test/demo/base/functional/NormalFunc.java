package org.test.demo.base.functional;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class NormalFunc {
    public static void main(String[] args) {
        System.out.println(builde(() -> "sss"));

        // consume("lilli", name -> System.out.println(new
        // StringBuilder(name).toString()));
        consume("lili", System.out::println);
    }

    // Supplier<T> 生产型接口 get
    static String builde(Supplier<String> supplier) {
        return supplier.get();
    }

    // Consumer<T> 消费型接口 accept
    static void consume(String name, Consumer<String> consumer) {
        consumer.accept(name);
    }

}