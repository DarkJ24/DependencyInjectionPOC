package com.darkj24.ioc.services;

import com.darkj24.ioc.enums.Scope;
import com.darkj24.ioc.models.ScannedClass;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import static com.darkj24.ioc.models.InstantiatedClasses.instantiatedClasses;

public class InstanceCreator {

    public static Object createSingletonInstance(ScannedClass cls){

        try {
            /*Object object = cls.getType().getDeclaredConstructor().newInstance();
            Object object2 = cls.getType().getDeclaredConstructor().newInstance();
            System.out.println(object.hashCode());
            System.out.println(object2.hashCode());
            Object object3 = object;
            System.out.println(object3.getClass().toString());
            System.out.println(object.equals(object3));*/
            Object object = null;
            if (cls.getScope() == Scope.SINGLETON){
                if(!instantiatedClasses.containsKey(cls.getType())){
                    object = cls.getType().getDeclaredConstructor().newInstance();
                    instantiatedClasses.put(cls.getType(), new ArrayList<Object>());
                    instantiatedClasses.get(cls.getType()).add(object);
                } else{
                    object = instantiatedClasses.get(cls.getType()).get(0);
                }
                return object;
            } else if(cls.getScope() == Scope.PROTOTYPE){
                if(!instantiatedClasses.containsKey(cls.getType())){
                    object = cls.getType().getDeclaredConstructor().newInstance();
                    instantiatedClasses.put(cls.getType(), new ArrayList<Object>());
                    instantiatedClasses.get(cls.getType()).add(object);
                } else{
                    object = cls.getType().getDeclaredConstructor().newInstance();
                    instantiatedClasses.get(cls.getType()).add(object);
                }
                return object;
            }


        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

return null;

    }
}
