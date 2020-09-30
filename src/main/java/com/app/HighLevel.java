package com.app;

import com.darkj24.ioc.annotations.Provider;

@Provider
public class HighLevel {

    private LowLevel lowLevel;

    private LowLevel2 lowLevel2;

    public HighLevel () {
    }

    public LowLevel getLowLevel() {
        return lowLevel;
    }

    public void setLowLevel(LowLevel lowLevel) {
        this.lowLevel = lowLevel;
    }

    public LowLevel2 getLowLevel2() {
        return lowLevel2;
    }

    public void setLowLevel2(LowLevel2 lowLevel2) {
        this.lowLevel2 = lowLevel2;
    }

    public String print(){
        return this.lowLevel.getHello() + " " + this.lowLevel2.getWorld();
    }
}
