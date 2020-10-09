package com.xml;

import com.sun.istack.internal.Nullable;

import java.util.HashMap;
import java.util.Map;

public class DataStructureBean {
    private String cls;
    private String scope;
    private String constructorArg;
    private String autowiringMode;
    private boolean lazyInit;
    private String initMethod;
    private String destroyMethod;
    private Map<String, DataStructureProperties> properties = new HashMap<String,DataStructureProperties>();

    private DataStructureBean(BeanBuilder builder) {
        this.cls = builder.cls;
        this.scope = builder.scope;
        this.constructorArg = builder.constructorArg;
        this.autowiringMode = builder.autowiringMode;
        this.lazyInit = builder.lazyInit;
        this.initMethod = builder.initMethod;
        this.destroyMethod = builder.destroyMethod;
    }

    public String getCls() {
        return cls;
    }

    public String getScope() {
        return scope;
    }

    public String getConstructorArg() {
        return constructorArg;
    }

    public String getAutowiringMode() {
        return autowiringMode;
    }

    public String getInitMethod() {
        return initMethod;
    }

    public String getDestroyMethod() {
        return destroyMethod;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public void addNewProperty(String name, String value, String ref){
        this.properties.put(name, new DataStructureProperties(value,ref));

    }

    public String getPropertyValue(String name){
        return properties.get(name).getValue();
    }

    public String getPropertyRef(String name){
        return properties.get(name).getRef();
    }

    public Map<String, DataStructureProperties> getProperties() {
        return properties;
    }

    public static class BeanBuilder{
        private String cls;
        private String scope = "Singleton";
        private String constructorArg = "0";
        private String autowiringMode = "No";
        private boolean lazyInit = false;
        private String initMethod = "";
        private String destroyMethod = "";

        public BeanBuilder(String cls){
            this.cls=cls;
        }

        public BeanBuilder addScope(@Nullable String scope){
            if(scope != null && !scope.isEmpty()) {
                this.scope = scope;
            }
            return this;
        }

        public BeanBuilder addConstructorArg(@Nullable String constructorArg){
            if(constructorArg != null && !constructorArg.isEmpty()) {
                this.constructorArg = constructorArg;
            }
            return this;
        }

        public BeanBuilder addAutowiringMode(@Nullable String autowiringMode){
            if(autowiringMode != null && !autowiringMode.isEmpty()) {
                this.autowiringMode = autowiringMode;
            }
            return this;
        }

        public BeanBuilder addLazyInit(boolean lazyInit){
            this.lazyInit=lazyInit;
            return this;
        }

        public BeanBuilder addInitMethod(@Nullable String initMethod){
            if(initMethod != null && !initMethod.isEmpty()) {
                this.initMethod = initMethod;
            }
            return this;
        }
        public BeanBuilder addDestroyMethod(@Nullable String destroyMethod){
            if(destroyMethod != null && !destroyMethod.isEmpty()) {
                this.destroyMethod = destroyMethod;
            }
            return this;
        }

        public DataStructureBean build(){
            DataStructureBean beans = new DataStructureBean(this);
            return beans;
        }

    }


}
