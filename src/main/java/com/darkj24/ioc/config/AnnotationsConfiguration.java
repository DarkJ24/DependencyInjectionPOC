package com.darkj24.ioc.config;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AnnotationsConfiguration extends SubConfiguration {

    private final Set<Class<? extends Annotation>> providerAnnotations;

    private final Set<Class<? extends Annotation>> beanAnnotations;

    public AnnotationsConfiguration(MainConfiguration parentConfig) {
        super(parentConfig);
        this.providerAnnotations = new HashSet<>();
        this.beanAnnotations = new HashSet<>();
    }

    public AnnotationsConfiguration addProviderAnnotation(Class<? extends Annotation> annotation) {
        this.providerAnnotations.add(annotation);
        return this;
    }

    public AnnotationsConfiguration addProviderAnnotations(Class<? extends Annotation>... annotations) {
        this.providerAnnotations.addAll(Arrays.asList(annotations));
        return this;
    }

    public AnnotationsConfiguration addBeanAnnotation(Class<? extends Annotation> annotation) {
        this.beanAnnotations.add(annotation);
        return this;
    }

    public AnnotationsConfiguration addBeanAnnotations(Class<? extends Annotation>... annotations) {
        this.beanAnnotations.addAll(Arrays.asList(annotations));
        return this;
    }

    public Set<Class<? extends Annotation>> getBeanAnnotations() {
        return this.beanAnnotations;
    }

    public Set<Class<? extends Annotation>> getProviderAnnotations() {
        return this.providerAnnotations;
    }
}