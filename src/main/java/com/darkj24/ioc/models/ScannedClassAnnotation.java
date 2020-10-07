package com.darkj24.ioc.models;

import com.darkj24.ioc.enums.AutowiringMode;
import com.darkj24.ioc.enums.ScannedClassType;
import com.darkj24.ioc.enums.Scope;
import com.darkj24.ioc.services.ListMerger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScannedClassAnnotation implements ScannedClass {

    private Class<?> type;

    private Constructor<?> constructor;

    private Object instance;

    private Method[] beans;

    private Method[] requiredMethods;

    private List<ScannedClass> dependantClasses;

    private List<ScannedClass> dependencyClasses;

    private boolean lazyInit = false;

    private Method initMethod;

    private Method destroyMethod;

    private Scope scope;

    private AutowiringMode autowiringMode;

    private ScannedClassType scanType;

    public ScannedClassAnnotation() {
        this.dependantClasses = new ArrayList<>();
        this.dependencyClasses = new ArrayList<>();
        this.scanType = ScannedClassType.ANNOTATION;
    }

    public ScannedClassAnnotation(Class<?> type, Constructor<?> constructor,
                                  Method initMethod, Method destroyMethod,
                                  Scope scope, AutowiringMode autowiringMode, boolean lazyInit,
                                  Method[] beans, Method[] requiredMethods) {
        this();
        this.setType(type);
        this.setConstructor(constructor);
        this.setBeans(beans);
        this.setInitMethod(initMethod);
        this.setDestroyMethod(destroyMethod);
        this.setScope(scope);
        this.setAutowiringMode(autowiringMode);
        this.setLazyInit(lazyInit);
        this.setRequiredMethods(requiredMethods);
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

    @Override
    public ScannedClass merge(ScannedClass secondClass) {
        this.scanType = ScannedClassType.BOTH;
        this.constructor = this.constructor == null ? secondClass.getConstructor() : this.constructor;
        //beans
        ListMerger<Method> methodMerger = new ListMerger<>();
        List<Method> newBeans = methodMerger.mergeLists(this.beans,secondClass.getBeans());
        this.beans = newBeans.toArray(new Method[0]);
        //requiredMethods
        this.constructor = this.constructor == null ? secondClass.getConstructor() : this.constructor;
        List<Method> newRequiredMethods = methodMerger.mergeLists(this.requiredMethods,secondClass.getRequiredMethods());
        this.requiredMethods = newRequiredMethods.toArray(new Method[0]);
        //dependantClasses
        ListMerger<ScannedClass> sClassMerger = new ListMerger<>();
        this.dependantClasses = sClassMerger.mergeLists(this.dependantClasses,secondClass.getDependantServices());
        //dependencyClasses
        this.dependencyClasses = sClassMerger.mergeLists(this.dependencyClasses,secondClass.getDependencyServices());
        this.lazyInit = this.lazyInit || secondClass.isLazyInit();
        this.initMethod = this.initMethod != null ? this.initMethod : secondClass.getInitMethod();
        this.scope = this.scope == Scope.PROTOTYPE ? this.scope : secondClass.getScope();
        this.autowiringMode = this.autowiringMode != AutowiringMode.NO ? this.autowiringMode : secondClass.getAutowiringMode();

        return this;
    }

}
