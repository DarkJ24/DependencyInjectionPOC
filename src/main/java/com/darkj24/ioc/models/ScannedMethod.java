package com.darkj24.ioc.models;

import java.lang.reflect.Method;

public class ScannedMethod {

    private Method method;
    private boolean isRequired;
    private boolean isBean;
    private String name;

    private ScannedMethod() {
    }

    private ScannedMethod(Method method, boolean isRequired, boolean isBean) {
        this();
        this.method = method;
        this.isRequired = isRequired;
        this.isBean = isBean;
        this.name = method.getName();
    }

    public boolean isRequired() {
        return this.isRequired;
    }

    public boolean isBean() {
        return this.isBean;
    }

    public String getName() {
        return this.name;
    }

    public Method getMethod() {
        return this.method;
    }

    public static class ScannedMethodBuilder {

        private Method method;
        private boolean isRequired;
        private boolean isBean;

        public ScannedMethodBuilder(Method method) {
            this.method = method;
            this.isRequired = false;
            this.isBean = true;
        }

        public ScannedMethodBuilder setRequired(boolean required) {
            this.isRequired = required;
            return this;
        }

        public ScannedMethodBuilder setBean(boolean bean) {
            this.isBean = bean;
            return this;
        }

        public ScannedMethod build() {
            ScannedMethod scannedMethod = new ScannedMethod(this.method, this.isRequired, this.isBean);
            return scannedMethod;
        }
    }
}
