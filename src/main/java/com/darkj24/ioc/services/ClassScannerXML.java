package com.darkj24.ioc.services;

import com.darkj24.ioc.annotations.Autowired;
import com.darkj24.ioc.enums.AutowiringMode;
import com.darkj24.ioc.enums.Scope;
import com.darkj24.ioc.models.ScannedClass;
import com.darkj24.ioc.models.ScannedClassXML;
import com.xml.DataStructureProperties;
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
        Set<ScannedClassXML> scannedClassesXML = new HashSet<ScannedClassXML>();
        List<String> beanIds = xmlFile.getAllBeanID();

        for(String bean:beanIds){
            try {
                Class cls = Class.forName(xmlFile.getCls(bean));

                ScannedClassXML scannedClass = new ScannedClassXML.ScannedClassBuilder(cls)
                        .addConstructor(findConstructor(cls,bean))
                        .addMethods(new Method[0]) //No necesario en configuración XML
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
        //Add Dependant and Dependency Classes
        try {
            addDependantClasses(scannedClassesXML, beanIds);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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

    private Constructor<?> findConstructor (Class<?> cls, String bean) throws ClassNotFoundException {
        for (Constructor ctr: cls.getDeclaredConstructors()){
            Map<String, DataStructureProperties> map = xmlFile.getConstructorArgs(bean);
            Class[] parameterTypes = ctr.getParameterTypes();
            if(!map.isEmpty() && ctr.getParameterCount() == map.size()){
                int count = 0;
                for(Map.Entry<String, DataStructureProperties> entry: map.entrySet()){
                    String propertyBean = map.get(entry.getKey()).getRef();
                    if(parameterTypes[count] == Class.forName(xmlFile.getCls(propertyBean))){
                        count++;
                        continue;
                    }else{
                        return cls.getConstructors()[0];
                    }
                }
                return ctr;
            }
        }
        return cls.getConstructors()[0];
    }

    private void addDependantClasses(Set<ScannedClassXML> scannedClassesXML,List<String> beanIds) throws ClassNotFoundException {
        for(ScannedClassXML primaryCls: scannedClassesXML){
            for(ScannedClassXML secondaryCls: scannedClassesXML){
                if(!primaryCls.equals(secondaryCls)){
                    List<String> properties = null;
                    for(String bean: beanIds){
                        if(xmlFile.getCls(bean).equals(primaryCls.getType().toString().
                                replace("class ",""))){
                            properties = xmlFile.getAllPropertiesFromBeanId(bean);
                        }
                    }
                    for (String propertyRef: properties) {
                        String propertyCls = xmlFile.getCls(propertyRef);
                        if(propertyCls.equals(secondaryCls.getType().toString().replace("class ",""))){
                            if(!primaryCls.getDependencyServices().contains(secondaryCls)){
                                primaryCls.addDependencyServices(secondaryCls);
                            }
                            if(!secondaryCls.getDependantServices().contains(primaryCls)){
                                secondaryCls.addDependantService(primaryCls);
                            }
                        }
                    }
                }
            }
        }
    }
}
