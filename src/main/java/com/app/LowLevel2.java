package com.app;

import com.darkj24.ioc.annotations.Autowired;
import com.darkj24.ioc.annotations.Provider;

@Provider
public class LowLevel2 {

    private String world;

    @Autowired
    public LowLevel2() {
        this.world = "World!";
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }
}
