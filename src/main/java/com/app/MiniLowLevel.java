package com.app;

import com.darkj24.ioc.annotations.Autowired;
import com.darkj24.ioc.annotations.Bean;
import com.darkj24.ioc.annotations.Prototype;
import com.darkj24.ioc.enums.AutowiringMode;

@Bean(autowire = AutowiringMode.BY_NAME)
@Prototype
public class MiniLowLevel {

    private String something;

    @Autowired
    public MiniLowLevel() {
        this.something = "Something?";
    }

    public String getSomething() {
        return something;
    }

    public void setSomething(String something) {
        this.something = something;
    }
}
