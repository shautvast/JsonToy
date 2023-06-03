package nl.jssl.jsontoy.serialize.performance;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import nl.jssl.jsontoy.serialize.Serializer;
import nl.jssl.jsontoy.serialize.nested.Bean1;
import nl.jssl.jsontoy.serialize.nested.Bean2;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonComparisonTest {
    private static final int ITERATIONS = 20;
    private static final int INNERLOOP_COUNT = 100000;
    List<String> trashbin = new ArrayList<>();

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
                jsonString = Serializer.toJSONString(bean1);
                trashbin.add(jsonString);
            }
            System.out.printf("% 7d%n",System.currentTimeMillis() - tt0);
        }

    }
}
