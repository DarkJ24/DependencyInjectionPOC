package com.darkj24.ioc.services;

import com.darkj24.ioc.annotations.*;
import com.darkj24.ioc.enums.AutowiringMode;
import com.darkj24.ioc.enums.Scope;
import com.darkj24.ioc.models.ScannedClass;
import com.darkj24.ioc.models.ScannedClassAnnotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassScannerAnnotation implements ClassScanner {

    public ClassScannerAnnotation() {
    }

    @Override
    public Set<ScannedClass> scanClasses(Set<Class<?>> locatedClasses) {
        final Set<ScannedClassAnnotation> scannedClassAnnotations = new HashSet<>();

        for (Class<?> cls : locatedClasses) {
            if (cls.isInterface()) {
                continue;
            }

            boolean isProvider = false;
            Scope scope = Scope.SINGLETON;
            boolean isLazyInit = false;
            AutowiringMode autowiringMode = AutowiringMode.NO;

            for (Annotation annotation : cls.getAnnotations()) {
                if (annotation.annotationType() == Provider.class) {
                    Provider pAnnotation = (Provider) annotation;
                    autowiringMode = pAnnotation.autowire();
                    isProvider = true;
                }
                if (annotation.annotationType() == Singleton.class) {
                    scope = Scope.SINGLETON;
                }
                if (annotation.annotationType() == Prototype.class) {
                    scope = Scope.PROTOTYPE;
                }
                if (annotation.annotationType() == Lazy.class) {
                    isLazyInit = true;
                }
            }

            if (isProvider) {
                ScannedClassAnnotation scannedClass = new ScannedClassAnnotation(
                        cls,
                        this.findSuitableConstructor(cls),
                        this.findInitMethod(cls), this.findDestroyMethod(cls), scope, autowiringMode, isLazyInit,
                        this.findBeans(cls), this.findRequiredMethods(cls)

                );

                scannedClassAnnotations.add(scannedClass);
            }
        }

        return scannedClassAnnotations.stream()
                .sorted(new ScannedClassComparator())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Constructor<?> findSuitableConstructor(Class<?> cls) {
        for (Constructor<?> ctr : cls.getDeclaredConstructors()) {
            if (ctr.isAnnotationPresent(Autowired.class)) {
                ctr.setAccessible(true);
                return ctr;
            }
        }

        return cls.getConstructors()[0];
    }

    private Method[] findBeans(Class<?> cls) {
        final Set<Method> beanMethods = new HashSet<>();

        for (Method method : cls.getDeclaredMethods()) {
            if (method.getParameterCount() != 0 || method.getReturnType() == void.class || method.getReturnType() == Void.class) {
                continue;
            }

            if (method.isAnnotationPresent(Bean.class)) {
                method.setAccessible(true);
                //beanMethods.add(new ScannedMethod.ScannedMethodBuilder(method).setRequired(method.isAnnotationPresent(Required.class)).setBean(true).build());
                beanMethods.add(method);
                break;
            }
        }

        return beanMethods.toArray(new Method[0]);
    }

    private Method[] findRequiredMethods(Class<?> cls) {
        final Set<Method> requiredMethods = new HashSet<>();

        for (Method method : cls.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Required.class)) {
                requiredMethods.add(method);
            }
        }

        return requiredMethods.toArray(new Method[0]);
    }

    private Method findInitMethod(Class<?> cls) {
        return findAnnotation(cls, Init.class);
    }

    private Method findDestroyMethod(Class<?> cls) {
        return findAnnotation(cls, Destroy.class);
    }

    private Method findAnnotation(Class<?> cls, Class annotation) {
        for (Method method : cls.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                method.setAccessible(true);
                return method;
            }
        }

        return null;
    }

    private class ScannedClassComparator implements Comparator<ScannedClassAnnotation> {
        @Override
        public int compare(ScannedClassAnnotation class1, ScannedClassAnnotation class2) {
            if (class1.getConstructor() == null || class2.getConstructor() == null) {
                return 0;
            }

            return Integer.compare(
                    class1.getConstructor().getParameterCount(),
                    class2.getConstructor().getParameterCount()
            );
        }
    }

}
