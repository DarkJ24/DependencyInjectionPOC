package com.app;

import com.darkj24.ioc.annotations.Autowired;

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
