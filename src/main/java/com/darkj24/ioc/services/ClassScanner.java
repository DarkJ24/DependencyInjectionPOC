package com.darkj24.ioc.services;

import com.darkj24.ioc.models.ScannedClass;

import java.util.Set;

public interface ClassScanner {

    Set<ScannedClass> scanClasses(Set<Class<?>> locatedClasses);

}
