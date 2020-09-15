package com.darkj24.ioc.services;

import com.darkj24.ioc.models.Directory;

public interface DirectoryResolver {
    Directory resolveDirectory(Class<?> startupClass);
}
