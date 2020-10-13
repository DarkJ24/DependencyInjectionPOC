package com.darkj24.ioc.models;

import com.darkj24.ioc.enums.AutowiringMode;
import com.darkj24.ioc.enums.ScannedClassType;
import com.darkj24.ioc.enums.Scope;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScannedClassXML implements ScannedClass {

    private Class<?> type;

    private Constructor<?> constructor;

    private Map<String,Object> instances;

    private Method[] beans;

    private Method[] requiredMethods = new Method[0];

    private final List<ScannedClass> dependantClasses;

    private final List<ScannedClass> dependencyClasses;

    private boolean lazyInit = false;

    private Method initMethod;

    private Method destroyMethod;

    private Scope scope;

    private AutowiringMode autowiringMode;

    private ScannedClassType scanType;

    private Method[] setterMethods;

    private ScannedClassXML(ScannedClassBuilder builder) {
        this.type = builder.type;
        this.constructor = builder.constructor;
        this.beans = builder.beans;
        this.dependantClasses = builder.dependantClasses;
        this.dependencyClasses = builder.dependencyClasses;
        this.lazyInit = builder.lazyInit;
        this.initMethod = builder.initMethod;
        this.destroyMethod = builder.destroyMethod;
        this.scope = builder.scope;
        this.autowiringMode = builder.autowiringMode;
        this.scanType = ScannedClassType.XML;
        this.instances = new HashMap<>();
        this.setterMethods = new Method[0];
    }

    @Override
    public Class<?> getType() {
        return this.type;
    }

    @Override
    public void setType(Class<?> type) {
        this.type = type;
    }

    @Override
    public Constructor<?> getConstructor() {
        return this.constructor;
    }

    @Override
    public void setConstructor(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    @Override
    public Map<String, Object> getInstances() {
        return this.instances;
    }

    @Override
    public void addInstance(String key, Object instance) {
        this.instances.put(key, instance);
    }

    @Override
    public void resetInstances() {
        this.instances.clear();
    }

    @Override
    public void removeInstance(String key) {
        this.instances.remove(key);
    }

    @Override
    public Method[] getBeans() {
        return this.beans;
    }

    @Override
    public void setBeans(Method[] beans) {
        this.beans = beans;
    }

    @Override
    public Method[] getRequiredMethods() {
        return this.requiredMethods;
    }

    @Override
    public void setRequiredMethods(Method[] methods) {
        this.requiredMethods = methods;
    }

    @Override
    public List<ScannedClass> getDependantServices() {
        return Collections.unmodifiableList(this.dependantClasses);
    }

    @Override
    public void addDependantService(ScannedClass dependantClasses) {
        this.dependantClasses.add(dependantClasses);
    }

    @Override
    public List<ScannedClass> getDependencyServices() {
        return Collections.unmodifiableList(this.dependencyClasses);
    }

    @Override
    public void addDependencyServices(ScannedClass dependencyClasses) {
        this.dependencyClasses.add(dependencyClasses);
    }

    @Override
    public Method getInitMethod() {
        return this.initMethod;
    }

    @Override
    public void setInitMethod(Method method) {
        this.initMethod = method;
    }

    @Override
    public Method getDestroyMethod() {
        return this.destroyMethod;
    }

    @Override
    public void setDestroyMethod(Method method) {
        this.destroyMethod = method;
    }

    @Override
    public Method[] getSetterMethods() {
        return this.setterMethods;
    }

    @Override
    public void setSetterMethods(Method[] setterMethods) {
        this.setterMethods = setterMethods;
    }

    @Override
    public boolean isLazyInit() {
        return this.lazyInit;
    }

    @Override
    public void setLazyInit(boolean isLazyInit) {
        this.lazyInit = isLazyInit;
    }

    @Override
    public Scope getScope() {
        return this.scope;
    }

    @Override
    public void setScope(Scope scope) {
        this.scope = scope;
    }

    @Override
    public AutowiringMode getAutowiringMode() {
        return this.autowiringMode;
    }

    @Override
    public void setAutowiringMode(AutowiringMode autowiringMode) {
        this.autowiringMode = autowiringMode;
    }

    @Override
    public ScannedClass merge(ScannedClass secondClass) {
        return secondClass.merge(this);
    }

    @Override
    public ScannedClassType getScanType() {
        return this.scanType;
    }

    @Override
    public int hashCode() {
        if (this.type == null) {
            return super.hashCode();
        }

        return this.type.hashCode();
    }

    @Override
    public String toString() {
        if (this.type == null) {
            return super.toString();
        }

        return this.type.getName();
    }

    public static class ScannedClassBuilder{

        private Class<?> type;
        private Constructor<?> constructor;
        private Object instance; // No se agrega con el builder
        private Method[] beans;
        private List<ScannedClass> dependantClasses;
        private List<ScannedClass> dependencyClasses;
        private boolean lazyInit = false;
        private Method initMethod;
        private Method destroyMethod;
        private Scope scope;
        private AutowiringMode autowiringMode;

        public ScannedClassBuilder(Class<?> type) {
            this.type=type;
        }

        public ScannedClassBuilder addConstructor (Constructor<?> constructor){
            if(constructor != null) {
                this.constructor = constructor;
            }
            return this;
        }

        public ScannedClassBuilder addMethods (Method[] beans){
            if(beans != null) {
                this.beans = beans;
            }
            return this;
        }

        public ScannedClassBuilder addDependantClasses (List<ScannedClass> dependantClasses){
            if(dependantClasses != null) {
                this.dependantClasses = dependantClasses;
            }
            return this;
        }

        public ScannedClassBuilder addDependencyClasses (List<ScannedClass> dependencyClasses){
            if(dependencyClasses != null) {
                this.dependencyClasses=dependencyClasses;
            }
            return this;
        }

        public ScannedClassBuilder addLazyInit(boolean lazyInit){
            this.lazyInit = lazyInit;
            return this;
        }

        public ScannedClassBuilder addInitMethod(Method initMethod){
            if(initMethod != null) {
                this.initMethod = initMethod;
            }
            return this;
        }

        public ScannedClassBuilder addDestroyMethod(Method destroyMethod){
            if(destroyMethod != null) {
                this.destroyMethod = destroyMethod;
            }
            return this;
        }

        public ScannedClassBuilder addScope(Scope scope){
            if(scope != null) {
                this.scope = scope;
            }
            return this;
        }

        public ScannedClassBuilder addAutowiringMode(AutowiringMode autowiringMode){
            if(autowiringMode != null) {
                this.autowiringMode = autowiringMode;
            }
            return this;
        }

        public ScannedClassXML build(){
            ScannedClassXML scannedClass = new ScannedClassXML(this);
            return scannedClass;
        }

    }



}
