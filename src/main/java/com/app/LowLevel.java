package com.app;

import com.darkj24.ioc.annotations.Autowired;
import com.darkj24.ioc.annotations.Provider;

@Provider
public class LowLevel {

    private String hello;

    @Autowired
    public LowLevel() {
        this.hello = "Hello";
    }

    public String getHello() {
        return hello;
    }

    public void setHello(String hello) {
        this.hello = hello;
    }
}
