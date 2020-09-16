package com.xml;

public class DataStructureProperties {

    private String value = null;
    private String ref = null;

    public DataStructureProperties(String value, String ref) {
        this.value = value;
        this.ref = ref;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}
