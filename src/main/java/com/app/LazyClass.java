package com.app;

import com.darkj24.ioc.annotations.Bean;
import com.darkj24.ioc.annotations.Destroy;
import com.darkj24.ioc.annotations.Lazy;
import com.darkj24.ioc.annotations.Qualifier;
import com.darkj24.ioc.enums.AutowiringMode;

@Bean(autowire = AutowiringMode.NO)
@Lazy
public class LazyClass {

    public LazyClass(@Qualifier("miniLevel2") MiniLowLevel miniLowLevel2) {
        System.out.println("I'm Lazy...");
        System.out.println(miniLowLevel2);
    }

    public void setHighLevel(HighLevel highLevel){
        System.out.println(highLevel);
    }

    public void setMiniLowLevel(@Qualifier("miniLevel1") MiniLowLevel miniLowLevel1) {
        System.out.println("Qualifier MiniLowLevel1");
        System.out.println(miniLowLevel1);
    }
}
