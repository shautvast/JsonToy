package nl.sanderhautvast.json.ser;


import nl.sanderhautvast.json.ser.nested.Bean1;

public class ExampleMapper extends BaseMapper{
    @Override
    protected void json(StringBuilder b, Object o) {
        Bean1 value = (Bean1)o;
        b.append("{");
        b.append("data1");
        b.append(":");
        Mapper.json(b, value.getData1());
        b.append("}");
    }
}
