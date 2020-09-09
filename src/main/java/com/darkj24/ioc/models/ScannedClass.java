package com.darkj24.ioc.models;

import com.darkj24.ioc.annotations.Provider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScannedClass {

    private Class<?> type;

    private Annotation annotation;

    private Constructor<?> targetConstructor;

    private Object instance;

    private Method[] beans;

    private final List<ScannedClass> dependantClasses;

    public ScannedClass() {
        this.dependantClasses = new ArrayList<>();
    }

    public ScannedClass(Class<?> type,
                          Annotation annotation, Constructor<?> targetConstructor,
                          Method[] beans) {
        this();
        this.setType(type);
        this.setAnnotation(annotation);
        this.setTargetConstructor(targetConstructor);
        this.setBeans(beans);
    }

    public Class<?> getType() {
        return this.type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public Annotation getAnnotation() {
        return this.annotation;
    }

    public void setAnnotation(Annotation annotation) {
        this.annotation = annotation;
    }

    public Constructor<?> getTargetConstructor() {
        return this.targetConstructor;
    }

    public void setTargetConstructor(Constructor<?> targetConstructor) {
        this.targetConstructor = targetConstructor;
    }

    public Object getInstance() {
        return this.instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public Method[] getBeans() {
        return this.beans;
    }

    public void setBeans(Method[] beans) {
        this.beans = beans;
    }

    public List<ScannedClass> getDependantServices() {
        return Collections.unmodifiableList(this.dependantClasses);
    }

    public void addDependantService(ScannedClass dependantClasses) {
        this.dependantClasses.add(dependantClasses);
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
