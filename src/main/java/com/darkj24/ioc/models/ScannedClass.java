package com.darkj24.ioc.models;

import com.darkj24.ioc.enums.AutowiringMode;
import com.darkj24.ioc.enums.Scope;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public interface ScannedClass {

    public Class<?> getType();

    public void setType(Class<?> type);

    public Constructor<?> getConstructor();

    public void setConstructor(Constructor<?> constructor);

    public Object getInstance();

    public void setInstance(Object instance);

    public Method[] getBeans();

    public void setBeans(Method[] beans);

    public Method[] getRequiredMethods();

    public void setRequiredMethods(Method[] methods);

    public List<ScannedClass> getDependantServices();

    public void addDependantService(ScannedClass dependantClasses);

    public List<ScannedClass> getDependencyServices();

    public void addDependencyServices(ScannedClass dependencyClasses);

    public Method getInitMethod();

    public void setInitMethod(Method method);

    public Method getDestroyMethod();

    public void setDestroyMethod(Method method);

    public boolean isLazyInit();

    public void setLazyInit(boolean isLazyInit);

    public Scope getScope();

    public void setScope(Scope scope);

    public AutowiringMode getAutowiringMode();

    public void setAutowiringMode(AutowiringMode autowiringMode);
}
