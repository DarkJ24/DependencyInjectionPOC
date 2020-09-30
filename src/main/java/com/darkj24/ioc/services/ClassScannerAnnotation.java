package com.darkj24.ioc.services;

import com.darkj24.ioc.annotations.*;
import com.darkj24.ioc.config.AnnotationsConfiguration;
import com.darkj24.ioc.enums.AutowiringMode;
import com.darkj24.ioc.enums.Scope;
import com.darkj24.ioc.models.Constants;
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

    private final AnnotationsConfiguration configuration;

    public ClassScannerAnnotation(AnnotationsConfiguration configuration) {
        this.configuration = configuration;
        this.init();
    }

    @Override
    public Set<ScannedClass> scanClasses(Set<Class<?>> locatedClasses) {
        final Set<ScannedClassAnnotation> scannedClassAnnotations = new HashSet<>();
        final Set<Class<? extends Annotation>> providerAnnotations = configuration.getProviderAnnotations();

        for (Class<?> cls : locatedClasses) {
            if (cls.isInterface()) {
                continue;
            }

            Annotation providerAnnotation = null;
            Scope scope = Scope.SINGLETON;
            boolean isLazyInit = false;
            AutowiringMode autowiringMode = AutowiringMode.NO;

            for (Annotation annotation : cls.getAnnotations()) {
                if (providerAnnotations.contains(annotation.annotationType())) {
                    providerAnnotation = annotation;
                    if (annotation instanceof  Provider) {
                        Provider pAnnotation = (Provider) annotation;
                        autowiringMode = pAnnotation.autowire();
                    }
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

            if (providerAnnotation != null) {
                ScannedClassAnnotation scannedClass = new ScannedClassAnnotation(
                        cls,
                        providerAnnotation,
                        this.findSuitableConstructor(cls),
                        this.findInitMethod(cls), this.findDestroyMethod(cls), scope, autowiringMode, isLazyInit, this.findBeans(cls)

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
        final Set<Class<? extends Annotation>> beanAnnotations = this.configuration.getBeanAnnotations();
        final Set<Method> beanMethods = new HashSet<>();

        for (Method method : cls.getDeclaredMethods()) {
            if (method.getParameterCount() != 0 || method.getReturnType() == void.class || method.getReturnType() == Void.class) {
                continue;
            }

            for (Class<? extends Annotation> beanAnnotation : beanAnnotations) {
                if (method.isAnnotationPresent(beanAnnotation)) {
                    method.setAccessible(true);
                    beanMethods.add(method);

                    break;
                }
            }
        }

        return beanMethods.toArray(new Method[0]);
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

    private void init() {
        this.configuration.getBeanAnnotations().add(Bean.class);
        this.configuration.getProviderAnnotations().add(Provider.class);
    }

    private class ScannedClassComparator implements Comparator<ScannedClassAnnotation> {
        @Override
        public int compare(ScannedClassAnnotation class1, ScannedClassAnnotation class2) {
            if (class1.getTargetConstructor() == null || class2.getTargetConstructor() == null) {
                return 0;
            }

            return Integer.compare(
                    class1.getTargetConstructor().getParameterCount(),
                    class2.getTargetConstructor().getParameterCount()
            );
        }
    }

}
