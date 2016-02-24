package ru.funbox.jsonparser;

public class JNumber extends java.lang.Number {

    private String value;

    public JNumber(String value) {
        this.value = value;
    }

    public Integer getAsInt() {
        return intValue();
    }

    public Integer getAsInteger() {
        return intValue();
    }

    public Long getAsLong() {
        return longValue();
    }

    public Double getAsDouble() {
        return doubleValue();
    }

    public Float getAsFloat() {
        return floatValue();
    }

    public Short getAsShort() {
        return shortValue();
    }

    public Byte getAsByte() {
        return byteValue();
    }

    @Override
    public int intValue() {
        return Integer.parseInt(value);
    }

    @Override
    public long longValue() {
        return Long.parseLong(value);
    }

    @Override
    public float floatValue() {
        return Float.parseFloat(value);
    }

    @Override
    public double doubleValue() {
        return Double.parseDouble(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
