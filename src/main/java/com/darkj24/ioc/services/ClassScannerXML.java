package com.darkj24.ioc.services;

import com.darkj24.ioc.annotations.Autowired;
import com.darkj24.ioc.enums.AutowiringMode;
import com.darkj24.ioc.enums.Scope;
import com.darkj24.ioc.models.ScannedClass;
import com.darkj24.ioc.models.ScannedClassXML;
import com.xml.XmlParser;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static com.darkj24.ioc.models.Constants.*;

public class ClassScannerXML implements ClassScanner{

    private String filePath;
    private XmlParser xmlFile;

    public ClassScannerXML(String filePath) {
        this.filePath = filePath;
        this.xmlFile = new XmlParser(this.filePath);
    }

    public Set<ScannedClass> scanClasses() {
        Set<Class<?>> locatedClasses = new HashSet<>();;
        ArrayList<String> classes = xmlFile.getAllCls();
        try {
            for (String s : classes) {
                locatedClasses.add(Class.forName(s));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return scanClasses(locatedClasses);
    }

    public Set<ScannedClass> scanClasses(Set<Class<?>> locatedClasses) {
        // ojo, el locatedClasses no se esta usando por el momento
        Set<ScannedClassXML> scannedClassesXML = new HashSet<ScannedClassXML>();
        List<String> beanIds = xmlFile.getAllBeanID();

        for(String bean:beanIds){
            List<String> properties = xmlFile.getAllPropertiesFromBeanId(bean);
            try {
                Class cls = Class.forName(xmlFile.getCls(bean));
                ScannedClassXML scannedClass = new ScannedClassXML.ScannedClassBuilder(cls)
                        .addConstructor(findConstructor(cls, xmlFile.getConstructorArg(bean)))
                        .addMethods(getMethods(cls))
                        .addDependantClasses(new ArrayList<>())
                        .addDependencyClasses(new ArrayList<>())
                        .addLazyInit(xmlFile.getLazyInit(bean))
                        .addInitMethod(findMethod(cls, xmlFile.getInitMethod(bean)))
                        .addDestroyMethod(findMethod(cls, xmlFile.getDestroyMethod(bean)))
                        .addScope(findScope(xmlFile.getScope(bean)))
                        .addAutowiringMode(findAutowiringMode(xmlFile.getAutowiringMode(bean)))
                        .build();
                scannedClassesXML.add(scannedClass);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
        return scannedClassesXML.stream().collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Scope findScope(String xmlScope){
        if(xmlScope.equals(ATTR_PROTOTYPE)){
            return Scope.PROTOTYPE;
        }else{
            return Scope.SINGLETON;
        }
    }

    private AutowiringMode findAutowiringMode(String xmlMode){
        if(xmlMode.equals(ATTR_BY_NAME)){
            return AutowiringMode.BY_TYPE;
        }else if(xmlMode.equals(ATTR_BY_TYPE)){
            return AutowiringMode.BY_NAME;
        }else {
            return AutowiringMode.NO;
        }
    }

    private Method findMethod(Class<?> cls, String methodName){
        try {
            if(!methodName.isEmpty() && methodName != null) {
                return cls.getMethod(methodName, null);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Method[] getMethods(Class<?> cls){
        return cls.getDeclaredMethods();
    }

    private Constructor<?> findConstructor (Class<?> cls, String constructorArg){
        for (Constructor ctr: cls.getDeclaredConstructors()){
            if(ctr.getParameterCount()==Integer.parseInt(constructorArg)){
                return ctr;
            }
        }
        return null;
    }
}
