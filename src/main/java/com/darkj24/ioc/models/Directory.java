package com.darkj24.ioc.models;

import com.darkj24.ioc.annotations.Bean;
import com.darkj24.ioc.annotations.Provider;
import com.darkj24.ioc.enums.DirectoryType;

public class Directory {
    private final String name;

    private final DirectoryType type;

    public Directory(String name, DirectoryType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public DirectoryType getType() {
        return this.type;
    }
}
