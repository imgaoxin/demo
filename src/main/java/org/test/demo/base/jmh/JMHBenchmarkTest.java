package org.test.demo.base.jmh;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@Fork(2)
@Threads(4)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 5)
@Measurement(iterations = 10, time = 5)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
// @State(Scope.Thread)
public class JMHBenchmarkTest {

    @Benchmark
    public void StringAdd() {
        String a = "0";
        for (int i = 0; i < 20; i++) {
            a += i;
        }
        System.out.println(a);
    }

    @Benchmark
    public void StringBuilderAppend() {
        StringBuilder b = new StringBuilder("0");
        for (int i = 0; i < 20; i++) {
            b.append(i);
        }
    }

    public static void main(String[] args) throws Exception {
        Options options = new OptionsBuilder().include(JMHBenchmarkTest.class.getSimpleName()).build();
        // .forks(2).threads(4)
        // .mode(Mode.Throughput).warmupIterations(5).measurementIterations(10)
        // .measurementTime(TimeValue.milliseconds(5)).timeUnit(TimeUnit.MICROSECONDS)
        // .output("filename")

        new Runner(options).run();
    }
}