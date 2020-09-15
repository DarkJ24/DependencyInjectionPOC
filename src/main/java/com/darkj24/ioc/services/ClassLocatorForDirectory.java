package com.darkj24.ioc.services;

import com.darkj24.ioc.models.Constants;
import com.darkj24.ioc.exceptions.ClassLocatorException;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ClassLocatorForDirectory implements ClassLocator {
    private static final String INVALID_DIRECTORY_MSG = "Invalid directory '%s'.";

    private final Set<Class<?>> locatedClasses;

    public ClassLocatorForDirectory() {
        this.locatedClasses = new HashSet<>();
    }

    @Override
    public Set<Class<?>> locateClasses(String directory) throws ClassLocatorException {
        this.locatedClasses.clear();
        File file = new File(directory);

        if (!file.isDirectory()) {
            throw new ClassLocatorException(String.format(INVALID_DIRECTORY_MSG, directory));
        }

        try {
            for (File innerFile : file.listFiles()) {
                this.scanDirectory(innerFile, "");
            }
        } catch (ClassNotFoundException e) {
            throw new ClassLocatorException(e.getMessage(), e);
        }

        return this.locatedClasses;
    }

    private void scanDirectory(File file, String packageName) throws ClassNotFoundException {
        if (file.isDirectory()) {
            packageName += file.getName() + ".";

            for (File innerFile : file.listFiles()) {
                this.scanDirectory(innerFile, packageName);
            }
        } else {
            if (!file.getName().endsWith(Constants.JAVA_BINARY_EXTENSION)) {
                return;
            }

            final String className = packageName + file.getName().replace(Constants.JAVA_BINARY_EXTENSION, "");

            this.locatedClasses.add(Class.forName(className));
        }
    }

}
