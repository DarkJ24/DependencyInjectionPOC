package com.xml;

import java.util.HashMap;
import java.util.Map;

public class DataStructureBean {
    private String cls = null;
    private String scope = null;
    private String constructorArg = null;
    private boolean autowiringMode = false;
    private boolean lazyInit = false;
    private boolean destroyMethod = false;
    private Map<String, DataStructureProperties> properties = new HashMap<String,DataStructureProperties>(); //HashMap para almacenar varias dependencias

    private DataStructureBean(BeanBuilder builder) {
        this.cls = builder.cls;
        this.scope = builder.scope;
        this.constructorArg = builder.constructorArg;
        this.autowiringMode = builder.autowiringMode;
        this.lazyInit = builder.lazyInit;
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

    public boolean isAutowiringMode() {
        return autowiringMode;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public boolean isDestroyMethod() {
        return destroyMethod;
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

    public static class BeanBuilder{
        private String cls;
        private String scope;
        private String constructorArg;
        private boolean autowiringMode;
        private boolean lazyInit;
        private boolean destroyMethod;

        public BeanBuilder(String cls){
            this.cls=cls;
        }

        public BeanBuilder scope(String scope){
            this.scope=scope;
            return this;
        }

        public BeanBuilder constructorArg(String constructorArg){
            this.constructorArg=constructorArg;
            return this;
        }

        public BeanBuilder autowiringMode(boolean autowiringMode){
            this.autowiringMode=autowiringMode;
            return this;
        }

        public BeanBuilder lazyInit(boolean lazyInit){
            this.lazyInit=lazyInit;
            return this;
        }

        public BeanBuilder destroyMethod(boolean destroyMethod){
            this.destroyMethod=destroyMethod;
            return this;
        }

        public DataStructureBean build(){
            DataStructureBean beans = new DataStructureBean(this);
            return beans;
        }

    }


}
