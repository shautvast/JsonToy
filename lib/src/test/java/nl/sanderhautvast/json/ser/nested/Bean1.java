package nl.sanderhautvast.json.ser.nested;


import java.util.UUID;

@SuppressWarnings("unused")
public class Bean1 {
    private UUID data1;
    private Bean2 bean2;

    public UUID getData1() {
        return data1;
    }

    public void setData1(UUID data1) {
        this.data1 = data1;
    }

    public Bean2 getBean2() {
        return bean2;
    }

    public void setBean2(Bean2 bean2) {
        this.bean2 = bean2;
    }
}
