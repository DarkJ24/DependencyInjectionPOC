package com.darkj24.ioc;

import com.app.MainApplication;
import com.darkj24.ioc.enums.DirectoryType;
import com.darkj24.ioc.models.Directory;
import com.darkj24.ioc.models.ScannedClass;
import com.darkj24.ioc.services.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class DependencyInjector {

    public static void run(Class<?> startupClass) {
        ClassScanner classScanner = new ClassScannerAnnotation();
        Directory directory = new DirectoryResolverImpl().resolveDirectory(startupClass);
        ClassLocator classLocator = new ClassLocatorForDirectory();
        if (directory.getType() == DirectoryType.JAR_FILE) {
            classLocator = new ClassLocatorForJarFile();
        }

        Set<Class<?>> locatedClasses = classLocator.locateClasses(directory.getName());
        Set<ScannedClass> scannedClassAnnotations = classScanner.scanClasses(locatedClasses);
        //System.out.println(scannedClassAnnotations);
        for (ScannedClass s : scannedClassAnnotations) {
            System.out.println(s.getType());
            System.out.println(s.getScope());
            System.out.println(s.getConstructor());
            System.out.println(s.getInitMethod());
            System.out.println(s.getDestroyMethod());
            System.out.println(s.getAutowiringMode());
            System.out.println(Arrays.stream(s.getBeans()).map(Method::getName).collect(Collectors.toList()));
        }
    }

    public static void main(String[] args) {
        run(MainApplication.class);
    }

}
