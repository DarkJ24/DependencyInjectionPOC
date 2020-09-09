package com.darkj24.ioc.services;

import com.darkj24.ioc.enums.DirectoryType;
import com.darkj24.ioc.models.Constants;
import com.darkj24.ioc.models.Directory;

import java.io.File;

public class DirectoryResolverImpl implements DirectoryResolver {

    @Override
    public Directory resolveDirectory(Class<?> startupClass) {
        final String directory = this.getDirectory(startupClass);

        return new Directory(directory, this.getType(directory));
    }

    private String getDirectory(Class<?> cls) {
        return cls.getProtectionDomain().getCodeSource().getLocation().getFile();
    }

    private DirectoryType getType(String directory) {
        File file = new File(directory);

        if (!file.isDirectory() && directory.endsWith(Constants.JAR_FILE_EXTENSION)) {
            return DirectoryType.JAR_FILE;
        }

        return DirectoryType.DIRECTORY;
    }
}
