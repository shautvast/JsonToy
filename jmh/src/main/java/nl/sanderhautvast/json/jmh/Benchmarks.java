package nl.sanderhautvast.json.jmh;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.sanderhautvast.json.ser.Mapper;
import org.openjdk.jmh.annotations.*;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

//@State(Scope.Thread)
//@BenchmarkMode(Mode.AverageTime)
//@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class Benchmarks {

    private static final int ITERATIONS = 10;

//    @Benchmark
    public void testJson() {
        Bean1 bean1;
        Bean2 bean2;

        for (int i = 0; i < ITERATIONS; i++) {
            bean1 = new Bean1();
            bean2 = new Bean2();
            bean1.setData1(UUID.randomUUID().toString());
            bean1.setBean2(bean2);
            bean2.setData2(UUID.randomUUID().toString());
            Mapper.json(bean1);
        }
    }

//    @Benchmark
    public void testJackson() throws JsonProcessingException {
        Bean1 bean1;
        Bean2 bean2;
        ObjectMapper mapper = new ObjectMapper();
        for (int i = 0; i < ITERATIONS; i++) {
            bean1 = new Bean1();
            bean2 = new Bean2();
            bean1.setData1(UUID.randomUUID().toString());
            bean1.setBean2(bean2);
            bean2.setData2(UUID.randomUUID().toString());
            mapper.writeValueAsString(bean1);
        }
    }

}
