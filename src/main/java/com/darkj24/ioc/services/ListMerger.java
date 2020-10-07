package com.darkj24.ioc.services;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ListMerger<T> {

    public ListMerger() {
    }

    public List<T> mergeLists (List<T> a, List<T> b) {
        List<T> finalList = new ArrayList<T>();
        for (T bean1 : a){
            boolean inserted = false;
            for (T bean2 : b){
                if (!inserted && bean1.toString().equals(bean2.toString())){
                    inserted = true;
                    finalList.add(bean1);
                }
            }
            if (!inserted){
                finalList.add(bean1);
            }
        }
        for (T bean1 : b){
            boolean inserted = false;
            for (T bean2 : a){
                if (!inserted && bean1.toString().equals(bean2.toString())){
                    inserted = true;
                }
            }
            if (!inserted){
                finalList.add(bean1);
            }
        }
        return finalList;
    }

    public List<T> mergeLists (T[] a, T[] b) {
        List<T> finalList = new ArrayList<T>();
        for (T bean1 : a){
            boolean inserted = false;
            for (T bean2 : b){
                if (!inserted && bean1.toString().equals(bean2.toString())){
                    inserted = true;
                    finalList.add(bean1);
                }
            }
            if (!inserted){
                finalList.add(bean1);
            }
        }
        for (T bean1 : b){
            boolean inserted = false;
            for (T bean2 : a){
                if (!inserted && bean1.toString().equals(bean2.toString())){
                    inserted = true;
                }
            }
            if (!inserted){
                finalList.add(bean1);
            }
        }
        return finalList;
    }
}
