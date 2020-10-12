package com.darkj24.ioc;

import com.app.*;
import com.darkj24.ioc.annotations.Lazy;
import com.darkj24.ioc.enums.DirectoryType;
import com.darkj24.ioc.models.Directory;
import com.darkj24.ioc.models.InstantiatedClasses;
import com.darkj24.ioc.models.ScannedClass;
import com.darkj24.ioc.services.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class DependencyInjector {

    public static void run(Class<?> startupClass) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
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
        printScannedClassesDetail(scannedClassAnnotations);

        Set<ScannedClass> scannedClassXML = new ClassScannerXML("src/main/resources/context.xml")
                .scanClasses();
        System.out.println("****CONFIGURACIÓN XML****");
        printScannedClassesDetail(scannedClassXML);

        ClassScannerMerger classMerger = new ClassScannerMerger();
        Set<ScannedClass> finalClasses = classMerger.mergeClasses(scannedClassAnnotations, scannedClassXML);
        System.out.println("****CONFIGURACIÓN GLOBAL****");
        printScannedClassesDetail(finalClasses);

        System.out.println("\n\n****CONTAINER****\n\n");
        Container container = new ContainerImpl();
        container.init(locatedClasses, finalClasses);
        //System.out.println(container.getInstance(HighLevel.class, HighLevel.class.getName()));
        System.out.println(container.getInstance(LowLevel.class, LowLevel.class.getName()));
        System.out.println(container.getInstance(MiniLowLevel.class, "miniLevel1"));
        System.out.println(container.getInstance(MiniLowLevel.class, "miniLevel2"));
        System.out.println("\n\n****LAZY****\n\n");
        LazyClass lz = container.getInstance(LazyClass.class);
        System.out.println(lz);
        System.out.println(container.getInstance(LazyClass.class));
        container.destroyInstance(lz);
        System.out.println(container.getInstance(LazyClass.class));
        container.destroy();
    }

    private static void printScannedClassesDetail(Set<ScannedClass> classes){
        for (ScannedClass s2 : classes) {
            System.out.println(s2.getType());
            System.out.println(s2.getScope());
            System.out.println(s2.getConstructor());
            System.out.println(s2.getInitMethod());
            System.out.println(s2.getDestroyMethod());
            System.out.println(s2.getAutowiringMode());
            System.out.println("Dependant Services: "+s2.getDependantServices());
            System.out.println("Dependency Services: "+s2.getDependencyServices());
            //System.out.println(s2.getBeans());
            System.out.println(Arrays.stream(s2.getBeans()).map(Method::getName).collect(Collectors.toList()));
        }
    }


    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        run(MainApplication.class);
    }

}
