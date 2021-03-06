package com.darkj24.ioc.models;

import com.darkj24.ioc.enums.AutowiringMode;
import com.darkj24.ioc.enums.ScannedClassType;
import com.darkj24.ioc.enums.Scope;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public interface ScannedClass {

    public ScannedClass merge(ScannedClass secondClass);

    public ScannedClassType getScanType();

    public Class<?> getType();

    public void setType(Class<?> type);

    public Constructor<?> getConstructor();

    public void setConstructor(Constructor<?> constructor);

    public Map<String, Object> getInstances();

    public void addInstance(String key, Object instance);

    public void resetInstances();

    public void removeInstance(String key);

    public Method[] getBeans();

    public void setBeans(Method[] beans);

    public Method[] getSetterMethods();

    public void setSetterMethods(Method[] setterMethods);

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
