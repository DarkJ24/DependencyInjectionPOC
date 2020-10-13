package com.darkj24.ioc.services;

import com.darkj24.ioc.annotations.Bean;
import com.darkj24.ioc.annotations.Qualifier;
import com.darkj24.ioc.enums.AutowiringMode;
import com.darkj24.ioc.enums.Scope;
import com.darkj24.ioc.exceptions.AlreadyInitializedException;
import com.darkj24.ioc.exceptions.NeedsDependencyException;
import com.darkj24.ioc.models.Constants;
import com.darkj24.ioc.models.ScannedClass;
import com.darkj24.ioc.models.ScannedClassAnnotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ContainerImpl implements Container {

    private Collection<ScannedClass> scannedClasses;

    private final Map<Class<?>, ScannedClass> mapScannedClasses;

    private boolean hasInitialized;

    private ClassScannerAnnotation scanner;

    public ContainerImpl() {
        this.scanner = new ClassScannerAnnotation();
        this.mapScannedClasses = new HashMap<>();
        this.hasInitialized = false;
    }

    @Override
    public void init(Collection<ScannedClass> scannedClasses) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (this.hasInitialized) {
            throw new AlreadyInitializedException("Class has already Initialized");
        }
        this.scannedClasses = scannedClasses;
        for (ScannedClass sClass : scannedClasses) {
            mapScannedClasses.put(sClass.getType(), sClass);
        }
        this.hasInitialized = true;
        // Check Required Methods
        // Validate cross XML check
        // Get Instances
        for (int i = 0; i < Constants.MAX_NUMBER_OF_INSTANTIATION_ITERATIONS; i++) {
            for (ScannedClass sClass : scannedClasses) {
                if (sClass.getScope() == Scope.SINGLETON && !sClass.isLazyInit()) {
                    getInstance(sClass, sClass.getType().getName());
                }
            }
        }
        // Call Setters
        for (ScannedClass sClass : scannedClasses) {
            callSetters(sClass);
        }
    }

    private Object getInstance(ScannedClass cls, String name) throws NeedsDependencyException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        Object object = null;
        Parameter[] parameters = cls.getConstructor().getParameters();
        Object[] initargs = new Object[parameters.length];
        if (cls.getScope() == Scope.SINGLETON){
            object = cls.getInstances().get(cls.getType().getName());
        } else if(cls.getScope() == Scope.PROTOTYPE) {
            object = cls.getInstances().get(name);
        }
        if (object != null) {
            return object;
        }
        // Try to create
        for (int i = 0; i < parameters.length; i++) {
            Qualifier qualifier = parameters[i].getDeclaredAnnotation(Qualifier.class);
            Object instance = getInstance(mapScannedClasses.get(parameters[i].getType()), qualifier != null ? qualifier.value() : parameters[i].getName());
            if (instance == null) {
                return null;
            }
            initargs[i] = instance;
        }
        object = cls.getConstructor().newInstance(initargs);
        // Call Init
        if (cls.getInitMethod() != null) {
            cls.getInitMethod().invoke(object);
        }
        cls.addInstance(cls.getType().getName(), object);
        // Generate Beans
        generateBeans(cls, object);
        return object;
    }

    private void generateBeans(ScannedClass cls, Object object) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        for (Method method : cls.getBeans()){
            ScannedClass beanClass = getScannedClass(method.getReturnType());
            if (beanClass == null) {
                beanClass = createBeanClass(method.getReturnType());
                mapScannedClasses.put(method.getReturnType(), beanClass);
            }
            String key = method.getReturnType().getName();
            Qualifier qAnnotation = method.getDeclaredAnnotation(Qualifier.class);
            if (qAnnotation != null) {
                key = qAnnotation.value();
            } else if (beanClass.getAutowiringMode() == AutowiringMode.BY_NAME) {
                Bean bAnnotation = method.getDeclaredAnnotation(Bean.class);
                key = bAnnotation.name() != "" ? bAnnotation.name() : method.getName();
                // Should throw if Bean name not defined?
            } else {
                if (beanClass.getInstances().get(key) != null) {
                    return;
                }
            }
            if (beanClass.getInstances().get(key) == null) {
                Parameter[] mParameters = cls.getConstructor().getParameters();
                Object[] methodArgs = new Object[mParameters.length];
                for (int i = 0; i < mParameters.length; i++) {
                    Qualifier qualifier = mParameters[i].getDeclaredAnnotation(Qualifier.class);
                    Object pInstance = getInstance(mapScannedClasses.get(mParameters[i].getType()), qualifier != null ? qualifier.value() : mParameters[i].getName());
                    if (pInstance == null) {
                        //No parameter to generate instance
                        return;
                    }
                    methodArgs[i] = pInstance;
                }
                Object generatedBean = method.invoke(object, methodArgs);
                // Call Init
                if (cls.getInitMethod() != null) {
                    cls.getInitMethod().invoke(object);
                }
                beanClass.addInstance(key, generatedBean);
                // Generate Beans
                generateBeans(cls, object);
                cls.addDependantService(beanClass);
            }
        }
    }

    private ScannedClass createBeanClass(Class<?> cls){
        return scanner.scanClass(cls, true);
    }

    private void callSetters(ScannedClass sClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        for (Map.Entry<String,Object> entry : sClass.getInstances().entrySet()) {
            for (Method method : sClass.getSetterMethods()){
                String key = method.getReturnType().getName();
                Annotation annotation = method.getDeclaredAnnotation(Qualifier.class);
                if (annotation != null) {
                    Qualifier qAnnotation = (Qualifier) annotation;
                    key = qAnnotation.value();
                } else if (sClass.getAutowiringMode() == AutowiringMode.BY_NAME) {
                    key = method.getName();
                } else {
                    if (sClass.getInstances().get(key) != null) {
                        return;
                    }
                }
                Parameter[] mParameters = method.getParameters();
                Object[] methodArgs = new Object[mParameters.length];
                for (int i = 0; i < mParameters.length; i++) {
                    Qualifier qualifier = mParameters[i].getDeclaredAnnotation(Qualifier.class);
                    Object pInstance = getInstance(mapScannedClasses.get(mParameters[i].getType()), qualifier != null ? qualifier.value() : key);
                    if (pInstance == null) {
                        //No parameter to generate instance
                        continue;
                    }
                    methodArgs[i] = pInstance;
                }
                method.invoke(entry.getValue(), methodArgs);
            }
        }
    }

    @Override
    public <T> T getInstance(Class<T> cls, String key) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        T obj = (T) mapScannedClasses.get(cls).getInstances().get(key);
        if (obj == null) {
            obj = (T) getInstance(getScannedClass(cls),key);
            callSetters(getScannedClass(cls));
        }
        return obj;
    }

    @Override
    public <T> T getInstance(Class<T> cls) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return getInstance(cls, cls.getName());
    }

    @Override
    public void destroy(Class<?> cls) throws InvocationTargetException, IllegalAccessException {
        ScannedClass sClass = mapScannedClasses.get(cls);
        if (sClass.getDestroyMethod() != null){
            for (Map.Entry<String,Object> entry : sClass.getInstances().entrySet()){
                sClass.getDestroyMethod().invoke(entry.getValue());
            }
        }
        sClass.resetInstances();
    }

    @Override
    public void destroy() throws InvocationTargetException, IllegalAccessException {
        for (Map.Entry<Class<?>,ScannedClass> entry : mapScannedClasses.entrySet()){
            destroy(entry.getKey());
        }
    }

    @Override
    public void destroyInstance(Object obj) throws InvocationTargetException, IllegalAccessException {
        ScannedClass sClass = mapScannedClasses.get(obj.getClass());
        if (sClass.getDestroyMethod() != null){
            sClass.getDestroyMethod().invoke(obj);
        }
        for (Map.Entry<String,Object> entry : sClass.getInstances().entrySet()) {
            if (obj == entry.getValue()) {
                sClass.removeInstance(entry.getKey());
                return;
            }
        }
    }

    @Override
    public ScannedClass getScannedClass(Class<?> cls) {
        return mapScannedClasses.get(cls);
    }

    @Override
    public Collection<ScannedClass> getAllScannedClasses() {
        return this.scannedClasses;
    }
}
