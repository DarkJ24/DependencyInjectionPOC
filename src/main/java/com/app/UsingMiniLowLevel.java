package com.app;

import com.darkj24.ioc.annotations.Provider;
import com.darkj24.ioc.annotations.Qualifier;

@Provider
public class UsingMiniLowLevel {

    public UsingMiniLowLevel(){
    }

    @Qualifier("miniLevel1")
    public void setMiniLowLevel1(MiniLowLevel miniLowLevel1){
        System.out.println(miniLowLevel1);
    }

    @Qualifier("miniLevel2")
    public void setMiniLowLevel2(MiniLowLevel miniLowLevel2){
        System.out.println(miniLowLevel2);
    }
}
