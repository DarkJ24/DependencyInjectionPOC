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
        System.out.println("****CONFIGURACIÓN ANNOTATION****");
        for (ScannedClass s : scannedClassAnnotations) {
            System.out.println(s.getType());
            System.out.println(s.getScope());
            System.out.println(s.getConstructor());
            System.out.println(s.getInitMethod());
            System.out.println(s.getDestroyMethod());
            System.out.println(s.getAutowiringMode());
            System.out.println(Arrays.stream(s.getBeans()).map(Method::getName).collect(Collectors.toList()));
        }

        Set<ScannedClass> scannedClassXML = new ClassScannerXML("src/main/resources/context.xml")
                .scanClasses();
        System.out.println("****CONFIGURACIÓN XML****");
        for (ScannedClass s2 : scannedClassXML) {
            System.out.println(s2.getType());
            System.out.println(s2.getScope());
            System.out.println(s2.getConstructor());
            System.out.println(s2.getInitMethod());
            System.out.println(s2.getDestroyMethod());
            System.out.println(s2.getConstructor());
            System.out.println(s2.getAutowiringMode());
            System.out.println(s2.getBeans());
        }

    }

    public static void main(String[] args) {
        run(MainApplication.class);
    }

}
