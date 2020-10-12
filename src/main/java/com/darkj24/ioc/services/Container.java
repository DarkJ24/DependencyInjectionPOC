package com.darkj24.ioc.services;

import com.darkj24.ioc.models.ScannedClass;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

public interface Container {

    void init(Collection<Class<?>> classes, Collection<ScannedClass> scannedClasses) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

    <T> T getInstance(Class<T> cls, String key) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

    <T> T getInstance(Class<T> cls) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

    void destroy() throws InvocationTargetException, IllegalAccessException;

    void destroy(Class<?> cls) throws InvocationTargetException, IllegalAccessException;

    void destroyInstance(Object obj) throws InvocationTargetException, IllegalAccessException;

    ScannedClass getScannedClass(Class<?> cls);

    Collection<Class<?>> getAllClasses();

    Collection<ScannedClass> getAllScannedClasses();
}
