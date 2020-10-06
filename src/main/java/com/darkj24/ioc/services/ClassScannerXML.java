package com.darkj24.ioc.services;

import com.darkj24.ioc.models.ScannedClass;
import com.darkj24.ioc.models.ScannedClassXML;
import com.xml.XmlParser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClassScannerXML implements ClassScanner{

    private String filePath;
    private XmlParser xmlFile;

    public ClassScannerXML(String filePath) {
        this.filePath = filePath;
        this.xmlFile = new XmlParser(filePath);
    }

    public Set<ScannedClassXML> scanClasses() {
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

    @Override
    public Set<ScannedClassXML> scanClasses(Set<Class<?>> locatedClasses) {
        // ojo, el locatedClasses no se esta usando por el momento
        Set<ScannedClassXML> scannedClassXML = new HashSet<>();
        List<String> beanIds = xmlFile.getAllBeanID();

        for(String bean:beanIds){
            List<String> properties = xmlFile.getAllPropertiesFromBeanId(bean);
            try {
                ScannedClassXML scannedClass = new ScannedClassXML.ScannedClassBuilder(Class.forName(xmlFile.getCls(bean)))
                        .addConstructor()
                        .addMethods()
                        .addMDependantClasses()
                        .addMDependencyClasses()
                        .addInitMethod()
                        .addDestroyMethod()
                        .addScope()
                        .addSAutowiringMode()
                        .build();

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }

        return scannedClassXML;
    }
}
