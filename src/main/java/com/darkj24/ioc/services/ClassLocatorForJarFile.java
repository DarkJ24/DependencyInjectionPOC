package com.darkj24.ioc.services;

import com.darkj24.ioc.models.Constants;
import com.darkj24.ioc.exceptions.ClassLocatorException;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassLocatorForJarFile implements ClassLocator {

    @Override
    public Set<Class<?>> locateClasses(String directory) throws ClassLocatorException {
        final Set<Class<?>> locatedClasses = new HashSet<>();

        try {
            JarFile jarFile = new JarFile(new File(directory));

            Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();

                if (!jarEntry.getName().endsWith(Constants.JAVA_BINARY_EXTENSION)) {
                    continue;
                }

                final String className = jarEntry.getName().replace(Constants.JAVA_BINARY_EXTENSION, "")
                        .replaceAll("\\\\", ".")
                        .replaceAll("/", ".");

                locatedClasses.add(Class.forName(className));
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new ClassLocatorException(e.getMessage(), e);
        }

        return locatedClasses;
    }
}
