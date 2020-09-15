package com.darkj24.ioc.config;

public abstract class SubConfiguration {

    private final MainConfiguration parentConfiguration;

    protected SubConfiguration(MainConfiguration parentConfiguration) {
        this.parentConfiguration = parentConfiguration;
    }

    public MainConfiguration and() {
        return this.parentConfiguration;
    }

}
