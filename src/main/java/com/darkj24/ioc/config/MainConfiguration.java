package com.darkj24.ioc.config;

public class MainConfiguration {

    private final AnnotationsConfiguration annotations;

    public MainConfiguration() {
        this.annotations = new AnnotationsConfiguration(this);
    }

    public AnnotationsConfiguration annotations() {
        return this.annotations;
    }

    public MainConfiguration build() {
        return this;
    }
}
