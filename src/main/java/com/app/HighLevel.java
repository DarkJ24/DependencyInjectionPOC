package com.app;

import com.darkj24.ioc.annotations.*;
import com.darkj24.ioc.enums.AutowiringMode;

@Provider(autowire = AutowiringMode.BY_NAME)
@Prototype
@Lazy
public class HighLevel {

    private LowLevel lowLevel;

    private LowLevel2 lowLevel2;

    public HighLevel () {
    }

    @Init
    public void init(){
        System.out.println("Init HighLevel");
    }

    @Destroy
    public void destroy(){
        System.out.println("Destroy HighLevel");
    }

    public LowLevel getLowLevel() {
        return lowLevel;
    }

    @Required
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
