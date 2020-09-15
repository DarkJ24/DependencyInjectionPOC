package com.darkj24.ioc;

import com.darkj24.ioc.config.MainConfiguration;
import com.darkj24.ioc.enums.DirectoryType;
import com.darkj24.ioc.models.Directory;
import com.darkj24.ioc.models.ScannedClass;
import com.darkj24.ioc.services.*;

import java.util.Set;

public class DependencyInjector {


    public static void run(Class<?> startupClass) {
        run(startupClass, new MainConfiguration());
    }

    public static void run(Class<?> startupClass, MainConfiguration configuration) {
        ClassScanner classScanner = new ClassScannerImpl(configuration.annotations());
        Directory directory = new DirectoryResolverImpl().resolveDirectory(startupClass);
        ClassLocator classLocator = new ClassLocatorForDirectory();
        if (directory.getType() == DirectoryType.JAR_FILE) {
            classLocator = new ClassLocatorForJarFile();
        }

        Set<Class<?>> locatedClasses = classLocator.locateClasses(directory.getName());
        Set<ScannedClass> scannedClasses = classScanner.scanClasses(locatedClasses);
        System.out.println(scannedClasses);
    }

    public static void main(String[] args) {
        run(DependencyInjector.class);
    }

}
