//package nl.jssl.jsontoy.serialize.nested;
//
//import static junit.framework.Assert.assertEquals;
//import nl.jssl.jsontoy.serialize.Serializer;
//
//import org.junit.Test;
//
//public class NestedBeanTest {
//    @Test
//    public void testBeans() {
//        Bean1 object1 = new Bean1();
//        object1.setData1("value1");
//        Bean2 object2 = new Bean2();
//        object2.setData2("value2");
//        object1.setBean2(object2);
//        assertEquals("{\"data1\": \"value1\",\"bean2\": {\"data2\": \"value2\"}}", Serializer.toJSONString(object1));
//        assertEquals("{\"data1\": \"value1\",\"bean2\": {\"data2\": \"value2\"}}", Serializer.toJSONString(object1));
//    }
//
//    @Test
//    public void testNullValueForBean() {
//        Bean1 object1 = new Bean1();
//        object1.setData1("value1");
//        assertEquals("{\"data1\": \"value1\",\"bean2\": null}", Serializer.toJSONString(object1));
//    }
//
//    @Test
//    public void testBeanChildren() {
//        Bean1 object1 = new Bean1();
//        object1.setData1("value1");
//        Bean2Child object3 = new Bean2Child();
//        object3.setData2("value2");
//        object3.setData3("value3");
//        object1.setBean2(object3);
//        assertEquals("{\"data1\": \"value1\",\"bean2\": {\"data3\": \"value3\",\"data2\": \"value2\"}}", Serializer.toJSONString(object1));
//    }
//}
