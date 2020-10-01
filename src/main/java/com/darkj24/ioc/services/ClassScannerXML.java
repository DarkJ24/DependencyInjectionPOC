package com.darkj24.ioc.services;

import com.darkj24.ioc.models.ScannedClass;
import com.darkj24.ioc.models.ScannedClassAnnotation;
import com.xml.XmlParser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ClassScannerXML implements ClassScanner{

    private String filePath;
    private XmlParser xmlFile;

    public ClassScannerXML(String filePath) {
        this.filePath = filePath;
        this.xmlFile = new XmlParser(filePath);
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

    @Override
    public Set<ScannedClass> scanClasses(Set<Class<?>> locatedClasses) {

        final Set<ScannedClassAnnotation> scannedClassXML = new HashSet<>();

        return null;
    }
}
