package com.darkj24.ioc.services;

import com.darkj24.ioc.exceptions.ProviderInstantiationException;
import com.darkj24.ioc.models.ScannedClassAnnotation;

import java.util.List;
import java.util.Set;

public class ClassInstantiatorImpl implements ClassInstantiator {

    @Override
    public List<ScannedClassAnnotation> instantiateProvidersAndBeans(Set<ScannedClassAnnotation> classes) throws ProviderInstantiationException {
        return null;
    }
}
