package com.app;

import com.darkj24.ioc.annotations.Autowired;
import com.darkj24.ioc.annotations.Bean;
import com.darkj24.ioc.annotations.Provider;
import com.darkj24.ioc.annotations.Qualifier;

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

    @Bean
    @Qualifier("miniLevel1")
    public MiniLowLevel getMiniLowLevel() {
        return new MiniLowLevel();
    }

    @Bean(name = "miniLevel2")
    public MiniLowLevel getMiniLowLevel2() {
        return new MiniLowLevel();
    }
}
