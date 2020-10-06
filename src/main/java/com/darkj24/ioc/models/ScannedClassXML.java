package com.darkj24.ioc.models;

import com.darkj24.ioc.enums.AutowiringMode;
import com.darkj24.ioc.enums.Scope;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScannedClassXML implements ScannedClass {

    private Class<?> type;

    private Constructor<?> targetConstructor;

    private Object instance;

    private Method[] beans;

    private final List<ScannedClass> dependantClasses;

    private final List<ScannedClass> dependencyClasses;

    private boolean lazyInit = false;

    private Method initMethod;

    private Method destroyMethod;

    private Scope scope;

    private AutowiringMode autowiringMode;

    private ScannedClassXML(ScannedClassBuilder builder) {
        this.type = builder.type;
        this.targetConstructor = builder.targetConstructor;
        this.beans = builder.beans;
        this.dependantClasses = builder.dependantClasses;
        this.dependencyClasses = builder.dependencyClasses;
        this.lazyInit = builder.lazyInit;
        this.initMethod = builder.initMethod;
        this.destroyMethod = builder.destroyMethod;
        this.scope = builder.scope;
        this.autowiringMode = builder.autowiringMode;
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
    public Constructor<?> getTargetConstructor() {
        return this.targetConstructor;
    }

    @Override
    public void setTargetConstructor(Constructor<?> targetConstructor) {
        this.targetConstructor = targetConstructor;
    }

    @Override
    public Object getInstance() {
        return this.instance;
    }

    @Override
    public void setInstance(Object instance) {
        this.instance = instance;
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
        this.dependantClasses.add(dependencyClasses);
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
        private Constructor<?> targetConstructor;
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

        public ScannedClassBuilder addConstructor (Constructor<?> targetConstructor){
            this.targetConstructor=targetConstructor;
            return this;
        }

        public ScannedClassBuilder addMethods (Method[] beans){
            this.beans=beans;
            return this;
        }

        public ScannedClassBuilder addMDependantClasses (List<ScannedClass> dependantClasses){
            this.dependantClasses=dependantClasses;
            return this;
        }

        public ScannedClassBuilder addMDependencyClasses (List<ScannedClass> dependencyClasses){
            this.dependencyClasses=dependencyClasses;
            return this;
        }

        public ScannedClassBuilder addLazyInit(boolean lazyInit){
            this.lazyInit=lazyInit;
            return this;
        }

        public ScannedClassBuilder addInitMethod(Method initMethod){
            this.initMethod=initMethod;
            return this;
        }

        public ScannedClassBuilder addDestroyMethod(Method destroyMethod){
            this.destroyMethod=destroyMethod;
            return this;
        }

        public ScannedClassBuilder addScope(Scope scope){
            this.scope=scope;
            return this;
        }

        public ScannedClassBuilder addSAutowiringMode(AutowiringMode autowiringMode){
            this.autowiringMode=autowiringMode;
            return this;
        }

        public ScannedClassXML build(){
            ScannedClassXML scannedClass = new ScannedClassXML(this);
            return scannedClass;
        }

    }



}
