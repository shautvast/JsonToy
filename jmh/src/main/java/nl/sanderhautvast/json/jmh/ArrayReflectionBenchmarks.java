package nl.sanderhautvast.json.jmh;

import org.openjdk.jmh.annotations.*;

import java.lang.reflect.Array;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ArrayReflectionBenchmarks {

    private static final int ITERATIONS = 10;

    public static void main(String[] args) {
        new ArrayReflectionBenchmarks().testReflectiveArray();
    }

    @Benchmark
    public void testReflectiveArray() {
        int[] r1 = {1};
        int[] r2 = {1, 2};
        int[][] table1 = {r1, r2};
        int[][] table2 = {r1, r2};
        int[][][] schema = {table1, table2};
        for (int i = 0; i < ITERATIONS; i++) {
            addArrayElements(schema);
        }
    }

    @Benchmark
    public void testNonReflectiveArray() {
        int[] r1 = {1};
        int[] r2 = {1, 2};
        int[][] table1 = {r1, r2};
        int[][] table2 = {r1, r2};
        int[][][] schema = {table1, table2};
        for (int i = 0; i < ITERATIONS; i++) {
            addIntegerArray(schema);
        }
    }

    private int addArrayElements(Object o) {
        int sum = 0;
        if (o.getClass().isArray()) {
            int length = Array.getLength(o);
            for (int i = 0; i < length; i++) {
                sum += addArrayElements(Array.get(o, i));
            }
        } else {
            sum += (Integer) o;
        }
        return sum;
    }

    private int addIntegerArray(int[][][] o) {
        int sum = 0;
        for (int[][] ints : o) {
            sum += addIntegerArray2(ints);
        }
        return sum;
    }

    private int addIntegerArray2(int[][] o) {
        int sum = 0;
        for (int[] ints : o) {
            sum += addIntegerArray3(ints);
        }
        return sum;
    }

    private int addIntegerArray3(int[] o) {
        int sum = 0;
        for (int j : o) {
            sum += j;
        }
        return sum;
    }
}
