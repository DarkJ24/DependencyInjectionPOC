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

    public ScannedClassXML() {
        this.dependantClasses = new ArrayList<>();
        this.dependencyClasses = new ArrayList<>();
    }

    public ScannedClassXML(Class<?> type, Constructor<?> targetConstructor,
                                  Method[] beans) {
        this();
        this.setType(type);
        this.setTargetConstructor(targetConstructor);
        this.setBeans(beans);
    }

    public ScannedClassXML(Class<?> type,
                                  Constructor<?> targetConstructor,
                                  Method initMethod, Method destroyMethod,
                                  Scope scope, AutowiringMode autowiringMode, boolean lazyInit,
                                  Method[] beans) {
        this();
        this.setType(type);
        this.setTargetConstructor(targetConstructor);
        this.setBeans(beans);
        this.setInitMethod(initMethod);
        this.setDestroyMethod(destroyMethod);
        this.setScope(scope);
        this.setAutowiringMode(autowiringMode);
        this.setLazyInit(lazyInit);
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
    public List<ScannedClass> getDependencieservices() {
        return Collections.unmodifiableList(this.dependencyClasses);
    }

    @Override
    public void addDependenciesService(ScannedClass dependencyClasses) {
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
}