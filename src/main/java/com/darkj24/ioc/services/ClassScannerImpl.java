package com.darkj24.ioc.services;

import com.darkj24.ioc.annotations.Autowired;
import com.darkj24.ioc.annotations.Bean;
import com.darkj24.ioc.annotations.Provider;
import com.darkj24.ioc.config.AnnotationsConfiguration;
import com.darkj24.ioc.models.ScannedClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassScannerImpl implements ClassScanner {

    private final AnnotationsConfiguration configuration;

    public ClassScannerImpl(AnnotationsConfiguration configuration) {
        this.configuration = configuration;
        this.init();
    }

    @Override
    public Set<ScannedClass> scanClasses(Set<Class<?>> locatedClasses) {
        final Set<ScannedClass> scannedClasses = new HashSet<>();
        final Set<Class<? extends Annotation>> providerAnnotations = configuration.getProviderAnnotations();

        for (Class<?> cls : locatedClasses) {
            if (cls.isInterface()) {
                continue;
            }

            for (Annotation annotation : cls.getAnnotations()) {
                if (providerAnnotations.contains(annotation.annotationType())) {
                    ScannedClass serviceDetails = new ScannedClass(
                            cls,
                            annotation,
                            this.findSuitableConstructor(cls),
                            this.findBeans(cls)
                    );

                    scannedClasses.add(serviceDetails);

                    break;
                }
            }
        }

        return scannedClasses.stream()
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

    private void init() {
        this.configuration.getBeanAnnotations().add(Bean.class);
        this.configuration.getProviderAnnotations().add(Provider.class);
    }

    private class ScannedClassComparator implements Comparator<ScannedClass> {
        @Override
        public int compare(ScannedClass class1, ScannedClass class2) {
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
