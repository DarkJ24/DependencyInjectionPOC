package com.darkj24.ioc.services;

import com.darkj24.ioc.exceptions.ClassLocatorException;

import java.util.Set;

public interface ClassLocator {

    Set<Class<?>> locateClasses(String directory) throws ClassLocatorException;

}
