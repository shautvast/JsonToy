package nl.sanderhautvast.json.ser.performance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.sanderhautvast.json.ser.Mapper;
import nl.sanderhautvast.json.ser.nested.Bean1;
import nl.sanderhautvast.json.ser.nested.Bean2;
import org.junit.jupiter.api.Test;

import java.util.*;

/*
 * => about 10% faster than jackson
 */
public class JacksonComparisonTest {
    private static final int ITERATIONS = 20;
    private static final int INNERLOOP_COUNT = 100000;
    private final List<String> trashbin = new ArrayList<>();

    @Test
    public void testPerformance() throws JsonProcessingException {
        System.out.println("jackson,jsontoy");
        ObjectMapper objectMapper = new ObjectMapper();
        Bean1 bean1 = new Bean1();
        Bean2 bean2 = new Bean2();
        bean1.setData1(UUID.randomUUID().toString());
        bean1.setBean2(bean2);
        bean2.setData2(UUID.randomUUID().toString());
        String valueAsString;
        String jsonString;

        for (int c = 0; c < ITERATIONS; c++) {
            trashbin.clear();
            long t0 = System.currentTimeMillis();
            for (int i = 0; i < INNERLOOP_COUNT; i++) {
                bean1 = new Bean1();
                bean2 = new Bean2();
                bean1.setData1(UUID.randomUUID().toString());
                bean1.setBean2(bean2);
                bean2.setData2(UUID.randomUUID().toString());
                valueAsString = objectMapper.writeValueAsString(bean1);
                trashbin.add(valueAsString);
            }
            System.out.printf("% 7d",(System.currentTimeMillis() - t0));
            System.out.print(",");
            trashbin.clear();
            long tt0 = System.currentTimeMillis();
            for (int i = 0; i < INNERLOOP_COUNT; i++) {
                bean1 = new Bean1();
                bean2 = new Bean2();
                bean1.setData1(UUID.randomUUID().toString());
                bean1.setBean2(bean2);
                bean2.setData2(UUID.randomUUID().toString());
                jsonString = Mapper.json(bean1);
                trashbin.add(jsonString);
            }
            System.out.printf("% 7d%n",System.currentTimeMillis() - tt0);
        }

    }
}
