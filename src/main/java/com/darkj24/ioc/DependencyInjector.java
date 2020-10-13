package com.darkj24.ioc;

import com.app.*;
import com.darkj24.ioc.enums.DirectoryType;
import com.darkj24.ioc.models.Directory;
import com.darkj24.ioc.models.ScannedClass;
import com.darkj24.ioc.services.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class DependencyInjector {

    public static final Container container;

    static {
        container = new ContainerImpl();
    }

    public static Container run(String logInfo, Class<?> startupClass) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return run(startupClass, null, logInfo);
    }

    public static Container run(String contextPath, String logInfo) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return run(null, contextPath, logInfo);
    }

    public static Container run(Class<?> startupClass) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return run(startupClass, null, "normal");
    }

    public static Container run(String contextPath) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return run(null, contextPath, "normal");
    }

    public static Container run(Class<?> startupClass, String contextPath) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return run(startupClass, contextPath, "normal");
    }

    public static Container run(Class<?> startupClass, String contextPath, String logInfo) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Set<ScannedClass> finalClasses = new HashSet<>();
        Set<ScannedClass> scannedClassAnnotations = new HashSet<>();
        Set<ScannedClass> scannedClassXML = new HashSet<>();
        if (startupClass != null) {
            ClassScanner classScanner = new ClassScannerAnnotation();
            Directory directory = new DirectoryResolverImpl().resolveDirectory(startupClass);
            ClassLocator classLocator = new ClassLocatorForDirectory();
            if (directory.getType() == DirectoryType.JAR_FILE) {
                classLocator = new ClassLocatorForJarFile();
            }

            Set<Class<?>> locatedClasses = classLocator.locateClasses(directory.getName());
            scannedClassAnnotations = classScanner.scanClasses(locatedClasses);

            if (logInfo.equals("verbose")) {
                System.out.println("****CONFIGURACIÓN ANNOTATION****");
                printScannedClassesDetail(scannedClassAnnotations);
            }

            if (contextPath == null){
                finalClasses = scannedClassAnnotations;
            }
        }

        if (contextPath != null) {
            scannedClassXML = new ClassScannerXML(contextPath)
                    .scanClasses();

            if (logInfo.equals("verbose")) {
                System.out.println("****CONFIGURACIÓN XML****");
                printScannedClassesDetail(scannedClassXML);
            }

            if (startupClass == null){
                finalClasses = scannedClassXML;
            }
        }

        if (contextPath != null && startupClass != null) {
            ClassScannerMerger classMerger = new ClassScannerMerger();
            finalClasses = classMerger.mergeClasses(scannedClassAnnotations, scannedClassXML);

        }

        if (logInfo.equals("verbose")) {
            System.out.println("****CONFIGURACIÓN GLOBAL****");
            printScannedClassesDetail(finalClasses);
        }

        container.init(finalClasses);

        return container;
    }

    private static void testConfiguration(Container container) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

        System.out.println("\n\n****CONTAINER****\n\n");
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
        Container c = run(MainApplication.class, "src/main/resources/context.xml", "verbose");
        testConfiguration(c);
    }

}
