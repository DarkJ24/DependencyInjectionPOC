package com.darkj24.ioc.services;

import com.darkj24.ioc.models.ScannedClass;

import java.util.HashSet;
import java.util.Set;

public class ClassScannerMerger {

    public ClassScannerMerger() {
    }

    public Set<ScannedClass> mergeClasses(Set<ScannedClass> classes1, Set<ScannedClass> classes2) {
        Set<ScannedClass> classes = new HashSet<>();
        for (ScannedClass cls : classes1){
            boolean hasInserted = false;
            for (ScannedClass cls2 : classes2){
                if (cls.toString().equals(cls2.toString())){
                    classes.add(cls.merge(cls2));
                    hasInserted = true;
                }
            }
            if (!hasInserted){
                classes.add(cls);
            }
        }
        for (ScannedClass cls2 : classes2) {
            boolean found = false;
            for (ScannedClass cls : classes1) {
                if (cls.toString().equals(cls2.toString())){
                    found = true;
                }
            }
            if (!found){
                classes.add(cls2);
            }
        }
        return classes;
    }
}
