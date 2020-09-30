package com.darkj24.ioc;

import com.app.MainApplication;
import com.darkj24.ioc.config.MainConfiguration;
import com.darkj24.ioc.enums.DirectoryType;
import com.darkj24.ioc.models.Directory;
import com.darkj24.ioc.models.ScannedClass;
import com.darkj24.ioc.models.ScannedClassAnnotation;
import com.darkj24.ioc.services.*;

import java.util.Set;

public class DependencyInjector {


    public static void run(Class<?> startupClass) {
        run(startupClass, new MainConfiguration());
    }

    public static void run(Class<?> startupClass, MainConfiguration configuration) {
        ClassScanner classScanner = new ClassScannerAnnotation(configuration.annotations());
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
            System.out.println(s.getTargetConstructor());
            System.out.println(s.getInitMethod());
            System.out.println(s.getDestroyMethod());
            System.out.println(s.getTargetConstructor());
            System.out.println(s.getAutowiringMode());
            System.out.println(s.getBeans());
        }
    }

    public static void main(String[] args) {
        run(MainApplication.class);
    }

}
